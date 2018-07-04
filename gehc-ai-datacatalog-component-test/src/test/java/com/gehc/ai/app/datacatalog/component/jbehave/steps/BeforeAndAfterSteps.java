package com.gehc.ai.app.datacatalog.component.jbehave.steps;


import static org.mockito.Mockito.reset;

import com.gehc.ai.app.datacatalog.repository.*;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.ScenarioType;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.service.IRemoteService;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;

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
    UploadRepository uploadRepository;

    @MockBean
    DataCatalogInterceptor dataCatalogInterceptor;

    @MockBean
    DataCatalogDaoImpl dataCatalogDao;
    
    @MockBean
    IRemoteService remoteServiceImpl;

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
        reset(remoteServiceImpl);
        reset(uploadRepository);
    }

}