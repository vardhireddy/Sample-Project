/*
 * PatientRepository.java
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.gehc.ai.app.datacatalog.entity.Patient;

/**
 * Created by 200014175 on 10/27/2016.
 */
@RepositoryRestResource(collectionResourceRel = "patient", path = "patient")
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByPatientName(@Param("name") String name);

    List<Patient> findByPatientId(@Param("id") String id);

    List<Patient> findByGender(@Param("gender") String g);

    List<Patient> findByAge(@Param("age") String id);

    List<Patient> findByOrgId(@Param("orgid") String id);

    List<Patient> findByIdIn(List<Long> ids);

    List<Patient> findByIdInAndOrgId(List<Long> ids, String orgId);

    Page<Patient> findAllByOrderByIdDesc(Pageable pageable);
    List<Patient> findById(@Param("id") Long id);
}
