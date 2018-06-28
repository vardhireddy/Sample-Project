package com.gehc.ai.app.datacatalog.rest.request;

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
public final class UpdateUploadRequest {

        private static final long serialVersionUID = 1L;

        private Long id;

        private String schemaVersion;

        private String orgId;

        private List<String> dataType;

        private Long contractId;

        private String spaceId;

        private List<String> summary;

        private Map<String,String> tags;

        private Map<String,String> status;

        private String uploadBy;

        private Timestamp uploadDate;

        private Timestamp lastModified;
}
