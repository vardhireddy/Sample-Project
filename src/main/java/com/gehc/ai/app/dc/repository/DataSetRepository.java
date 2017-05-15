package com.gehc.ai.app.dc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gehc.ai.app.dc.entity.DataSet;

public interface DataSetRepository extends JpaRepository<DataSet, Long> {
	//List<DataSet> findByIdInAndOrgId(List<Long> ids, String orgId);
	List<DataSet> findByIdAndOrgId(Long id, String orgId);
}
