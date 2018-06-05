/*
 * UpdateContractRequest.java
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

import com.gehc.ai.app.datacatalog.entity.Contract;

import java.util.List;

public class UpdateContractRequest {

    private Contract.UploadStatus status;

    private List<String> uri;

    public UpdateContractRequest() {
    }

    public UpdateContractRequest(Contract.UploadStatus status, List<String> uri) {
        this.status = status;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "UpdateContractRequest{" +
                "status='" + status + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

    public Contract.UploadStatus getUploadStatus() {
        return status;
    }

    public List<String> getUri() {
        return uri;
    }
}
