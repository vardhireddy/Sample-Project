/*
 * IDataCatalogDao.java
 * 
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.dao;

import java.util.List;

import com.gehc.ai.app.dc.entity.DataCollection;
import com.gehc.ai.app.dc.entity.ImageSet;

/**
 * @author 212071558
 *
 */
public interface IDataCatalogDao {
    /**
     * Gets the data catalog.
     * @param orgId TODO
     * @throws exception
     */
    List<ImageSet> getImageSet(String orgId) throws Exception;
    
    String[] getImgSetIdForDC(String id) throws Exception;
    
    List<DataCollection> getDataCollection() throws Exception;
}
