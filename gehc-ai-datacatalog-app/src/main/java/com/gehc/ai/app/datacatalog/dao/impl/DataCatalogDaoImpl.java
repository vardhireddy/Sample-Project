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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
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
	
	private static final String GET_IMG_SERIES_DATA_BY_FILTERS = "  select distinct x.id, x.org_id, x.modality, x.anatomy, x.data_format, x.series_instance_uid, x.institution,  x.instance_count, x.equipment, x.patient_id "
			+" from (  select im.id, im.org_id,im.institution, im.modality, im.anatomy, im.instance_count, p.patient_id, im.data_format, im.equipment, im.series_instance_uid from patient p, image_set im "
			+ " where p.id = im.patient_dbid  and p.org_id= im.org_id) x ";
	
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
			+ " CAST(JSON_EXTRACT(an.item, '$.object_name') as CHAR(500)), CAST(JSON_EXTRACT(an.item, '$.data') as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[0]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[1]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[2]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[3]')) as CHAR(500)), "
			+ " CAST(JSON_EXTRACT(item, CONCAT('$.properties.ge_class[4]')) as CHAR(500)) "
			+ " FROM patient p inner join image_set im on im.patient_dbid = p.id  "
			+ " inner join annotation an on an.image_set = im.id "
			+ " WHERE im.id in (";
			
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
		StringBuilder builder = new StringBuilder();
		builder.append(GET_IMG_SERIES_DATA_BY_FILTERS);
		builder.append(constructQuery(params));
		logger.debug("Query to get image series by filters = " + builder.toString());
		Query q = em.createNativeQuery(builder.toString());	// NOSONAR		
		List<ImageSeries> imageSeriesList = new ArrayList<ImageSeries>();
		List<Object[]> objList = q.getResultList();
		if(null != objList && !objList.isEmpty()){		
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
	        	imageSeriesList.add(imgSeries);
	        });     
	        logger.debug("Image series lis size " + imageSeriesList.size());
		}
		return imageSeriesList;
	}
	
	String constructQuery(Map<String, Object> params) {
		boolean geclassPresent = false;
		Map<String, Object> geclassParams = new HashMap<String, Object>();
			if(params.containsKey(GE_CLASS)){
				geclassPresent = true;
				geclassParams.put(GE_CLASS, params.get(GE_CLASS));
				params.remove(GE_CLASS);
			}
		StringBuilder builder = new StringBuilder();
		if (params.size() > 0) {
			if(params.containsKey(ANNOTATIONS)){
				if(!ABSENT.equalsIgnoreCase(params.get(ANNOTATIONS).toString())){
					builder.append(", annotation an where an.image_set = x.id and an.org_id = x.org_id and ");
					builder.append(constructAnnotationWhereClause("type", params.get(ANNOTATIONS).toString()));
					builder.append(" AND ");
				}else{
					builder.append(ANNOTATION_ABSENT_QUERY);
				}
			}else{
				builder.append(" WHERE ");
			}
			params.remove(ANNOTATIONS);
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
		if(geclassPresent){
			builder.append(getGEClassQuery(geclassParams));
		}
		return builder.toString();
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
	        	annotationByDS.setSeriesInstanceUid((String) record[1]);
	        	if (record[2] instanceof Integer){
	        		annotationByDS.setAnnotationId(((Integer) record[2]).longValue());
	        	}    
	        	annotationByDS.setType((String) record[3]);
	        	annotationByDS.setObjectName((String) record[4]);
	        	annotationByDS.setData((Object) record[5]);
	        	annotationByDS.setGeClass((Object) record[6]);
	        	annotationByDS.setGeClass1((Object) record[7]);
	        	annotationByDS.setGeClass2((Object) record[8]);
	        	annotationByDS.setGeClass3((Object) record[9]);
	        	annotationByDS.setGeClass4((Object) record[10]);
	        	annotationByDSList.add(annotationByDS);
	        });     
	        logger.debug("Annotation lis size " + annotationByDSList.size());
		}
		return annotationByDSList;
	}
}
