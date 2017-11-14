/*
 * ValidationConstants.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.common.constants;

/**
 * Created by sowjanyanaidu on 11/9/17.
 */
public abstract class ValidationConstants {

        public static final String ELEMENT_NAME="^[a-zA-Z0-9][a-zA-Z0-9\\s'&.,-]{2,255}$";
        public static final String DESCRIPTION="^[a-zA-Z0-9-,.\\s]+$";
        public static final String DIGIT="^[0-9]+$";
}

