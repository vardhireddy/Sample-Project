package com.gehc.ai.app.datacatalog.rest.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public final class UpdateUploadRequest {

        private static final long serialVersionUID = 1L;

        @NotNull
        private Long id;

        @Size (min=2, max=50)
        private String schemaVersion;


        @Size(min=1, max=255)
        private String orgId;

        @Size(min=1, max=255)
        private List<String> dataType;

        @NotNull
        private Long contractId;

        @Size(min=1, max=255)
        private String spaceId;

        @Size(min=1, max=255)
        private List<String> summary;

        @Size(min=1, max=255)
        private Map<String,String> tags;

        @Size(min=1, max=255)
        private Map<String,String> status;

        @Size(min=1, max=255)
        private String uploadBy;

        @NotNull
        private Timestamp uploadDate;

        @NotNull
        private Timestamp lastModified;

        @Override public String toString() {
                return "UpdateUploadRequest{" + "id=" + id + ", schemaVersion='" + schemaVersion + '\'' + ", orgId='" + orgId + '\'' + ", dataType=" + dataType + ", contractId=" + contractId + ", spaceId='" + spaceId + '\'' + ", summary=" + summary + ", tags=" + tags + ", status=" + status + ", uploadBy='" + uploadBy + '\'' + ", uploadDate=" + uploadDate + ", lastModified=" + lastModified + '}';
        }

        public UpdateUploadRequest() {
        }

        public UpdateUploadRequest( @NotNull Long id, @Size ( min = 2, max = 50 ) String schemaVersion, @Size ( min = 1, max = 255 ) String orgId, @Size ( min = 1, max = 255 ) List<String> dataType,
                                    @NotNull Long contractId, @Size ( min = 1, max = 255 ) String spaceId, @Size ( min = 1, max = 255 ) List<String> summary,
                                    @Size ( min = 1, max = 255 ) Map<String, String> tags, @Size ( min = 1, max = 255 ) Map<String, String> status, @Size ( min = 1, max = 255 ) String uploadBy,
                                    @NotNull Timestamp uploadDate, @NotNull Timestamp lastModified ) {
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

                if ( !getId().equals( that.getId() ) )
                        return false;
                if ( !getSchemaVersion().equals( that.getSchemaVersion() ) )
                        return false;
                if ( !getOrgId().equals( that.getOrgId() ) )
                        return false;
                if ( !getDataType().equals( that.getDataType() ) )
                        return false;
                if ( !getContractId().equals( that.getContractId() ) )
                        return false;
                if ( !getSpaceId().equals( that.getSpaceId() ) )
                        return false;
                if ( !getSummary().equals( that.getSummary() ) )
                        return false;
                if ( !getTags().equals( that.getTags() ) )
                        return false;
                if ( !getStatus().equals( that.getStatus() ) )
                        return false;
                if ( !getUploadBy().equals( that.getUploadBy() ) )
                        return false;
                if ( !getUploadDate().equals( that.getUploadDate() ) )
                        return false;
                return getLastModified().equals( that.getLastModified() );
        }

        @Override public int hashCode() {
                int result = getId().hashCode();
                result = 31 * result + getSchemaVersion().hashCode();
                result = 31 * result + getOrgId().hashCode();
                result = 31 * result + getDataType().hashCode();
                result = 31 * result + getContractId().hashCode();
                result = 31 * result + getSpaceId().hashCode();
                result = 31 * result + getSummary().hashCode();
                result = 31 * result + getTags().hashCode();
                result = 31 * result + getStatus().hashCode();
                result = 31 * result + getUploadBy().hashCode();
                result = 31 * result + getUploadDate().hashCode();
                result = 31 * result + getLastModified().hashCode();
                return result;
        }

        public Long getId() {
                return id;
        }

        public String getSchemaVersion() {
                return schemaVersion;
        }

        public String getOrgId() {
                return orgId;
        }

        public List<String> getDataType() {
                return dataType;
        }

        public Long getContractId() {
                return contractId;
        }

        public String getSpaceId() {
                return spaceId;
        }

        public List<String> getSummary() {
                return summary;
        }

        public Map<String, String> getTags() {
                return tags;
        }

        public Map<String, String> getStatus() {
                return status;
        }

        public String getUploadBy() {
                return uploadBy;
        }

        public Timestamp getUploadDate() {
                return uploadDate;
        }

        public Timestamp getLastModified() {
                return lastModified;
        }

}
