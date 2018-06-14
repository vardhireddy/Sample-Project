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
 * A {@code InvalidContractException} indicates that format in which an annotation is represented is not well-formed.
 *
 * @author umatabib1@ge.com (212691936)
 */
public class InvalidContractException extends Exception {

    /**
     * Creates a new {@code InvalidAnnotationException} using the specified message.
     *
     * @param message The message describing the exception.
     */
    public InvalidContractException(String message) {
        super(message);
    }

}
