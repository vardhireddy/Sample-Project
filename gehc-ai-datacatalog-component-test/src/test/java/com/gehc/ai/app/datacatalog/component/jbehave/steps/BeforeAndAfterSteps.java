package com.gehc.ai.app.datacatalog.component.jbehave.steps;


import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.ScenarioType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import static org.mockito.Mockito.reset;

@Component
public class BeforeAndAfterSteps {

    @MockBean
    private AnnotationRepository annotationRepository;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void initStorageBeforeScenario() {
    }

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void initMocksBeforeScenario() {
        reset(annotationRepository);

    }

}