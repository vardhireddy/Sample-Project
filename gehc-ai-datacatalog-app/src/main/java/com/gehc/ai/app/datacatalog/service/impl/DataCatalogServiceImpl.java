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
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@Component
public class DataCatalogServiceImpl implements IDataCatalogService {

	private static Logger logger = LoggerFactory.getLogger(DataCatalogServiceImpl.class);

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
	public List<Long> getImgSeriesIdsByFilters(Map<String, Object> params) {
		return dataCatalogDao.getImgSeriesIdsByFilters(params);
	}

	@Override
	public  Map<String,List<ContractByDataSetId>> getContractsByDatasetId(Long datasetId){
		List<Long> imageSetIdList;

		try{
			imageSetIdList = dataCatalogDao.getImageSetIdListByDataSetId(datasetId);
		}catch (Exception e){
			logger.error("Error retrieving imageSet id list for given data set id : {}", e.getMessage());
			throw e;
		}

		List<Contract> contractList;

        Map<String,List<ContractByDataSetId>> mapOfContracts = new HashMap<>();
        String active = "active";
        String inactive = "inactive";
        mapOfContracts.put(inactive, new ArrayList<>());
        mapOfContracts.put(active, new ArrayList<>());

		if (imageSetIdList == null) return mapOfContracts;
		try {
			contractList =  dataCatalogDao.getContractsByImageSetidList(imageSetIdList);
		}catch (Exception e1){
			logger.error("Error retrieving contracts associated with the dataset : {}", e1.getMessage());
			throw e1;
		}

		if(contractList.isEmpty()){
		    return mapOfContracts;
        }

        for (Contract contract : contractList) {

            ContractByDataSetId contractByDataSetId = new ContractByDataSetId();
            contractByDataSetId.setId(contract.getId());
            contractByDataSetId.setActive(contract.getActive());
            contractByDataSetId.setAgreementBeginDate(contract.getAgreementBeginDate());
            contractByDataSetId.setAgreementName(contract.getAgreementName());
            contractByDataSetId.setDataLocationAllowed(contract.getDataLocationAllowed());
            contractByDataSetId.setDataOriginCountriesAndStates(contract.getDataOriginCountriesStates());
            contractByDataSetId.setDeidStatus(contract.getDeidStatus());
            contractByDataSetId.setPrimaryContactEmail(contract.getPrimaryContactEmail());
            contractByDataSetId.setUploadBy(contract.getUploadBy());
            contractByDataSetId.setUploadDate(contract.getUploadDate());
            contractByDataSetId.setDataUsagePeriod(contract.getDataUsagePeriod());
            contractByDataSetId.setUseCases(contract.getUseCases());

            boolean isExpired;
            //calculate if contract expired
            isExpired = isContractExpired(contract.getAgreementBeginDate(),contract.getDataUsagePeriod());
            contractByDataSetId.setHasContractExpired(isExpired);

            if(contract.getActive().equalsIgnoreCase("false"))
            {
                mapOfContracts.get(inactive).add(contractByDataSetId);
            }else
            {
                mapOfContracts.get(active).add(contractByDataSetId);
            }
        }

        return mapOfContracts;

	}

	private boolean isContractExpired(String agreementBeginDate, String dataUsagePeriod){
        //set the current system date
        Date currentDate = new Date();

        // convert date to calendar
        Calendar c = Calendar.getInstance();

        //specify the input string date format
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date agreementDate = new Date();

        try {
            //convert String agreementBeginDate to util.Date
            agreementDate = formatter.parse(agreementBeginDate);
        }catch (Exception e){}

        c.setTime(agreementDate);

        int yearsToAdd = Integer.valueOf(dataUsagePeriod)/12;
        // manipulate date
        c.add(Calendar.YEAR, yearsToAdd);

        // convert calendar to date
        Date expirationDate = c.getTime();

        if(currentDate.equals(expirationDate)
                || currentDate.after(expirationDate))
        {
            return true;
        }
        else {
            return false;
        }
    }
}
