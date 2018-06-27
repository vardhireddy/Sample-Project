package com.gehc.ai.app.datacatalog.rest.request;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
@Getter
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

        @Override public String toString() {
                return "UpdateUploadRequest{" + "id=" + id + ", schemaVersion='" + schemaVersion + '\'' + ", orgId='" + orgId + '\'' + ", dataType=" + dataType + ", contractId=" + contractId + ", spaceId='" + spaceId + '\'' + ", summary=" + summary + ", tags=" + tags + ", status=" + status + ", uploadBy='" + uploadBy + '\'' + ", uploadDate=" + uploadDate + ", lastModified=" + lastModified + '}';
        }

        public UpdateUploadRequest() {
        }

        public UpdateUploadRequest(Long id, String schemaVersion, String orgId, List<String> dataType,
                                    Long contractId, String spaceId, List<String> summary,
                                    Map<String, String> tags, Map<String, String> status, String uploadBy,
                                    Timestamp uploadDate, Timestamp lastModified ) {
                this.id = id;
                this.schemaVersion = schemaVersion;
                this.orgId = orgId;
                this.dataType = dataType;
                this.contractId = contractId;
                this.spaceId = spaceId;
                this.summary = summary;
                this.tags = tags;
                this.status = status;
                this.uploadBy = uploadBy;
                this.uploadDate = uploadDate;
                this.lastModified = lastModified;
        }

        @Override public boolean equals( Object o ) {
                if ( this == o )
                        return true;
                if ( o == null || getClass() != o.getClass() )
                        return false;

                UpdateUploadRequest that = (UpdateUploadRequest)o;

                if ( !id.equals( that.id ) )
                        return false;
                if ( !schemaVersion.equals( that.schemaVersion ) )
                        return false;
                if ( !orgId.equals( that.orgId ) )
                        return false;
                if ( !dataType.equals( that.dataType ) )
                        return false;
                if ( !contractId.equals( that.contractId ) )
                        return false;
                if ( !spaceId.equals( that.spaceId ) )
                        return false;
                if ( !summary.equals( that.summary ) )
                        return false;
                if ( !tags.equals( that.tags ) )
                        return false;
                if ( !status.equals( that.status ) )
                        return false;
                if ( !uploadBy.equals( that.uploadBy ) )
                        return false;
                if ( !uploadDate.equals( that.uploadDate ) )
                        return false;
                return lastModified.equals( that.lastModified );
        }

        @Override public int hashCode() {
                int result = id.hashCode();
                result = 31 * result + schemaVersion.hashCode();
                result = 31 * result + orgId.hashCode();
                result = 31 * result + dataType.hashCode();
                result = 31 * result + contractId.hashCode();
                result = 31 * result + spaceId.hashCode();
                result = 31 * result + summary.hashCode();
                result = 31 * result + tags.hashCode();
                result = 31 * result + status.hashCode();
                result = 31 * result + uploadBy.hashCode();
                result = 31 * result + uploadDate.hashCode();
                result = 31 * result + lastModified.hashCode();
                return result;
        }
}
