package com.gehc.ai.app.datacatalog.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadEntityTest {

    @Test
    public void testEqualsMethodInUploadEntity() {
        //ARRANGE
        Upload upload = buildUploadEntity();
        Upload upload1 = buildUploadEntity();
        upload1.setId(2L);

        Timestamp date1 = new Timestamp(1313045029);
        Timestamp date2 = new Timestamp(1313045568);

        //ACT && ASSERT
        EqualsVerifier
                .forClass(Upload.class)
                .withPrefabValues(Upload.class, upload, upload1)
                .withPrefabValues(Timestamp.class, date1, date2)
                .withNonnullFields( "schemaVersion", "uploadBy",
                        "orgId","contractId","spaceId","dataType","tags", "uploadDate" ,"lastModified")
                .verify();
    }

    private Upload buildUploadEntity(){
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,String> tags = new HashMap<>();
        tags.put("tag1","sample");

        Upload uploadRequest = new Upload();
        uploadRequest.setId(1L);
        uploadRequest.setSchemaVersion("v1");
        uploadRequest.setOrgId("f1341a2c-7a54-4d68-9f40-a8b2d14d3806");
        uploadRequest.setContractId(100L);
        uploadRequest.setSpaceId("space123");
        uploadRequest.setUploadBy("user1");
        uploadRequest.setDataType(dataType);
        uploadRequest.setTags(tags);
        uploadRequest.setUploadDate(new Timestamp(System.currentTimeMillis()));
        uploadRequest.setLastModified(new Timestamp(System.currentTimeMillis()));

        return uploadRequest;
    }
}
