/*
 * ContractRepository.java
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

import com.gehc.ai.app.datacatalog.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author dipshah, umatabib(212691936)
 *
 */
@RepositoryRestResource(collectionResourceRel = "contract", path = "contract")
public interface ContractRepository extends JpaRepository<Contract, Long> {

    int countByIdAndOrgId(Long contractId, String orgId);

    Contract findByIdAndOrgId(Long contractId, String orgId);

    List<Contract> findAllByOrgIdOrderByActiveDescIdDesc(String orgId);

    @Query(value = "select * from lfdb.contract where id in " +
            "(select distinct contract_id from lfdb.upload where id in " +
            "(select distinct upload_id from lfdb.image_set where id in :imageSetIdList))",nativeQuery = true)
    List<Contract> getContractsByImageSetidList(@Param("imageSetIdList") List<Long> imageSetIdList);
}
