package com.gehc.ai.app.datacatalog.entity;

import com.gehc.ai.app.datacatalog.rest.request.UpdateUploadRequest;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateUploadRequestObjectTest {

    @Test
    public void testEqualsMethodInUpdateUploadRequest() {
        //ARRANGE
        UpdateUploadRequest upload = buildUpdateUploadRequest();
        UpdateUploadRequest upload1 = buildUpdateUploadRequest2();

        Timestamp date1 = new Timestamp(1313045029);
        Timestamp date2 = new Timestamp(1313045568);

        //ACT && ASSERT
        EqualsVerifier
                .forClass(UpdateUploadRequest.class)
                .withPrefabValues(UpdateUploadRequest.class, upload, upload1)
                .withPrefabValues(Timestamp.class, date1, date2)
                .withNonnullFields( "id","schemaVersion", "uploadBy",
                        "orgId","spaceId","dataType","tags",
                                    "summary","status","uploadDate","lastModified")
                .verify();
    }

    private UpdateUploadRequest buildUpdateUploadRequest(){
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,Object> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,Integer> status = new HashMap<>();
        status.put("failures",9);
        status.put("total",100);

        return  new UpdateUploadRequest(13L,"v1","orgId217wtysgs",
                                        dataType,1L,"space123",summary,tags,
                                        status,"user1",
                                        new Timestamp( 1313045029),new Timestamp( 1313045029));

    }

    private UpdateUploadRequest buildUpdateUploadRequest2(){
        List<String> dataType = new ArrayList<>();
        dataType.add("DICOM");
        dataType.add("JPEG");
        Map<String,Object> tags = new HashMap<>();
        tags.put("tag1","sample");

        List<String> summary = new ArrayList<>();
        summary.add("uri1");
        summary.add("uri2");
        Map<String,Integer> status = new HashMap<>();
        status.put("failures",9);
        status.put("total",100);

        return  new UpdateUploadRequest(23L,"v1","orgId217wtysgs",
                                        dataType,1L,"space123",summary,tags,
                                        status,"user1",
                                        new Timestamp( 1313045029),new Timestamp( 1313045029));

    }
}
