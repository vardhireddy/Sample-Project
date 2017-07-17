package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.entity.CosNotification;
import com.gehc.ai.app.datacatalog.repository.COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Matchers.any;
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
    private ResultActions retrieveResult;

    @Autowired
    public CosNotificationSteps(MockMvc mockMvc, COSNotificationRepository cosNotificationRepository) {
        this.mockMvc = mockMvc;
        this.cosNotificationRepository = cosNotificationRepository;

    }

    @Given("Store Cos Notification in DataCatalog - DataSetUp Provided")
    public void givenStoreCosNotificationInDataCatalogDataSetUpProvided() {
        CosNotification cosNotification = new CosNotification();
        cosNotification.setId(1L);
        cosNotification.setMessage("I am a Test");
        when(cosNotificationRepository.save(any(CosNotification.class))).thenReturn(cosNotification);
    }
    @When("Store Cos Notification in DataCatalog")
    public void whenStoreCosNotificationInDataCatalog() throws Exception {
        retrieveResult = mockMvc.perform(
                post("/api/v1/datacatalog/cos-notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("orgId", "12")
        );

    }
    @Then("Verify Store Cos Notification in DataCatalog")
    public void thenVerifyStoreCosNotificationInDataCatalog() throws Exception {
        retrieveResult.andExpect(status().isOk());
        retrieveResult.andExpect(content().string(containsString("{\"status\":\"SUCCESS\",\"code\":\"OK\",\"message\":\"SUCCESS\",\"id\":\"1\"}")));

    }


}