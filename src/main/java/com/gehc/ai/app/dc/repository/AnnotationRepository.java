/*
 * AnnotationRepository.java
 * 
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
 * 
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.dc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.dc.entity.Annotation;

/**
 * @author 212071558
 *
 */
@RepositoryRestResource(collectionResourceRel = "annotation", path = "annotation")
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    List<Annotation> findByTypeIn(List<String> type);
    List<Annotation> findByIdIn(List<Long> ids);
    List<Annotation> findByImageSet(@Param("imageSet") String imageSet);
    List<Annotation> findByImageSetIn(List<String> imageSet);
    List<Annotation> findByImageSetInAndTypeIn(List<String> imageSets, List<String> types);
    List<Annotation> findByImageSetAndOrgId(@Param("imageSet") String imageSet, @Param("orgId") String orgId);
    List<Annotation> findByIdInAndOrgId(List<Long> ids, String orgId);
 //   @Query(value="delete from annotation a where a.id = ?1")
  //  void deleteById(Long id);
    @Override
    <S extends Annotation> S save(S entity);
    <S extends Annotation> S delete(S entity);
}
