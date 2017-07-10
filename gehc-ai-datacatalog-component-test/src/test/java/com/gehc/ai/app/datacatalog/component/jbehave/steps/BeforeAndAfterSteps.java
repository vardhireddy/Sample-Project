package com.gehc.ai.app.datacatalog.component.jbehave.steps;


import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.ScenarioType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.reset;

@Component
public class BeforeAndAfterSteps {

    @MockBean
    private AnnotationRepository annotationRepository;

    @MockBean
    DataSetRepository dataSetRepository;

    @MockBean
    ImageSeriesRepository imageSeriesRepository;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void initStorageBeforeScenario() {
    }

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void initMocksBeforeScenario() {
        reset(annotationRepository);
        reset(dataSetRepository);
        reset(imageSeriesRepository);

    }

}