package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.entity.*;
import com.gehc.ai.app.datacatalog.entity.Properties;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.datacatalog.rest.response.ContractByDataSetId;
import com.gehc.ai.app.datacatalog.service.impl.DataCatalogServiceImpl;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.GEClass;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.AnnotationJson;
import com.gehc.ai.app.datacatalog.util.exportannotations.bean.json.LabelAnnotationJson;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component
public class RetrieveContractsSteps {

    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private DataCatalogDaoImpl dataCatalogDao;

    @Autowired
    public RetrieveContractsSteps(MockMvc mockMvc, DataCatalogDaoImpl dataCatalogDao ) {
        this.mockMvc = mockMvc;
        this.dataCatalogDao = dataCatalogDao;
    }

    //////////////////////
    //
    // GIVEN statements //
    //
    //////////////////////

    //given test cases for getContractsForDataCollection api
    @Given("a data collection/set ID supported by LF")
    public void givenDataSetIdSupportedByLF(){

        List<Contract> contractByDataSetIdList = new ArrayList<>();
        Contract contractByDataSetId = buildContractByDataSetId();
        contractByDataSetIdList.add(contractByDataSetId);

        Contract contractByDataSetId2 = buildContractByDataSetId();
        contractByDataSetId2.setActive("true");
        contractByDataSetIdList.add(contractByDataSetId2);

        List<Long> imageSetIdList = Arrays.asList(1293000012905L, 1293000012895L, 1293000012901L, 1293000012904L);
        when(dataCatalogDao.getImageSetIdListByDataSetId(anyLong())).thenReturn(imageSetIdList);
        when(dataCatalogDao.getContractsByImageSetidList(imageSetIdList)).thenReturn(contractByDataSetIdList);
    }

    @Given("a data collection/set ID not supported by LF")
    public void givenDataSetIdNotSupportedByLF(){
        when(dataCatalogDao.getImageSetIdListByDataSetId(anyLong())).thenReturn(new ArrayList<>());
    }

    /////////////////////
    //
    // WHEN statements //
    //
    /////////////////////

    //when test cases for getContractsForDataCollection api
    @When("the api that gets contracts associated with the image sets of that data collection")
    public void whenApiReturnsDataForDatSetId() throws Exception{
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/contract/data-collection/1"));
    }

    @When("the api that gets contracts associated with the image sets of that data collection is hit")
    public void whenApiReturnsDataForDatSetIdIsHit() throws Exception{
        retrieveResult = mockMvc.perform(
                get("/api/v1/datacatalog/contract/data-collection/12").contentType(MediaType.APPLICATION_JSON));
    }

    /////////////////////
    //
    // THEN statements //
    //
    /////////////////////

    //then test cases for getContractsForDataCollection api
    @Then("the api must return a map of active and inactive contracts associated with the data collection")
    public void thenResultShouldBeMapOfContractLists() throws Exception{
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("false")));
    }

    @Then("the api must return error message saying no contracts exist for the given dataSet ID")
    public void thenResultShouldBeAnErrorMessage() throws Exception{
        retrieveResult.andExpect(status().isBadRequest());
        retrieveResult.andExpect(content().string(containsString("No contracts exist for the given dataSet ID.")));
    }

    /////////////
    //
    // Helpers //
    //
    /////////////

    private Contract buildContractByDataSetId(){
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setAgreementName("Test contract name");
        contract.setPrimaryContactEmail("john.doe@ge.com");
        contract.setDeidStatus(Contract.DeidStatus.HIPAA_COMPLIANT);
        contract.setAgreementBeginDate("9999-06-08");
        contract.setDataUsagePeriod("12");
        contract.setUseCases(Arrays.asList(new ContractUseCase[]{new ContractUseCase(ContractUseCase.DataUser.GE_GLOBAL, ContractUseCase.DataUsage.TRAINING_AND_MODEL_DEVELOPMENT, "")}));
        contract.setActive("false");
        contract.setDataLocationAllowed(Contract.DataLocationAllowed.GLOBAL);
        contract.setUploadBy("user");
        contract.setUploadStatus(Contract.UploadStatus.UPLOAD_IN_PROGRESS);

        return contract;
    }

}



