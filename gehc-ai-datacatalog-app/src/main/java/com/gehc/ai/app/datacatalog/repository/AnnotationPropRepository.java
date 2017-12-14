/*
 * AnnotationPropRepository.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;

/**
 * @author 212071558
 *
 */
@RepositoryRestResource(collectionResourceRel = "annotation_properties", path = "annotation-properties")
public interface AnnotationPropRepository extends JpaRepository<AnnotationProperties, Long> {
    @Override
    <S extends AnnotationProperties> S save(S entity);
    List<AnnotationProperties> findByOrgId(@Param("orgId") String orgId);    
}
