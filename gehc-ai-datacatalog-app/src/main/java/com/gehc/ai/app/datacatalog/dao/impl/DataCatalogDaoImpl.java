package com.gehc.ai.app.datacatalog.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.GEClass;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Patient;

@Service
@Component
public class DataCatalogDaoImpl implements IDataCatalogDao{

	private static Logger logger = LoggerFactory.getLogger(DataCatalogDaoImpl.class);
	
	public static final String GE_CLASS ="ge-class";
	
	public static final String GE_CLASS_COUNTS_PREFIX = "SELECT count(distinct image_set) as image_count, CAST(single_class as CHAR(500)) FROM ( "
			 + " SELECT image_set, JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) AS single_class "
			 + " FROM annotation JOIN ( SELECT  0 AS idx UNION "
			 + " SELECT  1 AS idx UNION "
			 + " SELECT  2 AS idx UNION "
			 + " SELECT  3 AS idx UNION "
			 + " SELECT  4 AS idx UNION "
			 + " SELECT  5 AS idx UNION "
			 + " SELECT  6 AS idx UNION "
			 + " SELECT  7 AS idx UNION "
			 + " SELECT  8 AS idx UNION "
			 + " SELECT  9 AS idx UNION "
			 + " SELECT  10 AS idx UNION "
			 + " SELECT  11 ) AS indices WHERE org_id = :orgId and JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) IS NOT NULL "
			 + " ORDER BY id, idx) AS LABEL_JSON ";
	public static final String GE_CLASS_COUNTS_SUFFIX = " GROUP BY single_class";
	public static final String GE_CLASS_COUNTS = GE_CLASS_COUNTS_PREFIX + GE_CLASS_COUNTS_SUFFIX;
//	public static final String GE_CLASS_COUNTS_WITH_FILTER = GE_CLASS_COUNTS_PREFIX + " :filter " + GE_CLASS_COUNTS_SUFFIX;
	
	public static final String SIMPLE_JSON_QUERY = "SELECT CAST(JSON_EXTRACT(item, '$.properties.ge_class') AS CHAR(500)) FROM annotation";
	
	static final String GE_CLASS_QUERY = "select distinct im.id, im.org_id, p.patient_id, im.modality, im.anatomy, im.instance_count "
			+ " from image_set im "
			+ "inner join annotation an "
			+ "on an.image_set=im.id "
			+ "inner join patient p "
			+ "on p.id = im.patient_dbid ";

	static final List<Object> GE_CLASS_LIST = new ArrayList<Object>();
	
	@Autowired
	EntityManager em;
	
	
	public void getSelectedColumns(List<String> attrib) {
		logger.info("=========entity manager=========" + em);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		final Root<ImageSeries> root = cq.from(ImageSeries.class);
		//cq.multiselect(root.get(ImageSeries_.id), root.get(ImageSeries_.orgId));
		List<Tuple> tupleResult = em.createQuery(cq).getResultList();
		
		for(int k = 0; k < tupleResult.size(); k++) {
			Tuple t = tupleResult.get(k);
			logger.info(t.get(1).toString());
		}
		logger.info("returns tuple =========" + tupleResult.size());
		

//		Query q = em.createNativeQuery(SIMPLE_JSON_QUERY);
//		List objList = q.getResultList();
		Query q = em.createNativeQuery(GE_CLASS_COUNTS);

		@SuppressWarnings("unchecked")
		List<Object[]> objList = q.getResultList();
		
		Map<String, String> filterMap = new HashMap<String, String>();
        objList.stream().forEach((record) -> {
            logger.info(record[0].toString() + ",......." + record[1].toString());
            filterMap.put(record[1].toString(), record[0].toString());
            GE_CLASS_LIST.add(record[1]);
        });
		
        StringBuilder buf = new StringBuilder();
        buf.append(GE_CLASS_QUERY);
        for (int k = 0; k < GE_CLASS_LIST.size(); k++) {
        	buf.append(k==0? "where " : "or ");
        	buf.append("JSON_CONTAINS(an.item, '" + GE_CLASS_LIST.get(k) + "', '$.properties.ge_class') ");
        }
        
        logger.info("query is " + buf);
        
		logger.info("result size " + objList.size());
	}
	
	private static String getColumnQueryString(String column, String values) {
		StringBuilder q = new StringBuilder();
		q.append(" " + column.replaceAll("-", "_") + " in (");
		String [] candidates = values.split(",");
		for (int k = 0; k < candidates.length; k++) {
			if (!candidates[k].startsWith("'") && !candidates[k].startsWith("\""))
				q.append("'" + candidates[k] + "'");
			else
				q.append(candidates[k]);
			
			if (k < candidates.length - 1)
				q.append(",");
		}
		q.append(")");
		return q.toString();
	}
	
	static Map<String, String> ANNOTATION_COLUMN_MAP = new HashMap<String, String>();
	static Map<String, String> IMAGESET_COLUMN_MAP = new HashMap<String, String>();
	static {
		ANNOTATION_COLUMN_MAP.put("org-id", "org_id");
		ANNOTATION_COLUMN_MAP.put("annotation-type", "type");
		IMAGESET_COLUMN_MAP.put("anatomy", "anatomy");
		IMAGESET_COLUMN_MAP.put("modality", "modality");
	}

	@Override
	public Map<Object, Object> geClassDataSummary(Map<String, String> filters) {
		logger.info(" * In DAO geClassDataSummary, orgId = " + filters.get("org-id"));
		
		StringBuilder buf = new StringBuilder();
		Set<String> keys = filters.keySet();
		
		Map<String, String> imageSetAttributes = new HashMap<String, String>();
		Map<String, String> annotationAttributes = new HashMap<String, String>();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String key = it.next();
			if (IMAGESET_COLUMN_MAP.containsKey(key))
				imageSetAttributes.put(IMAGESET_COLUMN_MAP.get(key), filters.get(key));
			if (ANNOTATION_COLUMN_MAP.containsKey(key))
				annotationAttributes.put(ANNOTATION_COLUMN_MAP.get(key), filters.get(key));
		}
		
		if (!imageSetAttributes.isEmpty()) {
			buf.append(" inner join image_set on image_set.id = image_set where ");
			keys = imageSetAttributes.keySet();
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String key = it.next();
				buf.append(getColumnQueryString(key, imageSetAttributes.get(key)));
				if (it.hasNext())
					buf.append(" and ");
			}
		}
		
		String prefix = GE_CLASS_COUNTS_PREFIX;
		if (!annotationAttributes.isEmpty()) {
			keys = annotationAttributes.keySet();
			StringBuilder annotBuf = new StringBuilder();
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String key = it.next();
				if (!"org_id".equals(key))
					annotBuf.append(getColumnQueryString(key, annotationAttributes.get(key)) + " and ");
			}
			
			int ind = GE_CLASS_COUNTS_PREFIX.lastIndexOf("JSON_EXTRACT(");
			prefix = GE_CLASS_COUNTS_PREFIX.substring(0, ind) + annotBuf + GE_CLASS_COUNTS_PREFIX.substring(ind);
			
		}
		String queryString = prefix + buf + GE_CLASS_COUNTS_SUFFIX;
		
		logger.info("query string for ge class data summary = " + queryString);
		Query q = em.createNativeQuery(queryString);
		
		q.setParameter("orgId", filters.get("org-id").toString());
		

		@SuppressWarnings("unchecked")
		List<Object[]> objList = q.getResultList();
		
		Map<Object, Object> filterMap = new HashMap<Object, Object>();
        objList.stream().forEach((record) -> {
            filterMap.put(record[1], record[0]);
        });
        
        logger.info("" + objList.size() + " rows returned");
		return filterMap;
	}

	@Override
	public int getImageSetCount(Map<String, Object> params) {
			logger.info(" getImageSetCount ");
			ObjectMapper mapper = new ObjectMapper();

			
			GEClass [] geClasses = {};
			
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				logger.info("Key : " + entry.getKey() + " Value : " + entry.getValue() + ": " + entry.getClass());
				if ("ge_class".equals(entry.getKey())) {
					
					try {
						geClasses = mapper.readValue(entry.getValue().toString(), GEClass [].class);
						logger.info("json parsing result: " + Arrays.toString(geClasses));
					} catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
	        StringBuilder buf = new StringBuilder();
	        buf.append(GE_CLASS_QUERY);
	        mapper.setSerializationInclusion(Include.NON_NULL);

	        for (int k = 0; k < geClasses.length; k++) {
	        	buf.append(k==0? "where " : "or ");
	        	try {
					buf.append("JSON_CONTAINS(an.item, '" + mapper.writeValueAsString(geClasses[k]) + "', '$.properties.ge_class') ");
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        
	        logger.info("------ query is " + buf);
	        Query q = em.createNativeQuery(buf.toString());
			//	q.setParameter("orgId", "4fac7976-e58b-472a-960b-42d7e3689f20");
	        @SuppressWarnings("unchecked")
			List<Object[]> objList = q.getResultList();
			
			Map<String, String> filterMap = new HashMap<String, String>();
	        objList.stream().forEach((record) -> {
	           // logger.info(record[0].toString() + ",---" + record[1].toString() + ",......." + record[2].toString()+ ",......." + record[3].toString());
	            filterMap.put(record[1].toString(), record[0].toString());
	        });
	        
			logger.info("result size " + objList.size());
			return objList.size();
		}

	@Override
	public Map<String, Object> dataSummary(String groupby, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ImageSeries> getImgSeries(Map<String, Object> params, List<ImageSeries> imgSeriesLst) {
		logger.info(" In DAO, getImgSeries ");
		ObjectMapper mapper = new ObjectMapper();	
		GEClass [] geClasses = getGEClasses(params);
		//Get image set
		List<Long> imgSeriesIdLst = getImgSeriesIdLst(imgSeriesLst);
		StringBuilder imageSeriesIds = new StringBuilder();
		imageSeriesIds.append(" image_set in (");
		for (Iterator<Long> imgSeriesIdItr = imgSeriesIdLst.iterator(); imgSeriesIdItr.hasNext();) {
			imageSeriesIds.append(imgSeriesIdItr.next() + (imgSeriesIdItr.hasNext() ? "," : ""));
		}
		imageSeriesIds.append(")");
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(GE_CLASS_QUERY);
        mapper.setSerializationInclusion(Include.NON_NULL);
        for (int k = 0; k < geClasses.length; k++) {
        	queryBuilder.append(k==0? "where (" : "or ");
        	try {
				queryBuilder.append("JSON_CONTAINS(an.item, '" + mapper.writeValueAsString(geClasses[k]) + "', '$.properties.ge_class') ");
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        queryBuilder.append(")");      
        queryBuilder.append(" and " + imageSeriesIds);       
        logger.info("dataDetails query is " + queryBuilder);
        Query q = em.createNativeQuery(queryBuilder.toString());
        return getImaSeriesLst(q.getResultList());
	}
	
	public GEClass [] getGEClasses(Map<String, Object> params){
		ObjectMapper mapper = new ObjectMapper();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			//logger.info("Key : " + entry.getKey() + " Value : " + entry.getValue() + ": " + entry.getClass());
			if (GE_CLASS.equals(entry.getKey())) {
					try {
						return mapper.readValue(entry.getValue().toString(), GEClass [].class);
					} catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
			return null;
	}
	
	public List<Long> getImgSeriesIdLst(List<ImageSeries> imgSeriesLst){
		List<Long> imgSeriesIdLst = new ArrayList<Long>();
		for (Iterator<ImageSeries> imgSeriesItr = imgSeriesLst.iterator(); imgSeriesItr.hasNext();) {
			ImageSeries imageSeries = (ImageSeries) imgSeriesItr.next();
			imgSeriesIdLst.add(imageSeries.getId());
		}
		return imgSeriesIdLst;
	}
	
	public List<ImageSeries> getImaSeriesLst(List<Object[]> objList){
		List<ImageSeries> result = new ArrayList<ImageSeries>();
		objList.stream().forEach((record) -> {
			ImageSeries imgSeries = new ImageSeries();
			imgSeries.setId(Long.valueOf(record[0].toString()));
			imgSeries.setOrgId(record[1].toString());
			Patient p = new Patient();
			p.setPatientId(record[2].toString());
			imgSeries.setPatient(p);
			imgSeries.setModality(record[3].toString());
			imgSeries.setAnatomy(record[4].toString());
			imgSeries.setInstanceCount(Integer.valueOf(record[5].toString()));
			result.add(imgSeries);
		});
		logger.info("result size " + objList.size());
		return result;
	}
}
