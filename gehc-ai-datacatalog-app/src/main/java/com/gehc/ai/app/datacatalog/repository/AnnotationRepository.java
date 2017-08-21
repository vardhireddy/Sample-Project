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
package com.gehc.ai.app.datacatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;

/**
 * @author 212071558
 *
 */
@RepositoryRestResource(collectionResourceRel = "annotation", path = "annotation")
public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
	List<Annotation> findByTypeIn(List<String> type);
    List<Annotation> findByIdIn(List<Long> ids);
    List<Annotation> findByImageSetId(@Param("imageSetId") Long imageSet);
   // List<Annotation> findByImageSet(ImageSeries imageSet);
    //Following is used in filter api which is going to  change
   // List<Annotation> findByImageSetIn(List<String> imageSet);
    List<Annotation> findByImageSetIdIn(List<Long> imageSet);
  //  List<Annotation> findByImageSetInAndTypeIn(List<String> imageSets, List<String> types);
    List<Annotation> findByImageSetIdInAndTypeIn(List<Long> imageSets, List<String> types);
    List<Annotation> findByImageSetInAndTypeIn(List<ImageSeries> imageSets, List<String> types);
    //Below 4 are not in use
   // List<Annotation> findByImageSetAndOrgId(@Param("imageSet") String imageSet, @Param("orgId") String orgId);
    List<Annotation> findByIdInAndOrgId(List<Long> ids, String orgId);
    List<String> findDistinctImageSetByImageSetInAndTypeIn(List<String> imageSets, List<String> types);
    List<String> findDistinctImageSetByImageSetIn(List<String> imageSets);
    //@Query(value="delete from Annotation a where a.id = :id")
    void deleteById(@Param("id") Long id);
    @Override
    <S extends Annotation> S save(S entity);
  //  <S extends Annotation> S delete(S entity);
    @Query("SELECT type as name, count(distinct imageSet) as count FROM Annotation where orgId=:orgId group by type")
    List<Object[]> countAnnotationType(@Param("orgId") String orgId);
    void delete(Annotation deleted);
}
