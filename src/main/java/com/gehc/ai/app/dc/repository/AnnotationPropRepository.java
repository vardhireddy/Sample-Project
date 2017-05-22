package com.gehc.ai.app.dc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.dc.entity.AnnotationProperties;

/**
 * @author 212071558
 *
 */
@RepositoryRestResource(collectionResourceRel = "annotation_properties", path = "properties")
public interface AnnotationPropRepository extends JpaRepository<AnnotationProperties, Long> {
    @Override
    <S extends AnnotationProperties> S save(S entity);
    List<AnnotationProperties> findByOrgId(@Param("orgId") String orgId);
}
