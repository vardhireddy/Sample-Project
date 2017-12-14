/*
 * CosNotification.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.entity;

import static com.gehc.ai.app.common.constants.ValidationConstants.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gehc.ai.app.datacatalog.filters.JsonConverter;

/**
 * Created by 200014175 on 11/2/2016.
 */
@Entity
public class CosNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Column( name = "org_id" )
    @Size(max=255)
    @Pattern(regexp = UUID)
    private String orgId;

     /**
     * Actual message from COS notification, can be parsed
     */
    @Convert(converter = JsonConverter.class)
    private Object message;
    /**
     * @return the message
     */
    public Object getMessage() {
        return message;
    }
    /**
     * @param message the message to set
     */
    public void setMessage( Object message ) {
        this.message = message;
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
   
}
