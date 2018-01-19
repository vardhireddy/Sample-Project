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
	public static final String ENTITY_NAME="^$|^[^- ][a-zA-Z0-9,./_ -]+$";
    public static final String DESCRIPTION="^$|^[^- ][a-zA-Z0-9,.@%():{}[]~!#?/_ -]+$";
    public static final String DIGIT="^$|^[0-9]+$";
    public static final String UUID= "^$|[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    public static final String DATA_SET_TYPE = "Experiment|Annotation|Inference";
}

