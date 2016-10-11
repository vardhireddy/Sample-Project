/*
 * ResponseGenerator.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.common.responsegenerator;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The Class ResponseGenerator.
 */
@Component
public class ResponseGenerator {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger( ResponseGenerator.class );

    /**
     * Response on create.
     *
     * @param identifier the identifier
     * @return the response
     */
    public static Response responseOnCreate(String identifier) {
        return Response.status( Status.CREATED ).entity( "{\"id\":\"" + identifier + "\"}" ).build();
    }

    /**
     * Response on update.
     *
     * @return the response
     */
    public Response responseOnUpdate() {
        return Response.status( Status.OK ).build();
    }

}
