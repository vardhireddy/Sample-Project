/*
 * UpdateUploadRequest.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.rest.request;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.AccessLevel;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
@Data
@Getter
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@Immutable
public final class UpdateUploadRequest {

        private static final long serialVersionUID = 1L;

        private Long id;

        private String schemaVersion;

        private String orgId;

        private List<String> dataType;

        private Long contractId;

        private String spaceId;

        private List<String> summary;

        private Map<String,Object> tags;

        private Map<String,Integer> status;

        private String uploadBy;

        private Timestamp uploadDate;

        private Timestamp lastModified;

}
