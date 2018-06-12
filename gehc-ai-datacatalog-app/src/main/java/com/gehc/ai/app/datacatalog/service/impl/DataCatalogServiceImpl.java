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

import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.Contract;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@Component
public class DataCatalogServiceImpl implements IDataCatalogService {

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
	public List<ImageSeries> getImgSeriesByFilters(Map<String, Object> params, boolean randomize, int maxImageSeriesRows) {
		return dataCatalogDao.getImgSeriesByFilters(params, randomize, maxImageSeriesRows);
	}

	@Override
	public List<AnnotationJson> getAnnotationDetailsByImageSetIDs(List<Long> imgSerIdLst) throws InvalidAnnotationException {
		return dataCatalogDao.getAnnotationDetailsByImageSetIDs(imgSerIdLst);
	}

	@Override
	public String getAnnotationDetailsAsCsvByImageSetIDs(List<Long> imgSerIdLst) throws InvalidAnnotationException, CsvConversionException {
		return dataCatalogDao.getAnnotationDetailsAsCsvByImageSetIDs(imgSerIdLst);
	}

	@Override
	public List<Integer> getAnnotationsById(Annotation annotation) {
		return dataCatalogDao.getAnnotationsIds(annotation);
	}

	@Override
	public List<ImageSeries> getImgSeriesWithPatientByIds(List<Long> imgSerIdLst) {
		return dataCatalogDao.getImgSeriesWithPatientByIds(imgSerIdLst);
	}

	@Override
	public Contract saveContract(Contract contract) {
		return dataCatalogDao.saveContract(contract);
	}

	@Override
	public Contract getContract(Long contractId) {

		return dataCatalogDao.getContractDetails(contractId);
	}

	@Override
	public List<Contract> getAllContracts(String orgId) {

		return dataCatalogDao.getAllContractsDetails(orgId);
	}

	@Override
	public List<Long> getImgSeriesIdsByFilters(Map<String, Object> params) {
		return dataCatalogDao.getImgSeriesIdsByFilters(params);
	}
}
