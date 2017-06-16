package com.gehc.ai.app.dc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.dc.entity.ImageSeries;

@RepositoryRestResource(collectionResourceRel = "image_series", path = "image-series")
public interface ImageSeriesRepository extends JpaRepository<ImageSeries, Long> {
    @Override
    <S extends ImageSeries> S save(S entity);
    List<ImageSeries> findByOrgId(@Param("orgId") String orgId);  
    List<ImageSeries> findByPatientDbId(@Param("patientDbId") Long patientDbId);
    List<ImageSeries> findById(@Param("id") Long id);
}
