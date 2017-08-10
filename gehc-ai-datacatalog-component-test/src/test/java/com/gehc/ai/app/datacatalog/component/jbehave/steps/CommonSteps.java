package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by sowjanyanaidu on 7/13/17.
 */
@Component
public class CommonSteps {

    public Date getDate() {
        String str = "2017-03-31";
        return Date.valueOf(str);
    }

    public Date getDateTime() {
        String str = "2017-03-31 01:01:01 AM";
        return Date.valueOf(str);
    }

    public List<ImageSeries> getImageSeries() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(1L);
        imageSeries.setDescription("test");
        imageSeries.setAnatomy("Lung");
        imageSeries.setModality("CT");
        imageSeries.setDataFormat("dataFormat");
        imageSeries.setUri("tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10");
        imageSeries.setSeriesInstanceUid("1");
        imageSeries.setInstitution("UCSF");
        imageSeries.setEquipment("tem");
        imageSeries.setInstanceCount(1);
        imageSeries.setUploadBy("BDD");
        imageSeries.setUploadDate(getDate());
        imageSeries.setPatientDbId(1L);
        Properties prop = new Properties();
        prop.setProperty("test", "bdd");
        imageSeries.setProperties(prop);
        imgSerLst.add(imageSeries);
        return imgSerLst;
    }

    public List<ImageSeries> getImageSeriestwo() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(2L);
        imageSeries.setDescription("test");
        imageSeries.setAnatomy("Lung");
        imageSeries.setModality("CT");
        imageSeries.setDataFormat("dataFormat");
        imageSeries.setUri("tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10");
        imageSeries.setSeriesInstanceUid("1");
        imageSeries.setInstitution("CSF");
        imageSeries.setEquipment("tem");
        imageSeries.setInstanceCount(1);
        imageSeries.setUploadBy("BDD");
        imageSeries.setPatientDbId(1L);
        Properties prop = new Properties();
        prop.setProperty("test", "bdd");
        imageSeries.setProperties(prop);
        imgSerLst.add(imageSeries);
        return imgSerLst;
    }

    public String expectedImageSeries() {
        String imageSeries = "{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"tem\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"uploadDate\":\"2017-03-31\",\"patientDbId\":1,\"count\":0}";
        return imageSeries;
    }

    public Annotation getAnnotation(){
        Annotation annotation = new Annotation();
        annotation.setId(1L);
        annotation.setAnnotationDate(getDate());
        annotation.setAnnotatorId("123");
        annotation.setImageSet("1");
        annotation.setItem("item");
        annotation.setSchemaVersion("123");
        annotation.setType("type");
        annotation.setSchemaVersion("1");
        return annotation;
    }

    public Annotation getAnnotationWithOutValidId(){
        Annotation annotation = new Annotation();
        annotation.setId(1L);
        annotation.setAnnotationDate(getDate());
        annotation.setAnnotatorId("123");
        annotation.setImageSet("123");
        annotation.setItem("item");
        annotation.setSchemaVersion("123");
        annotation.setType("type");
        annotation.setSchemaVersion("1");
        return annotation;
    }


}
