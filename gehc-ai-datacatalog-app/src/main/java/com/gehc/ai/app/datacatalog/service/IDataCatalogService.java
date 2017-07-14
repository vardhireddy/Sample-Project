/*
 * IDataCatalogService.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.service;

import java.util.List;

import com.gehc.ai.app.datacatalog.entity.AnnotationImgSetDataCol;

/**
 * @author 212071558
 *
 */
/**
 * @author 212071558
 *
 */
public interface IDataCatalogService {     
    /**
   	 *
   	 * @param dataCollectionId
   	 * @param annotationType mask, point, contour etc to return that particular type of annotation data
   	 * @return annotation data associated with the data collection
   	 * @throws Exception
   	 */
   	List<AnnotationImgSetDataCol> getAnnotationByDataColId(String dataCollectionId, String annotationType) throws Exception;
}
