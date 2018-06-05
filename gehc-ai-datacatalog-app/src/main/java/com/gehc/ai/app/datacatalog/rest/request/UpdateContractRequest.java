package com.gehc.ai.app.datacatalog.rest.request;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateContractRequest {

    private String status;
    private List<String> uri;

    public UpdateContractRequest() {
    }

    public UpdateContractRequest(String status, List<String> uri) {
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

    public List<String> getUri() {
        return uri;
    }
}
