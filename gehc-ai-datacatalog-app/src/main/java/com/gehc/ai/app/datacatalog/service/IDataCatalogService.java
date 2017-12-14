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
import java.util.Map;

import com.gehc.ai.app.datacatalog.entity.ImageSeries;

public interface IDataCatalogService {
    
    /**
     * @param params
     * @param orgId
     * @param type
     * @return
     */
    Map<Object, Object> geClassDataSummary(Map<String, String> params, String orgId, String type);
    
    /**
     * @param params
     * @param imgSeriesLst
     * @param typelst TODO
     * @return
     */
    List<ImageSeries> getImgSeries(Map<String, Object> params, List<ImageSeries> imgSeriesLst, List<String> typelst);
}
