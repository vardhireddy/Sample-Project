package com.gehc.ai.app.datacatalog.service;

import java.util.List;
import java.util.Map;

import com.gehc.ai.app.datacatalog.entity.ImageSeries;

public interface IDataCatalogService {
    
    Map<Object, Object> geClassDataSummary(Map<String, String> params, String orgId, String type);
    
    List<ImageSeries> getImgSeries(Map<String, Object> params, List<ImageSeries> imgSeriesLst);
}
