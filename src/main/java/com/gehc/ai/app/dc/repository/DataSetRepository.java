package com.gehc.ai.app.dc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.dc.entity.DataSet;

@RepositoryRestResource(collectionResourceRel = "data_set", path = "data-set")
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
	//List<DataSet> findByIdInAndOrgId(List<Long> ids, String orgId);
	//List<DataSet> findByIdAndOrgId(Long id, String orgId);
	 List<DataSet> findByIdAndOrgId(@Param("id") Long id, @Param("orgId") String orgId);
	 List<DataSet> findByIdAndType(@Param("id") Long id, @Param("type") String type);
	 List<DataSet> findByTypeAndOrgId(@Param("type") String type, @Param("orgId") String orgId);
	 List<DataSet> findByIdAndTypeAndOrgId(@Param("id") Long id, @Param("type") String type, @Param("orgId") String orgId);
	 @Override
	 <S extends DataSet> S save(S entity);
}
