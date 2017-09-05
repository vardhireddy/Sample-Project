package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Created by sowjanyanaidu on 9/5/17.
 */
@Component
public class ViewerSteps {
    private final MockMvc mockMvc;
    private final StudyRepository studyRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final CommonSteps commonSteps;
    private final DataCatalogInterceptor dataCatalogInterceptor;
    private ResultActions retrieveResult;
    @Autowired
    public ViewerSteps(MockMvc mockMvc, StudyRepository studyRepository, ImageSeriesRepository imageSeriesRepository, CommonSteps commonSteps, DataCatalogInterceptor dataCatalogInterceptor) {
        this.mockMvc = mockMvc;
        this.studyRepository = studyRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.commonSteps = commonSteps;
        this.dataCatalogInterceptor =dataCatalogInterceptor;
    }
    @Given("Get Segmentation Results image - DataSetUp Provided")
    public void givenGetSegmentationResultsImageDataSetUpProvided() {
    }

    @When("Get Segmentation Results image")
    public void whenGetSegmentationResultsImage() {
    }

    @Then("verify Segmentation Results image")
    public void thenVerifySegmentationResultsImage() {
    }
}
