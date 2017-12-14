package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.entity.DataSet;
import com.gehc.ai.app.datacatalog.repository.COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sowjanyanaidu on 7/17/17.
 */
@Component
public class CosNotificationSteps {
    private final MockMvc mockMvc;
    private final COSNotificationRepository cosNotificationRepository;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private ResultActions retrieveResult;

    @Autowired
    public CosNotificationSteps(MockMvc mockMvc, COSNotificationRepository cosNotificationRepository, DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.cosNotificationRepository = cosNotificationRepository;
        this.dataCatalogInterceptor = dataCatalogInterceptor;
    }

    @BeforeScenario
    public void setUp() throws Exception {
        when(dataCatalogInterceptor.preHandle(any(HttpServletRequest.class),any(HttpServletResponse.class),anyObject())).thenReturn(true);
    }

    @Given("Store Cos Notification in DataCatalog - DataSetUp Provided")
    public void givenStoreCosNotificationInDataCatalogDataSetUpProvided() {
        CosNotification cosNotification = getCosNotification();
        when(cosNotificationRepository.save(any(CosNotification.class))).thenReturn(cosNotification);
    }

    private CosNotification getCosNotification() {
        CosNotification cosNotification = new CosNotification();
        cosNotification.setId(1L);
        cosNotification.setMessage("{\"message\":\"cosNotiication\"}");
        cosNotification.setOrgId("123");
        return cosNotification;
    }

    @When("Store Cos Notification in DataCatalog")
    public void whenStoreCosNotificationInDataCatalog() throws Exception {
        CosNotification cosNotification = getCosNotification();
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/cos-notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(defnToCosNJSON(cosNotification))
                        .requestAttr("orgId", "12")

        );

    }
    @Then("Verify Store Cos Notification in DataCatalog")
    public void thenVerifyStoreCosNotificationInDataCatalog() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(""));

    }

    private String defnToCosNJSON(CosNotification cosNotification) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(cosNotification);
    }


}