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
package com.gehc.ai.app.dc.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.gehc.ai.app.dc.dao.IDataCatalogDao;
import com.gehc.ai.app.dc.entity.AnnotationSet;
import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;
import com.gehc.ai.app.dc.entity.TargetData;
import com.gehc.ai.app.dc.service.IDataCatalogService;

/**
 * @author 212071558
 *
 */
@Configuration
@Component
public class DataCatalogServiceImpl implements IDataCatalogService {
    @Autowired
    private IDataCatalogDao dataCatalogDao;
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.service.IDataCatalogService#getDataCatalog()
     */
    @Override
    public List<ImageSet> getImgSet(Map<String, String> params) throws Exception {
        List<ImageSet> imageSet = null;
        try {
            imageSet = dataCatalogDao.getImgSet(params);
        } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving data collection ", e );
        }
        return imageSet;
    }

    /* (non-Javadoc)
         * @see com.gehc.ai.app.dc.service.IDataCatalogService#getImageSetIdForDC(java.lang.String)
         */
    @Override
    public String[] getImgSetIdForDC( String id ) throws Exception {
        try {
            return dataCatalogDao.getImgSetIdForDC( id );
         } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving data collection ", e );
        }
    }
    /* (non-Javadoc)
     * @see com.gehc.ai.app.dc.service.IDataCatalogService#getDataCollection()
     */
    @Override
    public List<DataCollection> getDataCollection(String id , String type ) throws Exception {
        List<DataCollection> dataCollection = null;
        try {
            dataCollection = dataCatalogDao.getDataCollection(id, type);
         } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving data collection ", e );
        }
        return dataCollection;
    }
	@Override
	public List<ImageSet> getImgSetByDataCollId(String dataCollectionId) throws Exception {
		List<ImageSet> imageSet = null;
        try {
               imageSet = dataCatalogDao.getImgSetByDataCollId(dataCollectionId);
         } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving data collection ", e );
        }
        return imageSet;
	}
	@Override
	public String createDataCollection(DataCollection dataCollection)
			throws Exception {
        String dataCollectionId;
        try {
            dataCollectionId = dataCatalogDao.createDataCollection(dataCollection);
         } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving data collection ", e );
        }
		return dataCollectionId;
	}
    @Override
    public String insertImageSet(ImageSet imageSet) throws Exception {
        return dataCatalogDao.insertImageSet(imageSet);
    }
    
	@Override
	public int insertAnnotationSet(AnnotationSet annotationSetJson) throws Exception {
		return dataCatalogDao.insertAnnotationSet(annotationSetJson);

	}
	@Override
	public List getAnnotationSet(String imageSets, String fields, Map<String, String> queryMap) throws Exception {
		List l = null;
        try {
               l = dataCatalogDao.getAnnotationSet(imageSets, fields, queryMap);
         } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving annotation set ", e );
        }
        return l;	
    }

	@Override
	public List<TargetData> getExperimentTargetData(String dataCollectionIds) throws Exception {
		List l = null;
        try {
               l = dataCatalogDao.getExperimentTargetData(dataCollectionIds);
         } catch ( Exception e ) {
            throw new Exception( "Exception occurred while retreiving target data ", e );
        }
        return l;
	}

	@Override
	public List<ImageSet> getImageSetByStudyId(String studyId) {
        return dataCatalogDao.getImageSetByStudyId(studyId);
	}
}
