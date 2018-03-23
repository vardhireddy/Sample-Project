/*
 *  QueryResults.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.dao.impl;

import java.util.List;
import java.util.Map;

/**
 * {@code QueryResults} encapsulates information that is necessary to parse result records produced by a database query.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class QueryResults {

    private List<Object[]> dbResults;

    private Map<String, Integer> resultIndexMap;

    private Map<String, Integer[]> resultIndicesMap;

    public QueryResults(List<Object[]> dbResults, Map<String, Integer> resultIndexMap, Map<String, Integer[]> resultIndicesMap) {
        this.dbResults = dbResults;
        this.resultIndexMap = resultIndexMap;
        this.resultIndicesMap = resultIndicesMap;
    }

    public List<Object[]> getDbResults() {
        return dbResults;
    }

    public Map<String, Integer> getResultIndexMap() {
        return resultIndexMap;
    }

    public Map<String, Integer[]> getResultIndicesMap() {
        return resultIndicesMap;
    }

}
