package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.*;

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

    public ImageSeries getOneimageSeries(){
        ImageSeries imageSeries = new ImageSeries();
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
        Map prop = new HashMap<String,String>();
        prop.put("test", "bdd");
        imageSeries.setProperties(prop);
        return imageSeries;
    }

    public List<ImageSeries> getImageSeriesWithEquipmentsSpecialChars() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeriesDx = getSingleImageSeries();
        imageSeriesDx.setEquipment(" \"\\\"Geode Platform\\\"\"");
        imgSerLst.add(imageSeriesDx);
        return imgSerLst;
    }

    public ImageSeries getOneimageSerieswithInsitutions(){
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setDescription("test");
        imageSeries.setAnatomy("Lung");
        imageSeries.setModality("CT");
        imageSeries.setDataFormat("dataFormat");
        imageSeries.setUri("tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10");
        imageSeries.setSeriesInstanceUid("1");
        imageSeries.setInstitution("UCSF, Institution,Montogmenry");
        imageSeries.setEquipment("tem");
        imageSeries.setInstanceCount(1);
        imageSeries.setUploadBy("BDD");
        imageSeries.setUploadDate(getDate());
        imageSeries.setPatientDbId(1L);
        Map prop = new HashMap<String,String>();
        prop.put("test", "bdd");
        imageSeries.setProperties(prop);
        return imageSeries;
    }

    public List<ImageSeries> getImageSeries() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = getSingleImageSeries();
        imgSerLst.add(imageSeries);
        ImageSeries imageSeriesDx = getSingleImageSeries();
        imageSeriesDx.setModality("DX");
        imgSerLst.add(imageSeriesDx);
        return imgSerLst;
    }

    public List<ImageSeries> getImageSeriesWithFilterOneModality() {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        ImageSeries imageSeries = getSingleImageSeries();
        imgSerLst.add(imageSeries);
        return imgSerLst;
    }


    private ImageSeries getSingleImageSeries() {
        ImageSeries imageSeries = new ImageSeries();
        imageSeries.setId(1L);
        imageSeries.setDescription("test");
        imageSeries.setAnatomy("Lung");
        imageSeries.setModality("CT");
        imageSeries.setDataFormat("dataFormat");
        imageSeries.setUri("tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10");
        imageSeries.setSeriesInstanceUid("1");
        imageSeries.setInstitution("UCSF");
        imageSeries.setEquipment("CT");
        imageSeries.setInstanceCount(1);
        imageSeries.setUploadBy("BDD");
        imageSeries.setUploadDate(getDate());
        imageSeries.setPatientDbId(1L);
        Map prop = new HashMap<String,String>();
        prop.put("test", "bdd");
        imageSeries.setProperties(prop);
        return imageSeries;
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
        Map prop = new HashMap<String,String>();
        prop.put("test", "bdd");
        imageSeries.setProperties(prop);
        imgSerLst.add(imageSeries);
        return imgSerLst;
    }

    public String expectedImageSeries() {
        String imageSeries = "[{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"CT\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"uploadDate\":\"2017-03-31 00:00:00\",\"patientDbId\":1},{\"id\":1,\"modality\":\"DX\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"CT\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"uploadDate\":\"2017-03-31 00:00:00\",\"patientDbId\":1}]";
        return imageSeries;
    }

//    public String expectedImageSeriesWithMultipleInstitutions() {
//        String imageSeries = "[{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF,MONT\",\"equipment\":\"CT\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1},{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF,MONT\",\"equipment\":\"CT\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":2}]";
//        return imageSeries;
//    }



    public String expectedImageSeriesJson() {
        String imageSeries = "{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"CT\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"uploadDate\":\"2017-03-31 00:00:00\",\"patientDbId\":1}";
        return imageSeries;
    }

//    public String expectedImageSeriesWithEquipment() {
//
//    }

    public Annotation getAnnotation(){
        Annotation annotation = new Annotation();
        annotation.setId(1L);
        annotation.setAnnotationDate(getDate());
        annotation.setAnnotatorId("87654321-abcd-42ca-a317-4d408b98c500");
        annotation.setAnnotationTool("Tool");
        ImageSeries imageSeries =  getOneimageSeries();
        imageSeries.setId(1L);
        annotation.setImageSet(imageSeries);
       annotation.setItem(new HashMap<String,String>());
        annotation.setSchemaVersion("123");
        annotation.setType("point");
        annotation.setSchemaVersion("v1");
        annotation.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        annotation.setImageSetId(1L);
        return annotation;
    }

    public Annotation getAnnotationWithOutValidId(){
        Annotation annotation = new Annotation();
        annotation.setId(1L);
        annotation.setAnnotationDate(getDate());
        annotation.setAnnotatorId("123");
        ImageSeries imageSeries =  getOneimageSeries();
        imageSeries.setId(4L);
        annotation.setImageSet(imageSeries);

       annotation.setItem(new HashMap<String,String>());
        annotation.setSchemaVersion("123");
        annotation.setType("point");
        annotation.setSchemaVersion("1");
        return annotation;
    }

    public List<ImageSeries> getImageSeriesWithEquipment() {
        ImageSeries imageSeries = getSingleImageSeries();

        imageSeries.setEquipment("CT");
        List<ImageSeries> imgSerLst = getImageSeriesWithFilters(imageSeries);
        return imgSerLst;
    }

    public List<ImageSeries> getImageSeriesWithFilters(ImageSeries imageSeries) {
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        imgSerLst.add(imageSeries);
        return imgSerLst;
    }


    public List<ImageSeries> getImageSeriesWithInstitution() {
        ImageSeries imageSeries = getSingleImageSeries();
        imageSeries.setInstitution("UCSFTEST");
        List<ImageSeries> imgSerLst = getImageSeriesWithFilters(imageSeries);
        return imgSerLst;
    }
}
