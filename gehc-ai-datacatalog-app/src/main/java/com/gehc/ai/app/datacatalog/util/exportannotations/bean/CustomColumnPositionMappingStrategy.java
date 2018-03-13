/*
 *  CustomColumnPositionMappingStrategy.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import com.opencsv.bean.ColumnPositionMappingStrategy;

/**
 * {@code CustomColumnPositionMappingStrategy} enables custom ordering of column headers.
 *
 * @param T The type of the CSV bean
 */
public class CustomColumnPositionMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {

    /**
     * Defines the names and orderings of column headers when writing CSV output.
     */
    private String[] headers;

    /**
     * Creates a new {@code CustomColumnPositionMappingStrategy} using the specified column headers.
     *
     * @param headers The array of column headers to use.
     * @return a new {@code CustomColumnPositionMappingStrategy}
     */
    public CustomColumnPositionMappingStrategy(String[] headers){
        super();
        this.headers = headers;
    }

    @Override
    public String[] generateHeader() {
        return this.headers;
    }
}
