package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Component
public class DataCatalogSteps {
    private MockMvc mockMvc;
    private ResultActions retrieveResult;
    private AnnotationRepository annotationRepository;

    @Autowired
    public DataCatalogSteps(MockMvc mockMvc, AnnotationRepository annotationRepository){
        this.mockMvc = mockMvc;
        this.annotationRepository =annotationRepository;

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


}



