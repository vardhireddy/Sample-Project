/*
 * AnnotationImgSetDataCol.java
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

import java.io.IOException;
import java.util.ArrayList;
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
			 + " SELECT  11 ) AS indices WHERE org_id = :orgId and type = :type and JSON_EXTRACT(item, CONCAT('$.properties.ge_class[', idx, ']')) IS NOT NULL "
			 + " ORDER BY id, idx) AS LABEL_JSON ";
	public static final String GE_CLASS_COUNTS_SUFFIX = " GROUP BY single_class";

	public static final String GE_CLASS_QUERY = "select distinct im.id, im.org_id, p.patient_id, im.modality, im.anatomy, im.instance_count "
			+ " from image_set im "
			+ "inner join annotation an "
			+ "on an.image_set=im.id "
			+ "inner join patient p "
			+ "on p.id = im.patient_dbid ";

	public static final List<Object> GE_CLASS_LIST = new ArrayList<Object>();
	
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
	public Map<Object, Object> geClassDataSummary(Map<String, String> filters, String orgId, String type) {
		logger.info(" In DAO geClassDataSummary, orgId = " + orgId + " type = " + type);
		Map<Object, Object> filterMap = new HashMap<Object, Object>();
		if(null != orgId && !orgId.isEmpty() && null != type && !type.isEmpty()){
			StringBuilder queryBuilder = new StringBuilder();		
			if (!filters.isEmpty()) {
				queryBuilder.append(" inner join image_set on image_set.id = image_set where ");
				for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
					String key = it.next();
					queryBuilder.append(getColumnQueryString(key, filters.get(key)));
					if (it.hasNext())
						queryBuilder.append(" and ");
				}
			}
			String queryString = GE_CLASS_COUNTS_PREFIX + queryBuilder + GE_CLASS_COUNTS_SUFFIX;
			Query q = em.createNativeQuery(queryString);		
			q.setParameter("orgId", orgId);
			q.setParameter("type", type);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ImageSeries> getImgSeries(Map<String, Object> params, List<ImageSeries> imgSeriesLst, List<String> typeLst) {
		ObjectMapper mapper = new ObjectMapper();	
		GEClass [] geClasses = getGEClasses(params);
		//Get image set
		List<Long> imgSeriesIdLst = getImgSeriesIdLst(imgSeriesLst);
		StringBuilder imageSeriesIds = new StringBuilder();
		imageSeriesIds.append(" image_set in (");
		if(null != imgSeriesIdLst && !imgSeriesIdLst.isEmpty()){
			for (Iterator<Long> imgSeriesIdItr = imgSeriesIdLst.iterator(); imgSeriesIdItr.hasNext();) {
				imageSeriesIds.append(imgSeriesIdItr.next() + (imgSeriesIdItr.hasNext() ? "," : ""));
			}
		}
		imageSeriesIds.append(")");
		//build query for annotation types
		StringBuilder annotationTypes = new StringBuilder();
		annotationTypes.append(" type in ('");
			if(null != typeLst && !typeLst.isEmpty()){
				for (Iterator<String> typeLstItr = typeLst.iterator(); typeLstItr.hasNext();) {
					annotationTypes.append(typeLstItr.next() + (typeLstItr.hasNext() ? "','" : ""));
				}
			}
			annotationTypes.append("')");
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
        queryBuilder.append(" and ");
		queryBuilder.append(imageSeriesIds);
        queryBuilder.append(" and ");
		queryBuilder.append(annotationTypes);
        logger.info(" getImgSeries query is " + queryBuilder);
        Query q = em.createNativeQuery(queryBuilder.toString());
        return getImgSeriesLst(q.getResultList());
	}
	
	public GEClass [] getGEClasses(Map<String, Object> params){
		ObjectMapper mapper = new ObjectMapper();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
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
	
	public List<ImageSeries> getImgSeriesLst(List<Object[]> objList){
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
		return result;
	}
}
