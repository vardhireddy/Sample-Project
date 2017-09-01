package com.gehc.ai.app.datacatalog.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.gehc.ai.app.datacatalog.dao.IDataCatalogDao;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;

@Configuration
@Component
public class DataCatalogServiceImpl implements IDataCatalogService{

	@Autowired
	private IDataCatalogDao dataCatalogDao;

	@Override
	public Map<Object, Object> geClassDataSummary(Map<String, String> params, String orgId, String type) {
		return dataCatalogDao.geClassDataSummary(params, orgId, type);
	}

	@Override
	public List<ImageSeries> getImgSeries(Map<String, Object> params, List<ImageSeries> imgSeriesLst) {
		return dataCatalogDao.getImgSeries(params, imgSeriesLst);
	}
}
