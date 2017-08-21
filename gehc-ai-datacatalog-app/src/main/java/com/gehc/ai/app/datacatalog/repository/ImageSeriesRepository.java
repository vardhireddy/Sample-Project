/*
 * ImageSeriesRepository.java
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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.datacatalog.entity.ImageSeries;

@RepositoryRestResource(collectionResourceRel = "image_set", path = "image_set")
public interface ImageSeriesRepository extends JpaRepository<ImageSeries, Long> {
	public static final String IMG_WITH_NO_ANNOTATION = "SELECT count(1) as count FROM ImageSeries im where orgId=:orgId "
			+ "and not exists (SELECT imageSet FROM Annotation an where an.imageSet = im.id and an.orgId = im.orgId )";
	
	public static final String IMG_WITH_NO_ANN_COUNT = "select count(im.id) from ImageSeries im LEFT OUTER JOIN im.annotation an where im.orgId=:orgId and an.imageSet IS NULL";
	
    @Override
    <S extends ImageSeries> S save(S entity);
    List<ImageSeries> findByOrgId(@Param("orgId") String orgId);  
    List<ImageSeries> findByPatientDbId(@Param("patientDbId") Long patientDbId);
    List<ImageSeries> findById(@Param("id") Long id);
    List<ImageSeries> findByOrgIdInAndModalityIn(List<String> orgId, List<String> modality);
    List<ImageSeries> findByOrgIdIn(List<String> orgId);
    List<ImageSeries> findByOrgIdInAndAnatomyIn(List<String> orgId, List<String> anatomy);
    List<ImageSeries> findByOrgIdInAndSeriesInstanceUid(List<String> orgId, List<String> seriesInstanceUid);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityIn(List<String> orgId, List<String> anatomy, List<String> modality);
    List<ImageSeries> findByIdAndOrgId(@Param("id") Long id, @Param("orgId") String orgId);
    List<ImageSeries> findBySeriesInstanceUidIn(List<String> seriesInstanceUid);
    List<ImageSeries> findByStudyDbIdAndOrgId(@Param("studyDbId") Long studyDbId, @Param("orgId") String orgId);
    List<ImageSeries> findByIdIn(List<Long> id);
    @Query("SELECT modality as name, count(*) as count FROM ImageSeries where orgId=:orgId group by modality")
    List<Object[]> countModality(@Param("orgId") String orgId);
    @Query("SELECT anatomy as name, count(*) as count FROM ImageSeries where orgId=:orgId group by anatomy")
    List<Object[]> countAnatomy(@Param("orgId") String orgId);
    @Query(IMG_WITH_NO_ANN_COUNT)
    List<Long> countImgWithNoAnn(@Param("orgId") String orgId);
 }
