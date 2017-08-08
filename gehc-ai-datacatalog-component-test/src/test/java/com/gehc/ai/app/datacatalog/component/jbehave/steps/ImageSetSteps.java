package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sowjanyanaidu on 7/13/17.
 */
@Component
public class ImageSetSteps {
    private final DataSetRepository dataSetRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final StudyRepository studyRepository;
    private final CommonSteps commonSteps;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;
    private Throwable throwable = null;

    @Autowired
    public ImageSetSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository, ImageSeriesRepository imageSeriesRepository, StudyRepository studyRepository,CommonSteps commonSteps,DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.studyRepository = studyRepository;
        this.commonSteps =commonSteps;
        this.dataCatalogInterceptor = dataCatalogInterceptor;

    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class),any(HttpServletResponse.class),anyObject())).thenReturn(true);
    }

    @Given("Retrieve image series by id - DataSetUp Provided")
    public void givenImageSeriesById() throws Exception {
        dataSetUpImageSeriesById();
    }

    @When("Get image series by image series id")
    public void getImageSeriesById() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        //.requestAttr("orgId", "12")
        );

    }

    @Then("verify image series by image series id")
    public void verifyImageSeriesById() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Retrieve image series by series instance uid - DataSetUp Provided")
    public void givenImageSeriesBySeriesInstanceId() throws Exception {
        dataSetUpImageSeriesBySeriesInstanceId();

    }

    @When("Get image series by series instance uid")
    public void getImageSeriesBySeriesInstanceId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?series-instance-uid=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify image series by series instance uid")
    public void verifyImageSeriesBySeriesInstanceId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Store an image set data - DataSetUp Provided")
    public void givenStoreAnImageSetData() throws Exception {
        List<ImageSeries> imageSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.save(any(ImageSeries.class))).thenReturn(imageSeries.get(0));
    }

    @When("Store an image set data")
    public void StoreAnImageSetData() throws Exception {
        List<ImageSeries> imageSeries = commonSteps.getImageSeries();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/image-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(imageSeriesToJSON(imageSeries.get(0)))
                        .requestAttr("orgId", "123")
        );

    }

    @Then("verify Store an image set data")
    public void verifyStoreAnImageSetData() throws Exception {
        retrieveResult.andExpect(status().isOk());

        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }


    @Given("Get Image set based on filter criteria with SUID - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteria() throws Exception {
        when(imageSeriesRepository.findBySeriesInstanceUidIn(anyListOf(String.class))).thenReturn(commonSteps.getImageSeries());
    }


    @When("Get Image set based on filter criteria with SUID")
    public void getImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?series-instance-uid=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify Image set based on filter with SUID")
    public void verifyImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with ORG ID and Modality - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteriaOrgId() throws Exception {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdInAndModalityIn(anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSeries);
//        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSeries);
//        List <Annotation> annList = new ArrayList<Annotation>();
//        annList.add(commonSteps.getAnnotation());
//        when(annotationRepository.findByImageSetIn(anyListOf(String.class))).thenReturn(annList);
    }


    @When("Get Image set based on filter criteria with ORG ID and Modality")
    public void getImagesetImageBasedOnFilterCriteriaOrgId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?org-id=61939267-d195-499f-bfd8-7d92875c7035&modality=CT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify Image set based on filter  with ORG ID and Modality")
    public void verifyImagesetImageBasedOnFilterCriteriaOrgId() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with ORG ID , Modality and Anatomy - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteriaOrgIdModAnatomy() throws Exception {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSeries);
    }


    @When("Get Image set based on filter criteria with ORG ID , Modality and Anatomy")
    public void getImagesetImageBasedOnFilterCriteriaOrgIdModAnatomy() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?org-id=61939267&modality=CT&anatomy=Lung")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }

    @Then("verify Image set based on filter  with ORG ID , Modality and Anatomy")
    public void verifyImagesetImageBasedOnFilterCriteriaOrgIdModAnatomy() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSeries);
        List <Annotation> annList = new ArrayList<Annotation>();
        annList.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetInAndTypeIn(anyListOf(String.class),anyListOf(String.class))).thenReturn(annList);
    }

    @When("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotation() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?org-id=61939267&modality=CT&anatomy=Lung&annotations=point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }

    @Then("verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation")
    public void thenVerifyImageSetBasedOnFilterWithORGIDModalityAnatomyAndAnnotation() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with ORG ID - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdIn(anyListOf(String.class))).thenReturn(imgSeries);

    }

    @When("Get Image set based on filter criteria with ORG ID")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGID() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?org-id=61939267")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }

    @Then("verify Image set based on filter  with ORG ID")
    public void thenVerifyImageSetBasedOnFilterWithORGID() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with ORG ID and Anatomy - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDAndAnatomyDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdInAndAnatomyIn(anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSeries);

    }

    @When("Get Image set based on filter criteria with ORG ID and Anatomy")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDAndAnatomy() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?org-id=61939267&anatomy=Lung")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }

    @Then("verify Image set based on filter  with ORG ID and Anatomy")
    public void thenVerifyImageSetBasedOnFilterWithORGIDAndAnatomy() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }
    @Given("Get Image set based on filter criteria with ORG ID and Modality throws Exception - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDAndModalityThrowsExceptionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdInAndModalityIn(anyListOf(String.class),anyListOf(String.class))).thenThrow(Exception.class);
    }

    @When("Get Image set based on filter criteria with ORG ID and Modality throws Exception")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDAndModalityThrowsException(){
        try {
            retrieveResult = mockMvc.perform(
                    get("/api/v1/datacatalog/image-set?org-id=61939267-d195-499f-bfd8-7d92875c7035&modality=CT")
                            .contentType(MediaType.APPLICATION_JSON)
            );
        }
        catch(Exception e){
            throwable= e;
        };

    }
    @Then("verify Image set based on filter  with ORG ID and Modality throws Exception")
    public void thenVerifyImageSetBasedOnFilterWithORGIDAndModalityThrowsException() throws Exception {
        retrieveResult.andExpect(status().is(200));
        assert (throwable.toString().contains("Request processing failed"));
    }

    @Given("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationABSENTDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSeries);
        List <Annotation> annList = new ArrayList<Annotation>();
        annList.add(commonSteps.getAnnotationWithOutValidId());
        when(annotationRepository.findByImageSetIn(anyListOf(String.class))).thenReturn(annList);
    }
    @When("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationABSENT() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?org-id=61939267&modality=CT&anatomy=Lung&annotations=absent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }
    @Then("verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT")
    public void thenVerifyImageSetBasedOnFilterWithORGIDModalityAnatomyAndAnnotationABSENT() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"tem\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1}]")));

    }

    @Given("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationABSENTWithNoMatchingImageSeriesDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSeries);
        List <Annotation> annList = new ArrayList<Annotation>();
        annList.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetIn(anyListOf(String.class))).thenReturn(annList);
    }
    @When("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationABSENTWithNoMatchingImageSeries() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set?org-id=61939267&modality=CT&anatomy=Lung&annotations=absent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );
    }
    @Then("verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT returns empty ImageSeries")
    public void thenVerifyImageSetBasedOnFilterWithORGIDModalityAnatomyAndAnnotationABSENTReturnsEmptyImageSeries() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    private String imageSeriesToJSON(ImageSeries imageSeries) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageSeries);
    }

    private void dataSetUpByFilter() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findBySeriesInstanceUidIn(anyListOf(String.class))).thenReturn(imgSerLst);
        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),
                anyListOf(String.class), anyListOf(String.class))).thenReturn(imgSerLst);
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetIn(anyListOf(String.class))).thenReturn(annotations);

    }

    private void dataSetUpImageSeriesById() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findById(anyLong())).thenReturn(imgSerLst);
    }

    private void dataSetUpImageSeriesBySeriesInstanceId() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findBySeriesInstanceUidIn(anyListOf(String.class))).thenReturn(imgSerLst);
    }

    private void dataSetUpImagesetByStudy() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByStudyDbIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }


}
