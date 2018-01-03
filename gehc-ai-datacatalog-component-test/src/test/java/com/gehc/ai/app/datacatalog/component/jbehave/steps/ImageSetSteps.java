package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.hibernate.service.spi.ServiceException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.Annotation;
import com.gehc.ai.app.datacatalog.entity.ImageSeries;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;

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

    private DataCatalogDaoImpl dataCatalogDao;
    private String IMAGE_SERIES_WITH_INSTITUTIONS = "[{\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF, Institution,Montogmenry\",\"equipment\":\"tem\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1}]";
    private String IMAGE_SERIES_WITH_EQUIPMENT = "[{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\" \\\"\\\\\\\"Geode Platform\\\\\\\"\\\"\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1}]";
    @Autowired
    public ImageSetSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository, ImageSeriesRepository imageSeriesRepository, StudyRepository studyRepository,CommonSteps commonSteps,DataCatalogInterceptor dataCatalogInterceptor,DataCatalogDaoImpl dataCatalogDao) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.studyRepository = studyRepository;
        this.commonSteps =commonSteps;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
        this.dataCatalogDao =dataCatalogDao;

    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class),any(HttpServletResponse.class),anyObject())).thenReturn(true);
        when(dataCatalogDao.getImgSeries(anyMap(),anyList(), anyList())).thenReturn(commonSteps.getImageSeries());

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
                get("/api/v1/datacatalog/image-series?series_instance_uid=1&org_id=12345678-abcd-42ca-a317-4d408b98c500")

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
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );

    }

    @Then("verify Store an image set data")
    public void verifyStoreAnImageSetData() throws Exception {
        retrieveResult.andExpect(status().isOk());

        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeriesJson())));
    }


    @Given("Get Image set based on filter criteria with SUID - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteria() throws Exception {
        when(imageSeriesRepository.findBySeriesInstanceUidInAndOrgIdIn(anyListOf(String.class),anyListOf(String.class))).thenReturn(commonSteps.getImageSeries());

    }


    @When("Get Image set based on filter criteria with SUID")
    public void getImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?series_instance_uid=1&org_id=12345678-abcd-42ca-a317-4d408b98c500")

        );

    }

    @Then("verify Image set based on filter with SUID")
    public void verifyImagesetImageBasedOnFilterCriteria() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with SUID Thorws runtime exception - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteriaException() throws Exception {
        when(imageSeriesRepository.findBySeriesInstanceUidInAndOrgIdIn(anyListOf(String.class),anyListOf(String.class))).thenThrow(RuntimeException.class);
    }


    @When("Get Image set based on filter criteria with SUID Thorws runtime exception")
    public void getImagesetImageBasedOnFilterCriteriaException() throws Exception {
        try{
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?series_instance_uid=1&org_id=12345678-abcd-42ca-a317-4d408b98c500")
        );}
        catch(Exception e){
            throwable = e;
        }

    }

    @Then("verify Image set based on filter with SUID Thorws runtime exception")
    public void verifyImagesetImageBasedOnFilterCriteriaException() throws Exception {
        assert (throwable.toString().contains("Request processing failed"));
    }

    @Given("Get Image set based on filter criteria with ORG ID and Modality - DataSetUp Provided")
    public void givenImagesetImageBasedOnFilterCriteriaOrgId() throws Exception {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
    }


    @When("Get Image set based on filter criteria with ORG ID and Modality")
    public void getImagesetImageBasedOnFilterCriteriaOrgId() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=61939267-d195-499f-bfd8-7d92875c7035&modality=CT")
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
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
    }


    @When("Get Image set based on filter criteria with ORG ID , Modality and Anatomy")
    public void getImagesetImageBasedOnFilterCriteriaOrgIdModAnatomy() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=61939267&modality=CT&anatomy=Lung")
                        .contentType(MediaType.APPLICATION_JSON)
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
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
        List <Annotation> annList = new ArrayList<Annotation>();
        annList.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetIdInAndTypeInAndOrgId(anyListOf(Long.class),anyListOf(String.class),anyList())).thenReturn(annList);
    }

    @When("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotation() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=61939267&modality=CT&anatomy=Lung&annotations=point")
                        .contentType(MediaType.APPLICATION_JSON)
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
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }

    @When("Get Image set based on filter criteria with ORG ID")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGID() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=61939267")
                        .contentType(MediaType.APPLICATION_JSON)
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
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }

    @When("Get Image set based on filter criteria with ORG ID and Anatomy")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDAndAnatomy() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=61939267&anatomy=Lung")
                        .contentType(MediaType.APPLICATION_JSON)
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
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenThrow(Exception.class);
    }

    @When("Get Image set based on filter criteria with ORG ID and Modality throws Exception")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDAndModalityThrowsException(){
        try {
            retrieveResult = mockMvc.perform(
                    get("/api/v1/datacatalog/image-series?org_id=61939267-d195-499f-bfd8-7d92875c7035&modality=CT")
                            .contentType(MediaType.APPLICATION_JSON)
            );
        }
        catch(Exception e){
            throwable= e;
        };

    }
    @Then("verify Image set based on filter  with ORG ID and Modality throws Exception")
    public void thenVerifyImageSetBasedOnFilterWithORGIDAndModalityThrowsException() throws Exception {
        assert (throwable.toString().contains("Request processing failed"));
    }

    @Given("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationABSENTDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeriesWithFilterOneModality();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
        List <Annotation> annList = new ArrayList<Annotation>();
        annList.add(commonSteps.getAnnotationWithOutValidId());
        when(annotationRepository.findByImageSetIdIn(anyListOf(Long.class))).thenReturn(annList);
    }
    @When("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationABSENT() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=61939267&modality=CT&anatomy=Lung&annotations=absent")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT")
    public void thenVerifyImageSetBasedOnFilterWithORGIDModalityAnatomyAndAnnotationABSENT() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[{\"id\":1,\"modality\":\"CT\",\"anatomy\":\"Lung\",\"dataFormat\":\"dataFormat\",\"uri\":\"tests3://gehc-data-repo-main/imaging/ct/lungData/LungCT_LIDC_LS/set10\",\"seriesInstanceUid\":\"1\",\"description\":\"test\",\"institution\":\"UCSF\",\"equipment\":\"CT\",\"instanceCount\":1,\"properties\":{\"test\":\"bdd\"},\"uploadBy\":\"BDD\",\"patientDbId\":1}]")));

    }

    @Given("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationABSENTWithNoMatchingImageSeriesDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
        List <Annotation> annList = new ArrayList<Annotation>();
        annList.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetIdIn(anyListOf(Long.class))).thenReturn(annList);
    }
    @When("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy and Annotation ABSENT with no matching ImageSeries")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyAndAnnotationABSENTWithNoMatchingImageSeries() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=61939267&modality=CT&anatomy=Lung&annotations=absent")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with ORG ID ,Modality, Anatomy and Annotation ABSENT returns empty ImageSeries")
    public void thenVerifyImageSetBasedOnFilterWithORGIDModalityAnatomyAndAnnotationABSENTReturnsEmptyImageSeries() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    @Given("Get Image set based on filter criteria with No Annotation - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithNoAnnotationDataSetUpProvided() {
        List noAnn = new ArrayList<Long>();
        noAnn.add(0,1L);
        when(imageSeriesRepository.countImgWithNoAnn(anyString())).thenReturn(noAnn);
    }
    @When("Get Image set based on filter criteria with No Annotation")
    public void whenGetImageSetBasedOnFilterCriteriaWithNoAnnotation() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-set/no-annotations")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with No Annotation")
    public void thenVerifyImageSetBasedOnFilterWithNoAnnotation() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[1]")));
    }

    @Given("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyGE_CLASSAndAnnotationDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
        List <Annotation> annList = new ArrayList<Annotation>();
        annList.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetIdInAndTypeIn(anyListOf(Long.class),anyListOf(String.class))).thenReturn(annList);
    }

    @When("Get Image set based on filter criteria with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDModalityAnatomyGE_CLASSAndAnnotation() throws Exception {


        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&anatomy=Chest,Lung")
                        .contentType(MediaType.APPLICATION_JSON)
                .param("ge-class", "[{\"name\": \"Aorta Abnormalities\"}, {\"name\": \"Pediatric\", \"value\": \"\", \"patient_outcome\": \"40\"}]")
        );

    }

    @Then("verify Image set based on filter  with ORG ID ,Modality, Anatomy, GE_CLASS and Annotation")
    public void thenVerifyImageSetBasedOnFilterWithORGIDModalityAnatomyGE_CLASSAndAnnotation() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithEquipmentDataSetUpProvided() throws Exception {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
    }

    @When("Get Image set based on filter criteria with Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Then("verify Image set based on filter  with Equipment")
    public void thenVerifyImageSetBasedOnFilterWithEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Equipment name containing accepted special characters like quotes and slashes - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithEquipmentNameContainingAcceptedSpecialCharactersLikeQuotesAndSlashesDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeriesWithEquipmentsSpecialChars();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Equipment name containing accepted special characters like quotes and slashes")
    public void whenGetImageSetBasedOnFilterCriteriaWithEquipmentNameContainingAcceptedSpecialCharactersLikeQuotesAndSlashes() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&equipment=\"\\\"Geode Platform\\\"\"")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Equipment name containing accepted special characters like quotes and slashes")
    public void thenVerifyImageSetBasedOnFilterWithEquipmentNameContainingAcceptedSpecialCharactersLikeQuotesAndSlashes() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(IMAGE_SERIES_WITH_EQUIPMENT)));

    }

    @Given("Get Image set based on filter criteria with Institution - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithInstitutionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
    }

    @When("Get Image set based on filter criteria with Institution")
    public void whenGetImageSetBasedOnFilterCriteriaWithInstitution() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&institution=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Institution")
    public void thenVerifyImageSetBasedOnFilterWithInstitution() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Institution name containing accepted special characters- DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithInstitutionNameContainingAcceptedSpecialCharactersDataSetUpProvided() {
        ImageSeries imageSeries = commonSteps.getOneimageSerieswithInsitutions();
        List<ImageSeries> imgSerLst = new ArrayList<ImageSeries>();
        imgSerLst.add(imageSeries);
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSerLst);
    }

    @When("Get Image set based on filter criteria with Institution name containing accepted special characters")
    public void whenGetImageSetBasedOnFilterCriteriaWithInstitutionNameContainingAcceptedSpecialCharacters() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&institution=UCSF%2C Institution,Montogmery")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Institution name containing accepted special characters")
    public void thenVerifyImageSetBasedOnFilterWithInstitutionNameContainingAcceptedSpecialCharacters() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(IMAGE_SERIES_WITH_INSTITUTIONS)));

    }

    @Given("Get Image set based on filter criteria with DataFormat - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithDataFormatDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with DataFormat")
    public void whenGetImageSetBasedOnFilterCriteriaWithDataFormat() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&data_format=dataFormat")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with DataFormat")
    public void thenVerifyImageSetBasedOnFilterWithDataFormat() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Institution and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithInstitutionAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Institution and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithInstitutionAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&institution=UCSF&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Institution and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithInstitutionAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with DataFormat and Institution - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithDataFormatAndInstitutionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
    }

    @When("Get Image set based on filter criteria with DataFormat and Institution")
    public void whenGetImageSetBasedOnFilterCriteriaWithDataFormatAndInstitution() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&data_format=dataFormat&institution=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with DataFormat and Institution")
    public void thenVerifyImageSetBasedOnFilterWithDataFormatAndInstitution() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with DataFormat, Institution and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithDataFormatInstitutionAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with DataFormat, Institution and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithDataFormatInstitutionAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&data_format=dataFormat&institution=UCSF&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with DataFormat, Institution and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithDataFormatInstitutionAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with DataFormat and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithDataFormatAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with DataFormat and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithDataFormatAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&data_format=dataFormat&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with DataFormat and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithDataFormatAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Anatomy and DataFormat - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithAnatomyAndDataFormatDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);
    }
    @When("Get Image set based on filter criteria with Anatomy and DataFormat")
    public void whenGetImageSetBasedOnFilterCriteriaWithAnatomyAndDataFormat() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&anatomy=chest&data_format=dataFormat")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Anatomy and DataFormat")
    public void thenVerifyImageSetBasedOnFilterWithAnatomyAndDataFormat() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Anatomy, DataFormat and Institution - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithAnatomyDataFormatAndInstitutionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Anatomy, DataFormat and Institution")
    public void whenGetImageSetBasedOnFilterCriteriaWithAnatomyDataFormatAndInstitution() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&anatomy=chest&data_format=dataFormat&institution=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Anatomy, DataFormat and Institution")
    public void thenVerifyImageSetBasedOnFilterWithAnatomyDataFormatAndInstitution() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithAnatomyDataFormatInstitutionAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Anatomy, DataFormat, Institution and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithAnatomyDataFormatInstitutionAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&anatomy=chest&data_format=dataFormat&institution=UCSF&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Anatomy, DataFormat, Institution and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithAnatomyDataFormatInstitutionAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Anatomy, Institution - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithAnatomyInstitutionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Anatomy, Institution")
    public void whenGetImageSetBasedOnFilterCriteriaWithAnatomyInstitution() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&anatomy=chest&institution=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Anatomy, Institution")
    public void thenVerifyImageSetBasedOnFilterWithAnatomyInstitution() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Anatomy, Institution and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithAnatomyInstitutionAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Anatomy, Institution and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithAnatomyInstitutionAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&anatomy=chest&institution=UCSF&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    @Then("verify Image set based on filter  with Anatomy, Institution and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithAnatomyInstitutionAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Anatomy, Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithAnatomyEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Anatomy, Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithAnatomyEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&anatomy=chest&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Anatomy, Equipment")
    public void thenVerifyImageSetBasedOnFilterWithAnatomyEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Modality, Anatomy and DataFormat - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyAndDataFormatDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Anatomy and DataFormat")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyAndDataFormat() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&anatomy=Chest&data_format=dataFormat")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Anatomy and DataFormat")
    public void thenVerifyImageSetBasedOnFilterWithModalityAnatomyAndDataFormat() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyDataFormatAndInstitutionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Institution")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyDataFormatAndInstitution() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&anatomy=Chest&data_format=dataFormat&institution=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Anatomy, DataFormat and Institution")
    public void thenVerifyImageSetBasedOnFilterWithModalityAnatomyDataFormatAndInstitution() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyDataFormatInstitutionAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Anatomy, DataFormat, Institution and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyDataFormatInstitutionAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&anatomy=Chest&data_format=dataFormat&institution=UCSF&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Anatomy, DataFormat, Institution and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithModalityAnatomyDataFormatInstitutionAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyInstitutionAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Anatomy, Institution and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyInstitutionAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT&anatomy=Chest&institution=UCSF&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Anatomy, Institution and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithModalityAnatomyInstitutionAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, Anatomy, Institution - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyInstitutionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Anatomy, Institution")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyInstitution() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT&anatomy=Chest&institution=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Anatomy, Institution")
    public void thenVerifyImageSetBasedOnFilterWithModalityAnatomyInstitution() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, Anatomy, Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Anatomy, Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT&anatomy=Chest&equipment=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Anatomy, Equipment")
    public void thenVerifyImageSetBasedOnFilterWithModalityAnatomyEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Equipment")
    public void thenVerifyImageSetBasedOnFilterWithModalityEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, Institution - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityInstitutionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Institution")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityInstitution() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT&institution=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Institution")
    public void thenVerifyImageSetBasedOnFilterWithModalityInstitution() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, DataFormat - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityDataFormatDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, DataFormat")
    public void v() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT&data_format=dataFormat")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, DataFormat")
    public void thenVerifyImageSetBasedOnFilterWithModalityDataFormat() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyDataFormatAndEquipmentDataSetUpProvided() {

        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Anatomy, DataFormat and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityAnatomyDataFormatAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&anatomy=Chest&data_format=dataFormat&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Anatomy, DataFormat and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithModalityAnatomyDataFormatAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityDataFormatInstitutionAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, DataFormat,Institution And Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityDataFormatInstitutionAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&data_format=dataFormat&institution=UCSF%2C+MONT,TEST&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, DataFormat,Institution And Equipment")
    public void thenVerifyImageSetBasedOnFilterWithModalityDataFormatInstitutionAndEquipment() throws Exception {
         retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Modality, DataFormat And Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityDataFormatAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, DataFormat And Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityDataFormatAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&data_format=dataFormat&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, DataFormat And Equipment")
    public void thenVerifyImageSetBasedOnFilterWithModalityDataFormatAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Modality, DataFormat And Institution - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityDataFormatAndInstitutionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, DataFormat And Institution")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityDataFormatAndInstitution() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&data_format=dataFormat&institution=UCSF")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, DataFormat And Institution")
    public void thenVerifyImageSetBasedOnFilterWithModalityDataFormatAndInstitution() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));
    }

    @Given("Get Image set based on filter criteria with Modality, Institution and Equipment - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithModalityInstitutionAndEquipmentDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(anyMap())).thenReturn(imgSeries);

    }
    @When("Get Image set based on filter criteria with Modality, Institution and Equipment")
    public void whenGetImageSetBasedOnFilterCriteriaWithModalityInstitutionAndEquipment() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?org_id=4fac7976-e58b-472a-960b-42d7e3689f20&modality=CT,DX&institution=UCSF&equipment=CT")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter  with Modality, Institution and Equipment")
    public void thenVerifyImageSetBasedOnFilterWithModalityInstitutionAndEquipment() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(commonSteps.expectedImageSeries())));

    }

    @Given("Get Image set based on filter criteria with ORG ID and Modality throws Service Exception - DataSetUp Provided")
    public void givenGetImageSetBasedOnFilterCriteriaWithORGIDAndModalityThrowsServiceExceptionDataSetUpProvided() {
        List<ImageSeries> imgSeries = commonSteps.getImageSeries();
        when(dataCatalogDao.getImgSetByFilters(null)).thenThrow(ServiceException.class);
    }
    @When("Get Image set based on filter criteria with ORG ID and Modality throws Service Exception")
    public void whenGetImageSetBasedOnFilterCriteriaWithORGIDAndModalityThrowsServiceException() throws Exception {
        try {
            retrieveResult = mockMvc.perform(
                    get("/api/v1/datacatalog/image-series?org_id=61939267-d195-499f-bfd8-7d92875c7035&modality=CT")
                            .contentType(MediaType.APPLICATION_JSON)
            );
        }
        catch(ServiceException e){
            throwable= e;
        };
    }
    @Then("verify Image set based on filter  with ORG ID and Modality throws Service Exception")
    public void thenVerifyImageSetBasedOnFilterWithORGIDAndModalityThrowsServiceException() throws Exception {
        assert (throwable.toString().contains("Request processing failed"));
    }

    @Given("Get Image set based on filter criteria with filters null")
    public void givenGetImageSetBasedOnFilterCriteriaWithFiltersNull() {
    }
    @When("Get Image set based on filter criteria with filters null")
    public void whenGetImageSetBasedOnFilterCriteriaWithFiltersNull() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/image-series?test=test")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
    @Then("verify Image set based on filter with filters null")
    public void thenVerifyImageSetBasedOnFilterWithFiltersNull() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    private String imageSeriesToJSON(ImageSeries imageSeries) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageSeries);
    }

    private void dataSetUpByFilter() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findBySeriesInstanceUidInAndOrgIdIn(anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSerLst);
        when(imageSeriesRepository.findByOrgIdInAndAnatomyInAndModalityIn(anyListOf(String.class),
                anyListOf(String.class), anyListOf(String.class))).thenReturn(imgSerLst);
        List<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(commonSteps.getAnnotation());
        when(annotationRepository.findByImageSetIdIn(anyListOf(Long.class))).thenReturn(annotations);

    }

    private void dataSetUpImageSeriesById() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findById(anyLong())).thenReturn(imgSerLst);
    }

    private void dataSetUpImageSeriesBySeriesInstanceId() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findBySeriesInstanceUidInAndOrgIdIn(anyListOf(String.class),anyListOf(String.class))).thenReturn(imgSerLst);
    }

    private void dataSetUpImagesetByStudy() {
        List<ImageSeries> imgSerLst = commonSteps.getImageSeries();
        when(imageSeriesRepository.findByStudyDbIdAndOrgId(anyLong(), anyString())).thenReturn(imgSerLst);
    }

}
