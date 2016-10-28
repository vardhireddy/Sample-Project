package com.gehc.ai.app.dc.repository;

import com.gehc.ai.app.dc.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by 200014175 on 10/27/2016.
 */
@RepositoryRestResource(collectionResourceRel = "study", path = "study")
public interface StudyRepository extends JpaRepository<Study, Long> {
}
