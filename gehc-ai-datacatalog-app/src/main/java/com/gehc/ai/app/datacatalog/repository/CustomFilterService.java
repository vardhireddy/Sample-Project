package com.gehc.ai.app.datacatalog.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.GEClass;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.ImageSeries_;
import com.mysql.fabric.xmlrpc.base.Array;


@Service
public class CustomFilterService {
	
	public static final String GE_CLASS_COUNTS = "SELECT count(distinct image_set) as image_count, CAST(single_class as CHAR(500)) FROM ( "
			 + " SELECT image_set, JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) AS single_class "
			 + " FROM Annotation JOIN (  SELECT  0 AS idx UNION "
			 + " SELECT  1 AS idx UNION "
			 + " SELECT  2 AS idx UNION "
			 + " SELECT  3 AS idx UNION "
			 + " SELECT  4 AS idx UNION "
			 + " SELECT  5 AS idx UNION "
			 + " SELECT  6 AS idx UNION "
			 + " SELECT  8 AS idx UNION "
			 + " SELECT  9 AS idx UNION "
			 + " SELECT  10 AS idx UNION "
			 + " SELECT  11 ) AS indices WHERE org_id = :orgId and JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) IS NOT NULL "
			 + " ORDER BY id, idx) AS LABEL_JSON GROUP BY single_class";
	
	public static final String SIMPLE_JSON_QUERY = "SELECT CAST(JSON_EXTRACT(item, '$.properties.ge_class') AS CHAR(500)) FROM annotation";
	
	static final String GE_CLASS_QUERY = "select distinct im.id, p.patient_id, im.modality, im.anatomy from image_set im "
			+ "inner join annotation an "
			+ "on an.image_set=im.id "
			+ "inner join patient p "
			+ "on p.id = im.patient_dbid ";

	static final List<Object> GE_CLASS_LIST = new ArrayList<Object>();
	
	private static Logger logger = LoggerFactory.getLogger(CustomFilterService.class);

	
	@Autowired
	EntityManager em;
	
	public void getSelectedColumns(List<String> attrib) {
		logger.info("=========entity manager=========" + em);
		CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		final Root<ImageSeries> root = cq.from(ImageSeries.class);
		cq.multiselect(root.get(ImageSeries_.id), root.get(ImageSeries_.orgId));
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
	
	public Map<Object, Object> geClassDataSummary(String orgId) {
		logger.info(" * In service geClassDataSummary, orgId = " + orgId);
		Query q = em.createNativeQuery(GE_CLASS_COUNTS);
		q.setParameter("orgId", orgId);
		@SuppressWarnings("unchecked")
		List<Object[]> objList = q.getResultList();
		
		Map<Object, Object> filterMap = new HashMap<Object, Object>();
        objList.stream().forEach((record) -> {
            logger.info(record[0].toString() + ",......." + record[1].toString());
            filterMap.put(record[1], record[0]);
//            ObjectMapper mapper = new ObjectMapper();
//				try {
//					filterMap.put(mapper.readValue(record[1].toString(), Object.class), record[0]);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			
           
        });
		return filterMap;
	}

	public void getImageSetCount(Map<String, Object> params) {
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
			
		//	Query q = em.createNativeQuery(GE_CLASS_COUNTS);
		//	q.setParameter("orgId", "4fac7976-e58b-472a-960b-42d7e3689f20");

//			@SuppressWarnings("unchecked")
//			List<Object[]> objList = q.getResultList();
			
//			Map<String, String> filterMap = new HashMap<String, String>();
//	        objList.stream().forEach((record) -> {
//	            logger.info(record[0].toString() + ",......." + record[1].toString());
//	            filterMap.put(record[1].toString(), record[0].toString());
//	        });
			
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
		}
	
	public void dataDetails(Map<String, Object> params) {
		logger.info(" dataDetails ");
		ObjectMapper mapper = new ObjectMapper();

		
		GEClass [] geClasses = {};for (Map.Entry<String, Object> entry : params.entrySet()) {
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
		
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			logger.info("Key : " + entry.getKey() + " Value : " + entry.getValue() + ": " + entry.getClass());
			if ("modality".equals(entry.getKey())) {
				logger.info("- modlaity is " + entry.getValue().toString());
				
				//buf.append("modality in ");
				//buf.append(entry.getValue().toString());
			}
		}
		
	//	Query q = em.createNativeQuery(GE_CLASS_COUNTS);
	//	q.setParameter("orgId", "4fac7976-e58b-472a-960b-42d7e3689f20");

//		@SuppressWarnings("unchecked")
//		List<Object[]> objList = q.getResultList();
		
//		Map<String, String> filterMap = new HashMap<String, String>();
//        objList.stream().forEach((record) -> {
//            logger.info(record[0].toString() + ",......." + record[1].toString());
//            filterMap.put(record[1].toString(), record[0].toString());
//        });


        for (int k = 0; k < geClasses.length; k++) {
        	buf.append(k==0? "where " : "or ");
        	try {
				buf.append("JSON_CONTAINS(an.item, '" + mapper.writeValueAsString(geClasses[k]) + "', '$.properties.ge_class') ");
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        logger.info("dataDetails query is " + buf);
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
	}
	
	private List<String> getListOfStringsFromParams(String values) {
		List<String> valueLst = new ArrayList<String>();
		if (null != values && !values.isEmpty()) {
			String[] valueStrings = values.split(",");
			if (null != valueStrings && valueStrings.length > 0) {
				for (int i = 0; i < valueStrings.length; i++)
					valueLst.add(valueStrings[i].toLowerCase());
			}
		}
		return valueLst;
	}

	private List<Long> getListOfLongsFromParams(String values) {
		List<Long> valueLst = new ArrayList<Long>();
		if (null != values && !values.isEmpty()) {
			String[] valueStrings = values.split(",");
			if (null != valueStrings && valueStrings.length > 0) {
				for (int i = 0; i < valueStrings.length; i++)
					valueLst.add(Long.valueOf(valueStrings[i]));
			}
		}
		return valueLst;
	}
}
