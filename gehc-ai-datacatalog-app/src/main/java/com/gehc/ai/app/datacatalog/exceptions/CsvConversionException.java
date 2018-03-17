/*
 *  CsvConversionException.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.exceptions;

/**
 * A {@code CsvConversionException} indicates that source data could not be converted into a CSV format.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class CsvConversionException extends Exception {

    /**
     * Creates a new {@code CsvConversionException} using the specified message.
     *
     * @param message The message describing the exception.
     */
    public CsvConversionException(String message) {
        super(message);
    }

}
