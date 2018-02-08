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

import com.gehc.ai.app.datacatalog.entity.InstitutionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "image_set", path = "image_set")
public interface ImageSeriesRepository extends JpaRepository<ImageSeries, Long> {
	public static final String IMG_WITH_NO_ANNOTATION = "SELECT count(1) as count FROM ImageSeries im where orgId=:orgId "
			+ "and not exists (SELECT imageSet FROM Annotation an where an.imageSet = im.id and an.orgId = im.orgId )";
	
	public static final String IMG_WITH_NO_ANN_COUNT = "select count(im.id) from ImageSeries im LEFT OUTER JOIN im.annotation an where im.orgId=:orgId and an.imageSet IS NULL";
	
    @Override
    <S extends ImageSeries> S save(S entity);
    List<ImageSeries> findByOrgId(@Param("orgId") String orgId);  
    List<ImageSeries> findByPatientDbIdAndOrgId(@Param("patientDbId") Long patientDbId, @Param("orgId") String orgId);
    List<ImageSeries> findById(@Param("id") Long id);
    List<ImageSeries> findByOrgIdInAndModalityIn(List<String> orgId, List<String> modality);
    List<ImageSeries> findByOrgIdIn(List<String> orgId);
    List<ImageSeries> findByOrgIdInAndAnatomyIn(List<String> orgId, List<String> anatomy);
    List<ImageSeries> findByOrgIdInAndSeriesInstanceUid(List<String> orgId, List<String> seriesInstanceUid);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityIn(List<String> orgId, List<String> anatomy, List<String> modality);
    List<ImageSeries> findByIdAndOrgId(@Param("id") Long id, @Param("orgId") String orgId);
    List<ImageSeries> findBySeriesInstanceUidInAndOrgIdIn(List<String> seriesInstanceUid, List<String> orgId);
    List<ImageSeries> findByStudyDbIdAndOrgId(@Param("studyDbId") Long studyDbId, @Param("orgId") String orgId);
    List<ImageSeries> findByIdIn(List<Long> id);
    @Query("SELECT modality as name, count(*) as count FROM ImageSeries where orgId=:orgId group by modality")
    List<Object[]> countModality(@Param("orgId") String orgId);
    @Query("SELECT anatomy as name, count(*) as count FROM ImageSeries where orgId=:orgId group by anatomy")
    List<Object[]> countAnatomy(@Param("orgId") String orgId);
    @Query(IMG_WITH_NO_ANN_COUNT)
    List<Long> countImgWithNoAnn(@Param("orgId") String orgId);
    @Query("SELECT dataFormat as dataFormat, count(*) as count FROM ImageSeries where orgId=:orgId group by dataFormat")
    List<Object[]> countDataFormat(@Param("orgId") String orgId);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityInAndDataFormatIn(List<String> orgId, List<String> anatomy, List<String> modality, List<String> dataFormat);
    List<ImageSeries> findByOrgIdInAndModalityInAndDataFormatIn(List<String> orgId, List<String> modality, List<String> dataFormat);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndDataFormatIn(List<String> orgId, List<String> anatomy, List<String> dataFormat);
    List<ImageSeries> findByOrgIdInAndDataFormatIn(List<String> orgId, List<String> dataFormat);
    @Query("SELECT institution as institution, count(*) as count FROM ImageSeries where orgId=:orgId group by institution")
    List<Object[]> countInstitution(@Param("orgId") String orgId);
    @Query("SELECT equipment as equipment, count(*) as count FROM ImageSeries where orgId=:orgId group by equipment")
    List<Object[]> countEquipment(@Param("orgId") String orgId);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityInAndDataFormatInAndInstitutionIn(List<String> orgId, List<String> anatomy, List<String> modality, List<String> dataFormat, List<String> institution);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityInAndInstitutionIn(List<String> orgId, List<String> anatomy, List<String> modality, List<String> institution);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndDataFormatInAndInstitutionIn(List<String> orgId, List<String> anatomy, List<String> dataFormat, List<String> institution);
    List<ImageSeries> findByOrgIdInAndModalityInAndDataFormatInAndInstitutionIn(List<String> orgId, List<String> modality, List<String> dataFormat, List<String> institution);
    List<ImageSeries> findByOrgIdInAndInstitutionIn(List<String> orgId, List<String> institution);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndInstitutionIn(List<String> orgId, List<String> anatomy, List<String> institution);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityInAndDataFormatInAndInstitutionInAndEquipmentIn(List<String> orgId, List<String> anatomy, List<String> modality, List<String> dataFormat, List<String> institution, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityInAndDataFormatInAndEquipmentIn(List<String> orgId, List<String> anatomy, List<String> modality, List<String> dataFormat, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityInAndInstitutionInAndEquipmentIn(List<String> orgId, List<String> anatomy, List<String> modality, List<String> institution, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndDataFormatInAndInstitutionInAndEquipmentIn(List<String> orgId, List<String> anatomy, List<String> dataFormat, List<String> institution, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndModalityInAndDataFormatInAndInstitutionInAndEquipmentIn(List<String> orgId, List<String> modality, List<String> dataFormat, List<String> institution, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndModalityInAndEquipmentIn(List<String> orgId, List<String> anatomy, List<String> modality, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndModalityInAndEquipmentIn(List<String> orgId, List<String> modality, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndModalityInAndDataFormatInAndEquipmentIn(List<String> orgId, List<String> modality, List<String> dataFormat, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndEquipmentIn(List<String> orgId, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndModalityInAndInstitutionInAndEquipmentIn(List<String> orgId, List<String> modality, List<String> institution, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndModalityInAndInstitutionIn(List<String> orgId, List<String> modality, List<String> institution);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndInstitutionInAndEquipmentIn(List<String> orgId, List<String> anatomy, List<String> institution, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndAnatomyInAndEquipmentIn(List<String> orgId, List<String> anatomy, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndDataFormatInAndInstitutionInAndEquipmentIn(List<String> orgId, List<String> dataFormat, List<String> institution, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndDataFormatInAndInstitutionIn(List<String> orgId, List<String> dataFormat, List<String> institution);
    List<ImageSeries> findByOrgIdInAndDataFormatInAndEquipmentIn(List<String> orgId, List<String> dataFormat, List<String> equipment);
    List<ImageSeries> findByOrgIdInAndInstitutionInAndEquipmentIn(List<String> orgId, List<String> institution, List<String> equipment);
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("update ImageSeries i set i.institution=:institution where i.seriesInstanceUid in :seriesUIds")
    void updateInstitution( @Param("institution") String institution, @Param("seriesUIds") String [] seriesUIds);
	void delete(ImageSeries deleted);
 }
