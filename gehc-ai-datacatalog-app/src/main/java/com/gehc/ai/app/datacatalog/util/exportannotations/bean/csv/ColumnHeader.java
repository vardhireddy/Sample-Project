/*
 *  ColumnHeader.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.bean.csv;

import java.util.Objects;

/**
 * {@code ColumnHeader} represents a CSV column header.
 */
public class ColumnHeader {

    private final String name;

    private final int priority;

    /**
     * Creates a new {@code ColumnHeader} with the specified name and priority.
     *
     * @param name     (Required) The name of the column header
     * @param priority (Required) A number indicating the priority in which this column should appear in the CSV file.
     *                 A higher priority column header which appear more leftward than a comparitively lower priority column header.
     *                 A <b>lower</b> number indicates a <b>higher</b> priority.
     */
    public ColumnHeader(String name, int priority) {
        this.name = Objects.requireNonNull(name);
        if (Objects.requireNonNull(priority) < 0) {
            throw new IllegalArgumentException("The priority must be an integer greater than or equal to 0");
        }
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

}
