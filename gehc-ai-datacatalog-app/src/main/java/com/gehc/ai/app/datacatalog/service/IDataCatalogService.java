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
     * @return
     */
    List<ImageSeries> getImgSeries(Map<String, Object> params, List<ImageSeries> imgSeriesLst);
}
