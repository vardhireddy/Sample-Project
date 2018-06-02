package com.gehc.ai.app.datacatalog.rest.request;

import javax.validation.constraints.NotNull;

public class UpdateContractRequest {

    private String status;
    private Object uri;

    public UpdateContractRequest() {
    }

    public UpdateContractRequest(String status, Object uri) {
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

    public String getStatus() {
        return status;
    }

    public Object getUri() {
        return uri;
    }
}
