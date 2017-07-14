package com.gehc.ai.app.datacatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.gehc.ai.app.datacatalog.entity.DataSet;

@RepositoryRestResource(collectionResourceRel = "data_set", path = "data-set")
public interface DataSetRepository extends JpaRepository<DataSet, Long> {
	//List<DataSet> findByIdInAndOrgId(List<Long> ids, String orgId);
	//List<DataSet> findByIdAndOrgId(Long id, String orgId);
	 List<DataSet> findByIdAndOrgId(@Param("id") Long id, @Param("orgId") String orgId);
	 List<DataSet> findByOrgIdOrderByCreatedDateDesc(@Param("orgId") String orgId);
	 List<DataSet> findByIdAndType(@Param("id") Long id, @Param("type") String type);
	 List<DataSet> findByTypeAndOrgIdOrderByCreatedDateDesc(@Param("type") String type, @Param("orgId") String orgId);
	 List<DataSet> findByIdAndTypeAndOrgId(@Param("id") Long id, @Param("type") String type, @Param("orgId") String orgId);
	 List<DataSet> findById(@Param("id") Long id);
	 @Override
	 <S extends DataSet> S save(S entity);
}
