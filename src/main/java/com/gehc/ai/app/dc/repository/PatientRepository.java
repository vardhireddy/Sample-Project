package com.gehc.ai.app.dc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.gehc.ai.app.dc.entity.Patient;

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
	
	Page<Patient> findAllByOrderByIdDesc( Pageable pageable);

}
