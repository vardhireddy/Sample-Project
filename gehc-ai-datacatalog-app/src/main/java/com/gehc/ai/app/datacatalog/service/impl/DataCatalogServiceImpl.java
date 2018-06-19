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
import com.gehc.ai.app.datacatalog.entity.Upload;
import com.gehc.ai.app.datacatalog.exceptions.CsvConversionException;
import com.gehc.ai.app.datacatalog.exceptions.DataCatalogException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidAnnotationException;
import com.gehc.ai.app.datacatalog.exceptions.InvalidContractException;
import com.gehc.ai.app.datacatalog.filters.RequestValidator;
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
	public List<Contract> getAllContracts(String orgId) throws InvalidContractException{

		return dataCatalogDao.getAllContractsDetails(orgId);
	}

	@Override
	public List<Long> getImgSeriesIdsByFilters(Map<String, Object> params) {
		return dataCatalogDao.getImgSeriesIdsByFilters(params);
	}

	@Override
	public  Map<String,List<ContractByDataSetId>> getContractsByDataCollectionId(Long dataCollectionId){
		List<Long> imageSetIdList;

		try{
			imageSetIdList = dataCatalogDao.getImageSetIdsByDataCollectionId(dataCollectionId);
		}catch (Exception e){
			logger.error("Error retrieving imageSet id list for given data set id : {}", e.getMessage());
			throw e;
		}

		List<Contract> contractList;

        Map<String,List<ContractByDataSetId>> mapOfContracts = new HashMap<>();
        final String active = "active";
        final String inactive = "inactive";
        mapOfContracts.put(inactive, new ArrayList<>());
        mapOfContracts.put(active, new ArrayList<>());

		if (imageSetIdList == null || imageSetIdList.isEmpty())
		{
			return mapOfContracts;
		}
		try {
			contractList =  dataCatalogDao.getContractsByImageSetIds(imageSetIdList);
		}catch (Exception e1){
			logger.error("Error retrieving contracts associated with the dataset : {}", e1.getMessage());
			throw e1;
		}

		if(contractList.isEmpty()){
		    return mapOfContracts;
        }

        Map<String,List<ContractByDataSetId>> result = ContractByDataSetId.fromDataSetEntities(contractList);
		mapOfContracts.put(active,result.get("true"));
		mapOfContracts.put(inactive,result.get("false"));

        return mapOfContracts;

	}

	@Override
    public Upload createUpload(Upload uploadRequest) throws DataCatalogException{
            //verify if all the fields have data
            validateUploadRequestHelper(uploadRequest);

            //verify if contract is valid and active
            validateContractForUploadData(uploadRequest.getContractId());

            uploadRequest.setSchemaVersion("v1");

            return saveUpload(uploadRequest);
    }

	@Override
	public Upload saveUpload(Upload uploadEntity){
		return dataCatalogDao.saveUpload(uploadEntity);
	}

    /**
     * Method to verify if the upload request is valid
     * @param uploadData - the upload request data
     *
     * if request is invalid -> throws DataCatalog Exception specifying the error message and HTTP status code
     */
	private void validateUploadRequestHelper(Upload uploadData) throws DataCatalogException{

		if ((uploadData.getOrgId() == null ||uploadData.getOrgId().isEmpty())
		|| (uploadData.getDataType() == null || uploadData.getDataType().isEmpty())
		|| (uploadData.getContractId() == null || uploadData.getContractId() < 1)
		|| (uploadData.getSpaceId() == null || uploadData.getSpaceId().isEmpty())
		|| (uploadData.getUploadBy() == null || uploadData.getUploadBy().isEmpty())
		|| (uploadData.getTags() == null || uploadData.getTags().isEmpty()))
		{
			throw new DataCatalogException("Missing one/more required fields data.",HttpStatus.BAD_REQUEST);
		}
	}

	/**
     * Verifies if the contract ID given for the upload is valid
     * @param contractId - contract unique id
     * @throws DataCatalogException - if the contract is invalid/expired or does not exist
     */
	private void validateContractForUploadData(Long contractId) throws DataCatalogException
	{
		Contract contract;
			try {
				contract = dataCatalogDao.getContractDetails(contractId);
			}catch (Exception e)
			{
				logger.error("Exception retrieving Contract : {}",e.getMessage());
				throw e;
			}

			if (contract == null)
            {
                    throw new DataCatalogException("No contract exists with provided contract ID.",HttpStatus.BAD_REQUEST);
            }

			boolean isContractExpired = ContractByDataSetId.isContractExpired(contract.getAgreementBeginDate(),contract.getDataUsagePeriod());
			if(contract.getActive().equalsIgnoreCase("false") || isContractExpired)
			{
				throw new DataCatalogException("Invalid/Expired contract ID provided.",HttpStatus.BAD_REQUEST);
			}
	}

    @Override
    public  List<Upload> getAllUploads( HttpServletRequest httpServletRequest ) throws DataCatalogException{

		String orgId = RequestValidator.getOrgIdFromAuth( httpServletRequest);

		if (orgId.isEmpty())
		{
			return new ArrayList<>();
		}

		List<Upload> uploadList = new ArrayList<>(  );
		try {
			uploadList = dataCatalogDao.getAllUploads(orgId);
		}catch ( Exception e )
		{
			e.printStackTrace();
			throw e;
		}
		return uploadList;
    }
}
