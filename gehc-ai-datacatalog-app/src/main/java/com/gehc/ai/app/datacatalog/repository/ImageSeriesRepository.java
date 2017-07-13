package com.gehc.ai.app.datacatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.datacatalog.entity.ImageSeries;

@RepositoryRestResource(collectionResourceRel = "image_set", path = "image_set")
public interface ImageSeriesRepository extends JpaRepository<ImageSeries, Long> {
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
 }
