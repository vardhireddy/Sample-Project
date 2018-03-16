/*
 * DataCatalogServiceImpl.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.AnnotationDetails;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;

@Configuration
@Component
public class DataCatalogServiceImpl implements IDataCatalogService{

	@Autowired
	private IDataCatalogDao dataCatalogDao;

	@Override
	public Map<Object, Object> geClassDataSummary(Map<String, String> params, String orgId) {
		return dataCatalogDao.geClassDataSummary(params, orgId);
	}
	
    /**
     * @param params
     * @return
     * @throws Exception 
     */
	@Override
	public List<ImageSeries> getImgSeriesByFilters(Map<String, Object> params) {
		return dataCatalogDao.getImgSeriesByFilters(params);
	}

	@Override
	public List<AnnotationDetails> getAnnotationsByDSId(List<Long> imgSerIdLst) {
		return dataCatalogDao.getAnnotationsByDSId(imgSerIdLst);
	}

	@Override
	public List<Integer> getAnnotationsById(Annotation annotation) {
		return dataCatalogDao.getAnnotationsIds(annotation);
	}

	@Override
	public List<ImageSeries> getImgSeriesWithPatientByIds(List<Long> imgSerIdLst) {
		return dataCatalogDao.getImgSeriesWithPatientByIds(imgSerIdLst);
	}
}
