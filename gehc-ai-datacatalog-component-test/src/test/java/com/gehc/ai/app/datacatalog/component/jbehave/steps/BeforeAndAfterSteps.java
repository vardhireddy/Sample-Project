package com.gehc.ai.app.datacatalog.component.jbehave.steps;


import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.repository.*;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.ScenarioType;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.mockito.Mockito.reset;

@Component
public class BeforeAndAfterSteps {

    @MockBean
    private AnnotationRepository annotationRepository;

    @MockBean
    private ContractRepository contractRepository;

    @MockBean
    DataSetRepository dataSetRepository;

    @MockBean
    ImageSeriesRepository imageSeriesRepository;

    @MockBean
    PatientRepository patientRepository;

    @MockBean
    StudyRepository studyRepository;

    @MockBean
    AnnotationPropRepository annotationPropRepository;

    @MockBean
    COSNotificationRepository cosNotificationRepository;

    @MockBean
    DataCatalogInterceptor dataCatalogInterceptor;

    @MockBean
    DataCatalogDaoImpl dataCatalogDao;

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void initStorageBeforeScenario() {
    }

    @BeforeScenario(uponType = ScenarioType.ANY)
    public void initMocksBeforeScenario() {
        reset(annotationRepository);
        reset(dataSetRepository);
        reset(imageSeriesRepository);
        reset(patientRepository);
        reset(studyRepository);
        reset(annotationPropRepository);
        reset(cosNotificationRepository);
        reset(dataCatalogInterceptor);
        reset(dataCatalogDao);
        reset(contractRepository);
    }

}