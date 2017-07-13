package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sowjanyanaidu on 7/13/17.
 */
@Component
public class StudySteps {
    private final MockMvc mockMvc;
    private final StudyRepository studyRepository;

    private final ImageSeriesRepository imageSeriesRepository;
    private ResultActions retrieveResult;

    @Autowired
    public StudySteps(MockMvc mockMvc, StudyRepository studyRepository, ImageSeriesRepository imageSeriesRepository) {
        this.mockMvc = mockMvc;
        this.studyRepository = studyRepository;
        this.imageSeriesRepository = imageSeriesRepository;
    }

    @Given("Get Imageset by study - DataSetUp Provided")
    public void givenImagesetByStudy() throws Exception {
        dataSetUpImagesetByStudy();

    }

    @When("Get Imageset by study")
    public void getImagesetByStudy() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/study/123/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify Imageset by study")
    public void verifyImagesetByStudy() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(expectedImageSeries())));
    }

    private void dataSetUpImagesetByStudy() {
        List<ImageSeries> imgSerLst = getImageSeries();
        when(imageSeriesRepository.findByStudyDbIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }

    private List<ImageSeries> getImageSeries() {
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
        imageSeries.setPatientDbId(1L);
        Properties prop = new Properties();
        prop.setProperty("test", "bdd");
        imageSeries.setProperties(prop);
        imgSerLst.add(imageSeries);
        return imgSerLst;
    }

    private String expectedImageSeries() {
        String imageSeries = "{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"tem\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1}";
        return imageSeries;
    }


}
