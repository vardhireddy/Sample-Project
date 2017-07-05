/*
 * COSNotificationRepository.java
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

import com.gehc.ai.app.datacatalog.entity.CosNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by 200014175 on 10/27/2016.
 */
@RepositoryRestResource(collectionResourceRel = "cos_notification", path = "cos-notification")
public interface COSNotificationRepository extends JpaRepository<CosNotification, Long> {
}
