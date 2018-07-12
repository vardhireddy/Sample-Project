package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.AnnotationProperties;
import com.gehc.ai.app.datacatalog.repository.AnnotationPropRepository;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.hibernate.service.spi.ServiceException;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sowjanyanaidu on 7/14/17.
 */
@Component
public class AnnotationPropertiesSteps {
    private final MockMvc mockMvc;

    private final PatientRepository patientRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final AnnotationRepository annotationRepository;
    private final CommonSteps commonSteps;
    private final AnnotationPropRepository annotationPropRepository;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private ResultActions retrieveResult;
    private String AnnotationProp = "[{\"id\":1,\"schemaVersion\":\"123\",\"orgId\":\"12345678-abcd-42ca-a317-4d408b98c500\",\"resourceName\":\"TEST\",\"classes\":null,\"createdDate\":\"2017-03-31\",\"createdBy\":\"test\"}]";
    private Throwable throwable = null;
    @Autowired
    public AnnotationPropertiesSteps(MockMvc mockMvc, AnnotationPropRepository annotationPropRepository, PatientRepository patientRepository, ImageSeriesRepository imageSeriesRepository,AnnotationRepository annotationRepository,CommonSteps commonSteps,DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.annotationPropRepository = annotationPropRepository;
        this.patientRepository = patientRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.annotationRepository = annotationRepository;
        this.commonSteps = commonSteps;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class),any(HttpServletResponse.class),anyObject())).thenReturn(true);
    }
    
    @Given("Store an Annotation Properties set data - DataSetUp Provided")
    public void givenStoreAnAnnotationPropertiesSetDataDataSetUpProvided() {
       AnnotationProperties annotationProperties =  setAnnotationProp();
       when(annotationPropRepository.save(any(AnnotationProperties.class))).thenReturn(annotationProperties);

    }
    @When("Store an Annotation Properties set data")
    public void whenStoreAnAnnotationPropertiesSetData() throws Exception {
        AnnotationProperties annotationProperties =  setAnnotationProp();
        retrieveResult = mockMvc.perform(
                post("/api/v1/annotation-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AnnotationPropertiesToJSON(annotationProperties))
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }
    
    @Then("Verify Store an Annotation Properties set data")
    public void thenVerifyStoreAnAnnotationPropertiesSetData() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("SUCCESS")));
    }
    
    @Given("Get Annotation Properties set data with invalid orgId Throws Exception - DataSetUp Provided")
    public void givenGetAnnotationPropertiesSetDataWithInvalidOrgIdThrowsExceptionDataSetUpProvided() {
    	AnnotationProperties annotationProperties =  setAnnotationProp();
        List<AnnotationProperties> annoPropList =  new ArrayList<AnnotationProperties>();
        annoPropList.add(annotationProperties);
        when(annotationPropRepository.findByOrgId(anyString())).thenReturn(annoPropList);
    }


    @When("Get Annotation Properties set data with invalid orgId - Throws Exception")
    public void whenGetAnnotationPropertiesSetDataWithInvalidOrgIdThrowsException() throws Exception{
    	retrieveResult = mockMvc.perform(
                get("/api/v1/annotation-properties?orgId=123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "123")
        );
    }

    @Then("Verify Get Annotation Properties set data with invalid orgId Throws Exception")
    public void thenVerifyGetAnnotationPropertiesSetDataWithInvalidOrgIdThrowsException() throws Exception{
    	retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }
    
    @Given("Get Annotation Properties set data with long orgId Throws Exception - DataSetUp Provided")
    public void givenGetAnnotationPropertiesSetDataWithLongOrgIdThrowsExceptionDataSetUpProvided() {
    	AnnotationProperties annotationProperties =  setAnnotationProp();
        List<AnnotationProperties> annoPropList =  new ArrayList<AnnotationProperties>();
        annoPropList.add(annotationProperties);
        when(annotationPropRepository.findByOrgId(anyString())).thenReturn(annoPropList);
    }


    @When("Get Annotation Properties set data with long orgId - Throws Exception")
    public void whenGetAnnotationPropertiesSetDataWithLongOrgIdThrowsException() throws Exception{
    	String longOrgId = "12345678-abcd-42ca-a317-4d408b98c500-abcd-42ca-a317-4d408b98c500-abcd-42ca-a317-4d408b98c500-12345678-abcd-42ca-a317-4d408b98c500-abcd-42ca-a317-4d408b98c500-abcd-42ca-a317-4d408b98c500-12345678-abcd-42ca-a317-4d408b98c500-4d408b98c500-abcd-42ca-a317-4d408b98c500";
    	retrieveResult = mockMvc.perform(
                get("/api/v1/annotation-properties?orgId=" + longOrgId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", longOrgId)
        );
    }


    @Then("Verify Get Annotation Properties set data with long orgId Throws Exception")
    public void thenVerifyGetAnnotationPropertiesSetDataWithLongOrgIdThrowsException() throws Exception{
    	retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("[]")));
    }

    @Given("Get Annotation Properties set data - DataSetUp Provided")
    public void givenGetAnnotationPropertiesSetDataDataSetUpProvided() {
        AnnotationProperties annotationProperties =  setAnnotationProp();
        List<AnnotationProperties> annoPropList =  new ArrayList<AnnotationProperties>();
        annoPropList.add(annotationProperties);
        when(annotationPropRepository.findByOrgId(anyString())).thenReturn(annoPropList);
        //when(annotationPropRepository.findByOrgId(null)).thenReturn(null);
        //doThrow(new Exception()).when(annotationPropRepository).findByOrgId(anyString());
    }

    @When("Get Annotation Properties set data")
    public void whenGetAnnotationPropertiesSetData() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation-properties?orgId=12345678-abcd-42ca-a317-4d408b98c500")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );
    }

    @Then("Verify Get Annotation Properties set data")
    public void thenVerifyGetAnnotationPropertiesSetData() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString(AnnotationProp)));
    }

    @Given("Get Annotation Properties set data Throws Service Exception - DataSetUp Provided")
    public void givenGetAnnotationPropertiesSetDataThrowsServiceExceptionDataSetUpProvided() {
        when(annotationPropRepository.findByOrgId(null)).thenThrow(ServiceException.class);
    }

    @When("Get Annotation Properties set data - Throws Service Exception")
    public void whenGetAnnotationPropertiesSetDataThrowsServiceException() {
        try{
        retrieveResult = mockMvc.perform(
                get("/api/v1/annotation-properties?orgId=12345678-abcd-42ca-a317-4d408b98c500")
                        .contentType(MediaType.APPLICATION_JSON)
        );}
        catch(Exception e){
            throwable = e;
        }
    }

    @Then("Verify Get Annotation Properties set data Throws Service Exception")
    public void thenVerifyGetAnnotationPropertiesSetDataThrowsServiceException() throws Exception {
        retrieveResult.andExpect(status().is(200));
        assert(throwable.toString().contains("Request processing failed"));

    }

    @Given("Get Annotation Properties set data Throws Exception - DataSetUp Provided")
    public void givenGetAnnotationPropertiesSetDataThrowsExceptionDataSetUpProvided() {
        when(annotationPropRepository.findByOrgId(null)).thenThrow(DataRetrievalFailureException.class);
    }

    @When("Get Annotation Properties set data - Throws Exception")
    public void whenGetAnnotationPropertiesSetDataThrowsException() {
        try{
            retrieveResult = mockMvc.perform(
                    get("/api/v1/annotation-properties?orgId=12345678-abcd-42ca-a317-4d408b98c500")
                            .contentType(MediaType.APPLICATION_JSON)
            );}
        catch(Exception e){
            throwable = e;
        }
    }

    @Then("Verify Get Annotation Properties set data Throws Exception")
    public void thenVerifyGetAnnotationPropertiesSetDataThrowsException() throws Exception {
        retrieveResult.andExpect(status().is(200));
        assert (throwable.toString().contains("Request processing failed"));
    }

    @Given("Post Annotation Properties set data Throws Exception - DataSetUp Provided")
    public void givenPostAnnotationPropertiesSetDataThrowsExceptionDataSetUpProvided() {
        AnnotationProperties annotationProperties =  setAnnotationProp();
        when(annotationPropRepository.save(any(AnnotationProperties.class))).thenThrow(DataRetrievalFailureException.class);

    }
    @When("Post Annotation Properties set data - Throws Exception")
    public void whenPostAnnotationPropertiesSetDataThrowsException() {
        AnnotationProperties annotationProperties =  setAnnotationProp();
        try{
        retrieveResult = mockMvc.perform(
                post("/api/v1/annotation-properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AnnotationPropertiesToJSON(annotationProperties))
                        .requestAttr("orgId", "12345678-abcd-42ca-a317-4d408b98c500")
        );}
        catch(Exception e){
            throwable = e;
        }
    }
    @Then("Verify Post Annotation Properties set data Throws Exception")
    public void thenVerifyPostAnnotationPropertiesSetDataThrowsException() throws Exception {
        retrieveResult.andExpect(status().is(200));
        retrieveResult.andExpect(content().string(containsString("FAILURE")));
    }

    private String AnnotationPropertiesToJSON(AnnotationProperties annotationProperties) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(annotationProperties);
    }

    private AnnotationProperties setAnnotationProp() {
        AnnotationProperties annotationProperties = new AnnotationProperties();
        annotationProperties.setClasses(null);
        annotationProperties.setCreatedBy("test");
        annotationProperties.setCreatedDate(commonSteps.getDate());
        annotationProperties.setId(1L);
        annotationProperties.setOrgId("12345678-abcd-42ca-a317-4d408b98c500");
        annotationProperties.setResourceName("TEST");
        annotationProperties.setSchemaVersion("123");
        return annotationProperties;
    }
}
