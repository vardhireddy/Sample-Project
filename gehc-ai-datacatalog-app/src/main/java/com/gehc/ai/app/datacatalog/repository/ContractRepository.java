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

/**
 * @author dipshah
 *
 */
@RepositoryRestResource(collectionResourceRel = "contract", path = "contract")
public interface ContractRepository extends JpaRepository<Contract, Long> {

	@Override
    <S extends Contract> S save(S entity);

    @Query(value = "select count( id ) from contract where id=:contractId and org_id=:orgId", nativeQuery = true)
    int validateContractIdAndOrgId(@Param("contractId") Long contractId, @Param("orgId") String orgId);
}
