/*
 * Annotation.java
 * 
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gehc.ai.app.dc.filters.JsonConverter;

/**
 * @author 212071558
 *
 */
@Entity
@JsonInclude(Include.NON_NULL)
@Table(name = "annotation")
@XmlRootElement(name = "annotation")
public class Annotation implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="schema_version")
    private String schemaVersion;
    /**
     * The organization who owns the data. 
     */
    @Column(name="org_id")
    private String orgId;
    /**
     * An identifier for the one who annotated the data. This allows to query for the data annotated by a specific person.
     */
    @Column(name="annotator_id")
    private String annotatorId;
    @Column(name="annotation_tool")
    private String annotationTool;      
    /**
     * Date data was annotated. Should be left to database to provide.
     */
    
   // @JsonFormat(pattern="yyyyMMdd")
    @Column(name="annotation_date", columnDefinition="DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date annotationDate;
    @Column(name="type")
    private String type;
    @Column(name="image_set")
    private String imageSet;  
    /**
     * Flexible JSON object to store annotated items
     */
    @Convert(converter = JsonConverter.class)
    private transient Object item;
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId( Long id ) {
        this.id = id;
    }
    /**
     * @return the schemaVersion
     */
    public String getSchemaVersion() {
        return schemaVersion;
    }
    /**
     * @param schemaVersion the schemaVersion to set
     */
    public void setSchemaVersion( String schemaVersion ) {
        this.schemaVersion = schemaVersion;
    }
    /**
     * @return the orgId
     */
    public String getOrgId() {
        return orgId;
    }
    /**
     * @param orgId the orgId to set
     */
    public void setOrgId( String orgId ) {
        this.orgId = orgId;
    }
    /**
     * @return the annotatorId
     */
    public String getAnnotatorId() {
        return annotatorId;
    }
    /**
     * @param annotatorId the annotatorId to set
     */
    public void setAnnotatorId( String annotatorId ) {
        this.annotatorId = annotatorId;
    }
    /**
     * @return the annotationTool
     */
    public String getAnnotationTool() {
        return annotationTool;
    }
    /**
     * @param annotationTool the annotationTool to set
     */
    public void setAnnotationTool( String annotationTool ) {
        this.annotationTool = annotationTool;
    }
    /**
     * @return the annotationDate
     */
    public Date getAnnotationDate() {
        return new Date(annotationDate.getTime());
    }
    /**
     * @param annotationDate the annotationDate to set
     */
    public void setAnnotationDate( Date annotationDate ) {
        this.annotationDate = new Date(annotationDate.getTime());

    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType( String type ) {
        this.type = type;
    }
    /**
     * @return the imageSet
     */
    public String getImageSet() {
        return imageSet;
    }
    /**
     * @param imageSet the imageSet to set
     */
    public void setImageSet( String imageSet ) {
        this.imageSet = imageSet;
    }
    /**
     * @return the item
     */
    public Object getItem() {
        return item;
    }
    /**
     * @param item the item to set
     */
    public void setItem( Object item ) {
        this.item = item;
    }
}
