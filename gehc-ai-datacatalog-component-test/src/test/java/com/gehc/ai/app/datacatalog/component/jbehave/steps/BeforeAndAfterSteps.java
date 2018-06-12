package com.gehc.ai.app.datacatalog.component.jbehave.steps;


<<<<<<< HEAD
import static org.mockito.Mockito.reset;

=======
import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.repository.*;
import com.gehc.ai.app.datacatalog.service.IDataCatalogService;
import com.gehc.ai.app.datacatalog.service.impl.DataCatalogServiceImpl;
import com.gehc.ai.app.interceptor.DataCatalogInterceptor;
>>>>>>> 0d1996310382e2816029fc6bfc1c2df950d0acb2
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.ScenarioType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import com.gehc.ai.app.datacatalog.dao.impl.DataCatalogDaoImpl;
import com.gehc.ai.app.datacatalog.repository.AnnotationPropRepository;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.COSNotificationRepository;
import com.gehc.ai.app.datacatalog.repository.ContractRepository;
import com.gehc.ai.app.datacatalog.repository.DataSetRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import com.gehc.ai.app.datacatalog.repository.StudyRepository;
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
    }

}