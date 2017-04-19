/*
 * StudyRepository.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
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
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.dc.entity.Study;

/**
 * Created by 200014175 on 10/27/2016.
 */
@RepositoryRestResource(collectionResourceRel = "study", path = "study")
public interface StudyRepository extends JpaRepository<Study, Long> {
	List<Study> findByPatientDbId(@Param("patient_dbid") Long id);
	List<Study> findByPatientDbIdAndOrgId(@Param("patient_dbid") Long id, @Param("orgId") String orgId);
	List<Study> findByIdIn(List<Long> ids);
	List<Study> findByIdInAndOrgId(List<Long> ids, String orgId);
	List<Study> findByOrgId(@Param("orgid") String id);
}
