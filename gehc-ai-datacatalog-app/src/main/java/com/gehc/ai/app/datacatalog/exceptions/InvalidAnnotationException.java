/*
 *  InvalidAnnotationException.java
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
 * An {@code InvalidAnnotationException} indicates that format in which an annotation is represented is not well-formed.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class InvalidAnnotationException extends Exception {

    /**
     * Creates a new {@code InvalidAnnotationException} using the specified message.
     *
     * @param message The message describing the exception.
     */
    public InvalidAnnotationException(String message) {
        super(message);
    }

}
