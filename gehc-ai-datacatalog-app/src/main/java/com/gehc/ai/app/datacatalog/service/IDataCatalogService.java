package com.gehc.ai.app.datacatalog.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.gehc.ai.app.datacatalog.entity.ImageSeries;

public interface IDataCatalogService {
    /**
     * @param groupBy
     * @param request
     * @return map of all the filters
     */
    Map<String, Object> dataSummary(String groupby, HttpServletRequest request);
    
    Map<Object, Object> geClassDataSummary(Map<String, String> params);
    
    List<ImageSeries> getImgSeries(Map<String, Object> params, List<ImageSeries> imgSeriesLst);
    
    int getImageSetCount(Map<String, Object> params);
}
