package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.entity.ImageSet;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.ws.rs.core.MediaType;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component
public class DataCatalogSteps {

    private final DataSetRepository dataSetRepository;
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;
    private PreparedStatementSetter ps;
    private RowMapper rm;

    @Autowired
    public DataCatalogSteps(MockMvc mockMvc, AnnotationRepository annotationRepository, DataSetRepository dataSetRepository) {
        this.mockMvc = mockMvc;
        this.annotationRepository = annotationRepository;
        this.dataSetRepository = dataSetRepository;

    }


    @Given("datacatalog health check")
    public void datacatalogHealthCheck() throws Exception {
        retrieveResult = mockMvc.perform(
                get("/api/v1/dataCatalog/healthCheck")
        );
    }

    @Then("verify success")
    public void verifySuccess() throws Exception {
        retrieveResult.andExpect(status().isOk());
    }

    @When("Get data collection details by its id")
    public void getdataSet() throws Exception {
        dataSetSetUpForId();
        retrieveResult = mockMvc.perform(
                get("/api/v1//datacatalog/data-set/1234567")
                        .content(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection details by its id")
    public void verifyGetDatSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }


    @When("Get data collection details by its type")
    public void getdataSetByType() throws Exception {
        dataSetSetUpForType();
        retrieveResult = mockMvc.perform(
                get("/api/v1//datacatalog/data-set/type/Annotation")
                        .content(MediaType.APPLICATION_JSON)
                        .param("org-id", "12")
        );

    }

    @Then("verify data collection details by its type")
    public void verifyGetDatSetByType() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

    @When("save DataSet")
    public void saveDataSet() throws Exception {

        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        dataSet.setDescription("test");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateInString = "22-01-2017 10:20:56";
        Date date = new Date();
        try {
            date = sdf.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dataSet.setCreatedDate(dateInString);
        dataSet.setImageSets(new ImageSet());
        dataSet.setName("Test");
        dataSet.setProperties(new Properties());

        dataSet.setOrgId("123");
        dataSet.setSchemaVersion("123");
        dataSet.setType("Annotation");
        when(dataSetRepository.save(any(DataSet.class))).thenReturn(dataSet);
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/data-set")
                        .content(MediaType.APPLICATION_JSON)
                        .content(defnToJSON(dataSet))
                        .param("org-id", "12")

        );

    }

    @Then("verify Saving DataSet")
    public void verifySaveDatSet() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"id\":1,\"createdBy\":\"test\"}")));
    }

    private String defnToJSON(DataSet dataSet) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dataSet);
    }

    private List<DataSet> getDataSets() {
        List<DataSet> dataSets = new ArrayList<DataSet>();
        DataSet dataSet = new DataSet();
        dataSet.setId(1L);
        dataSet.setCreatedBy("test");
        dataSets.add(dataSet);
        return dataSets;
    }

    private void dataSetSetUpForId() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByIdAndOrgId(anyLong(), anyString())).thenReturn(dataSets);
    }

    private void dataSetSetUpForType() {
        List<DataSet> dataSets = getDataSets();
        when(dataSetRepository.findByTypeAndOrgId(anyString(), anyString())).thenReturn(dataSets);
    }
}



