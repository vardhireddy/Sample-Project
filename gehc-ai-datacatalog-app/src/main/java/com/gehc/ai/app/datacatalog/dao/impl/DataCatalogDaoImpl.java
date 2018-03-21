/*
 * DataCatalogDaoImpl.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.dao.impl;

import static com.gehc.ai.app.common.constants.ApplicationConstants.ANNOTATIONS;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.AnnotationDetails;
import com.gehc.ai.app.datacatalog.entity.GEClass;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.entity.Patient;

@Service
@Component
public class DataCatalogDaoImpl implements IDataCatalogDao{

	private static Logger logger = LoggerFactory.getLogger(DataCatalogDaoImpl.class);
	
	public static final String GE_CLASS ="ge-class";
	public static final String ABSENT ="absent";
	public static final String RECT = "rect";
	public static final String ELLIPSE = "ellipse";
	public static final String LINE = "line";
	public static final String POINT = "point";
	public static final String DATE_FROM = "dateFrom";
	public static final String DATE_TO = "dateTo";

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
            + " SELECT  11 ) AS indices WHERE org_id = :orgId and ";

    public static final String GE_CLASS_COUNTS_MIDDLE = " and JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) IS NOT NULL "
            + " ORDER BY id, idx) AS LABEL_JSON ";
           
    public static final String GE_CLASS_COUNTS_SUFFIX = " GROUP BY single_class";

	public static final String GE_CLASS_QUERY = "select distinct im.id, im.org_id, p.patient_id, im.modality, im.anatomy, im.instance_count "
			+ ", im.data_format, im.institution, im.equipment from image_set im "
			+ "inner join annotation an "
			+ "on an.image_set=im.id "
			+ "inner join patient p "
			+ "on p.id = im.patient_dbid ";
	
	private static final String GET_IMG_SERIES_DATA_BY_FILTERS = "  select distinct x.id, x.org_id, x.modality, x.anatomy, x.data_format, x.series_instance_uid, x.institution,  "
			+ " x.instance_count, x.equipment, x.patient_id, x.properties, x.upload_date "
			+" from (  select im.id, im.org_id,im.institution, im.modality, im.anatomy, im.instance_count, p.patient_id, im.data_format, im.equipment, "
			+ " im.series_instance_uid, CAST(im.properties as CHAR(20000)) properties, im.upload_date from patient p, image_set im "
			+ " where p.id = im.patient_dbid  and p.org_id= im.org_id) x ";
	private static final String SUFFIX_IMG_SERIES_DATA_BY_FILTERS = "  order by x.patient_id ";
	private static final String ANNOTATION_ABSENT_QUERY = " where x.id not in (select image_set from annotation an where x.org_id = an.org_id) and ";
	
	/*private static final String GET_ANNOTATION_INFO_BY_IMG_SERIES = "SELECT p.patient_id, im.series_instance_uid, an.id, an.type, "
			+ " CAST(JSON_EXTRACT(an.item, '$.object_name') as CHAR(500)), CAST(JSON_EXTRACT(an.item, '$.data') as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) as CHAR(500)) "
			+ " FROM patient p inner join image_set im on im.patient_dbid = p.id  "
			+ " inner join annotation an on an.image_set = im.id "
			+ " JOIN (  SELECT  0 AS idx UNION "
			+ " SELECT  1 AS idx UNION "
			+ " SELECT  2 AS idx UNION "
			+ " SELECT  3 AS idx UNION "
			+ " SELECT  4 AS idx UNION "
			+ " SELECT  5 AS idx UNION "
			+ " SELECT  6 AS idx UNION "
			+ " SELECT  8 AS idx UNION "
			+ " SELECT  9 AS idx UNION "
			+ " SELECT  10 AS idx UNION "
			+ " SELECT  11) AS indices "
			+ " WHERE JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) IS NOT NULL and im.id in (";*/
	
	private static final String GET_ANNOTATION_INFO_BY_IMG_SERIES = "SELECT p.patient_id, im.series_instance_uid, an.id, an.type, "
			+ " CAST(JSON_EXTRACT(an.item, '$.object_name') as CHAR(500)), CAST(JSON_EXTRACT(an.item, '$.data') as CHAR(10000)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[0]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[1]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[2]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[3]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[4]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[5]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[6]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[7]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[8]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[9]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[10]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(an.item, '$.coord_sys') as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.indication')) as CHAR(5000)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.findings')) as CHAR(10000)), "
			+ " CAST(JSON_EXTRACT(im.properties, '$.instances') as CHAR(20000)),"
			+ " CAST(JSON_EXTRACT(an.item, '$.origin') as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(an.item, '$.uri') as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(an.item, '$.format') as CHAR(500)) "
			+ " FROM patient p inner join image_set im on im.patient_dbid = p.id  "
			+ " inner join annotation an on an.image_set = im.id "
			+ " WHERE im.id in (";

	private static final String GET_ITEM_INFO_BY_ANNOTATION = "SELECT id, "
			+ " CAST(JSON_EXTRACT(item, '$.object_name') as CHAR(500)), CAST(JSON_EXTRACT(item, '$.data') as CHAR(20000)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[0]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[1]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[2]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[3]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[4]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[5]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[6]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[7]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[8]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[9]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[10]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, '$.coord_sys') as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.indication')) as CHAR(5000)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.findings')) as CHAR(10000)) "
			+ "FROM annotation "
			+ " WHERE ";

	public static final String IMG_SER_PATIENT = "select im.id, im.org_id, im.modality, im.anatomy, im.data_format, im.series_instance_uid, im.institution, im.instance_count, im.equipment, "
			+ " p.patient_id, p.age, p.gender, im.uri, CAST(im.properties as CHAR(20000)), im.upload_date "
			+ " from patient p, image_set im where im.id in ( ";
	public static final String IMG_SER_PATIENT_SUFFIX = " ) and p.org_id= im.org_id and p.id = im.patient_dbid order by p.patient_id ";
	
	protected static final List<Object> GE_CLASS_LIST = new ArrayList<Object>();
	
	@Autowired
	private EntityManager em;

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

	@Override
    public Map<Object, Object> geClassDataSummary(Map<String, String> filters, String orgId) {
                    StringBuilder annotQueryBuilder = new StringBuilder();        
                    if(filters.containsKey(ANNOTATIONS)){
                                    annotQueryBuilder.append(constructAnnotationWhereClause("type", filters.get(ANNOTATIONS)));
                    }
                    filters.remove(ANNOTATIONS);
                    Map<Object, Object> filterMap = new HashMap<Object, Object>();
                    if(null != orgId && !orgId.isEmpty()){
                                    StringBuilder queryBuilder = new StringBuilder();                    
                                    if (!filters.isEmpty()) {
                                                    queryBuilder.append(" inner join image_set on image_set.id = image_set where ");
                                                    for (Iterator<Map.Entry<String, String>> entries = filters.entrySet().iterator();entries.hasNext();) {
                                                                    Map.Entry<String, String> entry = entries.next();
                                                                    queryBuilder.append(getColumnQueryString(entry.getKey(), entry.getValue()));
                                                                    if (entries.hasNext())
                                                                                    queryBuilder.append(" and ");
                                                    }
                                    }
                                    String queryString = GE_CLASS_COUNTS_PREFIX + annotQueryBuilder + GE_CLASS_COUNTS_MIDDLE + queryBuilder + GE_CLASS_COUNTS_SUFFIX;
                                    logger.debug(" Query to get GE classes = " + queryString + " for org id = " + orgId);
                                    Query q = em.createNativeQuery(queryString);        // NOSONAR
                                    q.setParameter("orgId", orgId);
                                    @SuppressWarnings("unchecked")
                                    List<Object[]> objList = q.getResultList();
                                    if(null != objList && !objList.isEmpty()){
                            objList.stream().forEach((record) -> {
                                filterMap.put(record[1], record[0]);
                            });     
                                    }
                    }
                    return filterMap;
    }
	
	public GEClass [] getGEClasses(Map<String, Object> params){
		logger.debug("Getting GE classes ");
		ObjectMapper mapper = new ObjectMapper();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (GE_CLASS.equals(entry.getKey())) {
					try {
						return mapper.readValue(entry.getValue().toString(), GEClass [].class);
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
			return new GEClass[0];
	}


	private String quoteValues(String values) {
		StringBuilder builder = new StringBuilder();
		Iterator<String> valuesIterator = Arrays.asList(values.split(",")).iterator();

		while (valuesIterator.hasNext()) {
			builder.append("\"").append(valuesIterator.next()).append("\"");
			if (valuesIterator.hasNext()) {
				builder.append(", ");
			}
		}

		return builder.toString();
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.gehc.ai.app.dc.dao.IDataCatalogDao#getDataCatalog()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ImageSeries> getImgSeriesByFilters(Map<String, Object> params) {
		List<ImageSeries> imageSeriesList = new ArrayList<ImageSeries>();
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(GET_IMG_SERIES_DATA_BY_FILTERS);
			builder.append(constructQuery(params));
			builder.append(SUFFIX_IMG_SERIES_DATA_BY_FILTERS);
			logger.debug("Query to get image series by filters = " + builder.toString());
			Query q = em.createNativeQuery(builder.toString());	// NOSONAR

			List<Object[]> objList = q.getResultList();
			if(null != objList && !objList.isEmpty()){
				ObjectMapper mapper = new ObjectMapper();
		        objList.stream().forEach((record) -> {
		        	Patient p = new Patient();
		        	ImageSeries imgSeries = new ImageSeries();
		        	if (record[0] instanceof BigInteger){
		        		imgSeries.setId(((BigInteger) record[0]).longValue());
		        	}
		        	imgSeries.setOrgId((String) record[1]);
		        	imgSeries.setModality((String) record[2]);
		        	imgSeries.setAnatomy((String) record[3]);
		        	imgSeries.setDataFormat((String) record[4]);
		        	imgSeries.setSeriesInstanceUid((String) record[5]);
		        	imgSeries.setInstitution((String) record[6]);
		        	imgSeries.setInstanceCount((int) record[7]);
		        	imgSeries.setEquipment((String) record[8]);
		        	p.setPatientId((String) record[9]);
		        	imgSeries.setPatient(p);
		        	try {
						imgSeries.setProperties((Object) mapper.readValue(record[10].toString(), Object.class));
					} catch (IOException e) {
                        logger.error("Properties not available for image id "+ imgSeries.getId() + e);
					}
		        	imgSeries.setUploadDate(((Timestamp)record[11]).toLocalDateTime());
		        	imageSeriesList.add(imgSeries);
		        });
		        logger.debug("Image series lis size " + imageSeriesList.size());
			}
		}catch(Exception e){
			logger.error("Dao error while getImgSeriesByFilters", e);
			throw e;
		}
		return imageSeriesList;
	}
	
	String constructQuery(Map<String, Object> params) {
		boolean geclassPresent = false;
		String dateRangeQuery = null;
		Map<String, Object> geclassParams = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder();
		try{
			if(params.containsKey(GE_CLASS)){
				geclassPresent = true;
				geclassParams.put(GE_CLASS, params.get(GE_CLASS));
				params.remove(GE_CLASS);
			}
			dateRangeQuery = buildDateRangeQuery(params);
			if (params.size() > 0) {
				buildAnnotationQuery(params, builder);
				
				for (Iterator<Map.Entry<String, Object>> entries = params.entrySet().iterator();entries.hasNext();) {
					Map.Entry<String, Object> entry = entries.next();
					String key = entry.getKey();
					String values = entry.getValue().toString();
					builder.append(constructWhereClause(key,values));
					if (entries.hasNext()) {
						builder.append(" AND ");
					}
				}
			}
			if(dateRangeQuery != null){
				builder.append(dateRangeQuery);
			}
			if(geclassPresent){
				builder.append(getGEClassQuery(geclassParams));
			}
		}catch(Exception e){
			logger.error("Dao error while constructQuery", e);
			throw e;
		}
		return builder.toString();
	}

	/**
	 * @param params
	 * @param dateRangeQuery
	 * @return
	 */
	private String buildDateRangeQuery(Map<String, Object> params) {
		String dateRangeQuery = null;
		if(params.containsKey(DATE_FROM)){
            dateRangeQuery = " and x.upload_date between date_format(\""+dateFrom+"\", \"%Y-%m-%d %H:%i\") "
                    + "and date_format(date_add(\""+dateTo+"\", INTERVAL 1 MINUTE), \"%Y-%m-%d %H:%i\")";
            params.remove(DATE_FROM);
			params.remove(DATE_TO);
		}
		return dateRangeQuery;
	}

	private void buildAnnotationQuery(Map<String, Object> params, StringBuilder builder) {
		if(params.containsKey(ANNOTATIONS)){
			if(!ABSENT.equalsIgnoreCase(params.get(ANNOTATIONS).toString())){
				builder.append(", annotation an where an.image_set = x.id and an.org_id = x.org_id and ");
				builder.append(constructAnnotationWhereClause("type", params.get(ANNOTATIONS).toString()));
				builder.append(" AND ");
			}else{
				builder.append(ANNOTATION_ABSENT_QUERY);
			}
			params.remove(ANNOTATIONS);
		}else{
			builder.append(" WHERE ");
		}
	}

	private String constructWhereClause(String param, String values) {
		StringBuilder whereClause = new StringBuilder().append("x."+param + " IN (") ;
        whereClause.append(quoteValues(values));
		whereClause.append(")");
		return whereClause.toString();
	}
	
	private String constructAnnotationWhereClause(String param, String values) {
		StringBuilder whereClause = new StringBuilder().append(param + " IN (") ;
        whereClause.append(quoteValues(values));
		whereClause.append(")");
		return whereClause.toString();
	}
	
	private String getGEClassQuery(Map<String, Object> params) {
		logger.debug("In getGEClassQuery ");
		ObjectMapper mapper = new ObjectMapper();	
		GEClass [] geClasses = getGEClasses(params);
        StringBuilder queryBuilder = new StringBuilder();
        mapper.setSerializationInclusion(Include.NON_NULL);
        for (int k = 0; k < geClasses.length; k++) {
        	queryBuilder.append(k==0? " and (" : "or ");
        	try {
				queryBuilder.append("JSON_CONTAINS(an.item, '" + mapper.writeValueAsString(geClasses[k]) + "', '$.properties.ge_class') ");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
        }
        queryBuilder.append(")");      
        logger.debug(" Query with GE class is " + queryBuilder);
        return queryBuilder.toString();
	}

	@Override
	public List<AnnotationDetails> getAnnotationsByDSId(List<Long> imgSerIdLst) {
		StringBuilder builder = new StringBuilder();
		builder.append(GET_ANNOTATION_INFO_BY_IMG_SERIES);
            for (Iterator<Long> iter =  imgSerIdLst.iterator(); iter.hasNext();){
                builder.append(iter.next());
                if (iter.hasNext()) {
					builder.append(",");
				}
            }
		builder.append(")");
		logger.debug("Query to get annotation = " + builder.toString());
		Query q = em.createNativeQuery(builder.toString());	// NOSONAR		
		List<AnnotationDetails> annotationByDSList = new ArrayList<AnnotationDetails>();
		List<Object[]> objList = q.getResultList();
		if(null != objList && !objList.isEmpty()){		
	        objList.stream().forEach((record) -> {
	        	AnnotationDetails annotationByDS = new AnnotationDetails();
	        	annotationByDS.setPatientId((String) record[0]);
	        	annotationByDS.setSeriesUID((String) record[1]);
	        	if (record[2] instanceof Integer){
	        		annotationByDS.setAnnotationId(((Integer) record[2]).longValue());
	        	}    
	        	annotationByDS.setAnnotationType((String) record[3]);
	        	annotationByDS.setName((String) record[4]);
	        	annotationByDS.setData((Object) record[5]);
	        	annotationByDS.setGeClass((Object) record[6]);
	        	annotationByDS.setGeClass1((Object) record[7]);
	        	annotationByDS.setGeClass2((Object) record[8]);
	        	annotationByDS.setGeClass3((Object) record[9]);
	        	annotationByDS.setGeClass4((Object) record[10]);
	        	annotationByDS.setGeClass5((Object) record[11]);
	        	annotationByDS.setGeClass6((Object) record[12]);
	        	annotationByDS.setGeClass7((Object) record[13]);
	        	annotationByDS.setGeClass8((Object) record[14]);
	        	annotationByDS.setGeClass9((Object) record[15]);
	        	annotationByDS.setGeClass10((Object) record[16]);
	        	annotationByDS.setCoordSys((String) record[17]);
	        	annotationByDS.setIndication((String) record[18]);
	        	annotationByDS.setFindings((String) record[19]);
	        	annotationByDS.setInstances((Object) record[20]);
	        	annotationByDS.setMaskOrigin((Object) record[21]);
	        	annotationByDS.setMaskURI((String) record[22]);
	        	annotationByDS.setMaskFormat((String) record[23]);
	        	annotationByDSList.add(annotationByDS);
	        });     
	        logger.debug("Annotation lis size " + annotationByDSList.size());
		}
		return annotationByDSList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAnnotationsIds(Annotation annotation) {
		List<Integer> ids = new ArrayList<Integer>();
		String queryString = getQueryStringForAnnotationIds(annotation);
		Query q = em.createNativeQuery(queryString); // NOSONAR
		List<Object[]> objList = q.getResultList();
		LinkedHashMap item = (LinkedHashMap) annotation.getItem();

		if (null != objList && !objList.isEmpty()) {
			objList.stream().forEach((record) -> {
				ArrayList<String> types = getRectEllipseTypes();
				ArrayList<String> otherTypes = getLinePointTypes();

				try {
					boolean dataKeysPresent = false;
					//data key validation

					if (types.contains(annotation.getType())) {
						dataKeysPresent = isDataKeysPresent(item, record);
					}
					if (otherTypes.contains(annotation.getType())) {
						dataKeysPresent = isDataKeysLinePointPresent(item, record);
					}

					//properties ge_class validation
					boolean geClassKeysPresent = isAllGeClassKeysPresent(item, record);

					//verify all records match
					verifyAllRecordsExistInDb(annotation, ids, item, record, dataKeysPresent, geClassKeysPresent);

				} catch (IOException e) {
					logger.warn("Unable to verify if annotation present");

				}

			});
		}

		return ids;
	}

	private ArrayList<String> getLinePointTypes() {
		ArrayList<String> otherTypes = new ArrayList<String>();
		otherTypes.add(LINE);
		otherTypes.add(POINT);
		return otherTypes;
	}

	private ArrayList<String> getRectEllipseTypes() {
		ArrayList<String> types = new ArrayList<String>();
		types.add(RECT);
		types.add(ELLIPSE);
		return types;
	}

	private boolean isAllGeClassKeysPresent(LinkedHashMap item, Object[] record) throws IOException {
		return (isGeClassKeysPresent(item, record, 0,3) &&
				isGeClassKeysPresent(item, record, 1,4) &&
				isGeClassKeysPresent(item, record, 2,5) &&
				isGeClassKeysPresent(item, record, 3,6) &&
				isGeClassKeysPresent(item, record, 4,7) &&
				isGeClassKeysPresent(item, record, 5,8) &&
				isGeClassKeysPresent(item, record, 6,9) &&
				isGeClassKeysPresent(item, record, 7,10) &&
				isGeClassKeysPresent(item, record, 8,11) &&
				isGeClassKeysPresent(item, record, 9,12) &&
				isGeClassKeysPresent(item, record, 10,13)
		);
	}

	private String getQueryStringForAnnotationIds(Annotation annotation) {
		StringBuilder annotQueryBuilder = new StringBuilder();
		annotQueryBuilder.append(constructAnnotationWhereClause("org_id", annotation.getOrgId()));
		annotQueryBuilder.append(" AND ");
		annotQueryBuilder.append(constructAnnotationWhereClause("image_set", annotation.getImageSetId().toString()));
		annotQueryBuilder.append(" AND ");
		annotQueryBuilder.append(constructAnnotationWhereClause("annotator_id", annotation.getAnnotatorId()));
		annotQueryBuilder.append(" AND ");
		annotQueryBuilder.append(constructAnnotationWhereClause("annotation_tool", annotation.getAnnotationTool()));
		annotQueryBuilder.append(" AND ");
		annotQueryBuilder.append(constructAnnotationWhereClause("type", annotation.getType()));
		return GET_ITEM_INFO_BY_ANNOTATION + annotQueryBuilder;
	}

	private void verifyAllRecordsExistInDb(Annotation annotation, List<Integer> ids, LinkedHashMap item, Object[] record, boolean dataKeysPresent, boolean geClassKeysPresent) {
		if (item.get("object_name") == null || item.get("object_name").equals(getReplace(record[1]))) {
			if (item.get("coord_sys") == null || item.get("coord_sys").equals((String) getReplace(record[14]))) {
				if ((!annotation.getType().equals("label") && dataKeysPresent) || (annotation.getType().equals("label") && geClassKeysPresent)) {
					verifyIndicationFindingsPresentAndAddToIds(ids, item, record);
				}
			}

		}
	}

	private void verifyIndicationFindingsPresentAndAddToIds(List<Integer> ids, LinkedHashMap item, Object[] record) {
		boolean indicationPresent = isIndicationPresent(item, record);
		if (indicationPresent) {
			boolean findingsPresent = isFindingsPresent(item, record);
			if (findingsPresent) {
				logger.info("Annotation already exists so not saving");
				ids.add((Integer) record[0]);
			}
		}
	}

	private boolean isDataKeysPresent(LinkedHashMap item, Object[] record) throws IOException {
		boolean dataKeysPresent = false;
		if (record[2] != null) {
			Map<String, List<Long>> dataJson = (Map<String, List<Long>>) (item.get("data"));
			Map<String, List> resultMap = new ObjectMapper().readValue((String) record[2],
					new TypeReference<Map<String, List>>() {
					});

			for (Map.Entry<String, List<Long>> entry : dataJson.entrySet()) {
				String key =entry.getKey();
				if (dataJson.get(key).equals(resultMap.get(key))) {
					dataKeysPresent = true;

				} else {
					dataKeysPresent = false;
					break;
				}
			}
		}
		return dataKeysPresent;
	}

	private boolean isDataKeysLinePointPresent(LinkedHashMap item, Object[] record) {
		boolean dataKeysPresent = false;
		if (record[2] != null) {
			if (item.get("data").toString().equals(record[2])) {
				dataKeysPresent = true;

			} else {
				dataKeysPresent = false;
			}
		}
		return dataKeysPresent;
	}

	private boolean isGeClassKeysPresent(LinkedHashMap item, Object[] record, int geclassNum, int recordNum) throws IOException {
		boolean geClassKeysPresent = false;
		if (record[recordNum] != null) {
			Map<String, List<String>> geClassJson = (Map<String, List<String>>) item.get("properties");
			Map<String, String> geClassMap = new ObjectMapper().readValue((String) record[recordNum],
					new TypeReference<Map<String, String>>() {
					});
			int size = geClassJson.get("ge_class").size();
			if (size >= geclassNum+1) {
				String geClassValue = geClassJson.get("ge_class").toArray()[geclassNum].toString();
				if (geClassValue.equals(geClassMap.toString())) {
					geClassKeysPresent = true;

				} else {
					geClassKeysPresent = false;
				}
			} else {
				geClassKeysPresent = true;
			}
		}else {
			geClassKeysPresent = true;
		}
		return geClassKeysPresent;
	}

	private boolean isFindingsPresent(LinkedHashMap item, Object[] record) {
		boolean findingsPresent = false;
		if (record[16] != null) {
			Map<String, String> geClassJson = (Map<String, String>) item.get("properties");
			if (geClassJson.containsKey("findings")) {
				String findingsValue = geClassJson.get("findings");
				if (((String) record[16]).contains(findingsValue)) {
					findingsPresent = true;
				} else {
					findingsPresent = false;
				}
			}
		} else {
			findingsPresent = true;

		}
		return findingsPresent;
	}

	private boolean isIndicationPresent(LinkedHashMap item, Object[] record) {
		boolean indicationPresent = false;
		if (record[15] != null) {
			Map<String, String> geClassJson = (Map<String, String>) item.get("properties");
			if (geClassJson.containsKey("indication")) {
				String findingsValue = geClassJson.get("indication");
				if (((String) record[15]).contains(findingsValue)) {
					indicationPresent = true;

				} else {
					indicationPresent = false;
				}

			}
		} else {
			indicationPresent = true;
		}
		return indicationPresent;
	}

	private String getReplace(Object o) {
		if (o != null) {
			return o.toString().replace("\"", "");
		}
		return null;
	}

	@Override
	public List<ImageSeries> getImgSeriesWithPatientByIds(List<Long> imgSerIdLst) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(IMG_SER_PATIENT);
		for(Iterator<Long> iter = imgSerIdLst.iterator(); iter.hasNext();){
			queryBuilder.append(iter.next());
			if (iter.hasNext()) {
				queryBuilder.append(",");
			}
		}
		queryBuilder.append(IMG_SER_PATIENT_SUFFIX);
		logger.debug("queryBuilder = "+ queryBuilder);
		Query q = em.createNativeQuery(queryBuilder.toString());	// NOSONAR		
		List<ImageSeries> imageSeriesList = new ArrayList<ImageSeries>();
		List<Object[]> objList = q.getResultList();
		if(null != objList && !objList.isEmpty()){		
			ObjectMapper mapper = new ObjectMapper();
	        objList.stream().forEach((record) -> {
	        	Patient p = new Patient();
	        	ImageSeries imgSeries = new ImageSeries();
	        	if (record[0] instanceof BigInteger){
	        		imgSeries.setId(((BigInteger) record[0]).longValue());
	        	}
	        	imgSeries.setOrgId((String) record[1]);
	        	imgSeries.setModality((String) record[2]);
	        	imgSeries.setAnatomy((String) record[3]);
	        	imgSeries.setDataFormat((String) record[4]);
	        	imgSeries.setSeriesInstanceUid((String) record[5]);
	        	imgSeries.setInstitution((String) record[6]);
	        	imgSeries.setInstanceCount((int) record[7]);
	        	imgSeries.setEquipment((String) record[8]);
	        	p.setPatientId((String) record[9]);
	        	p.setAge((String) record[10]);
	        	p.setGender((String) record[11]);
	        	imgSeries.setPatient(p);
	        	imgSeries.setUri((String) record[12]);
	        	try {
					imgSeries.setProperties((Object) mapper.readValue(record[13].toString(), Object.class));
				} catch (IOException e) {
					// TODO throw the exception
					e.printStackTrace();
				}
	        	imgSeries.setUploadDate(((Timestamp)record[14]).toLocalDateTime());
	        	imageSeriesList.add(imgSeries);
	        });     
		}
        logger.debug("Image series with patient, list size " + imageSeriesList.size());
        return imageSeriesList;
	}
}
