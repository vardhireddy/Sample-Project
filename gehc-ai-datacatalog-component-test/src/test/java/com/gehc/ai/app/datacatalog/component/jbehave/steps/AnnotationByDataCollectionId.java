package com.gehc.ai.app.datacatalog.component.jbehave.steps;

import com.gehc.ai.app.datacatalog.repository.AnnotationPropRepository;
import com.gehc.ai.app.datacatalog.repository.AnnotationRepository;
import com.gehc.ai.app.datacatalog.repository.ImageSeriesRepository;
import com.gehc.ai.app.datacatalog.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Created by sowjanyanaidu on 7/14/17.
 */
public class AnnotationByDataCollectionId {
    private final MockMvc mockMvc;

    private final PatientRepository patientRepository;
    private final ImageSeriesRepository imageSeriesRepository;
    private final AnnotationRepository annotationRepository;
    private final CommonSteps commonSteps;
    private final AnnotationPropRepository annotationPropRepository;
    private ResultActions retrieveResult;

    @Autowired
    public AnnotationByDataCollectionId(MockMvc mockMvc, AnnotationPropRepository annotationPropRepository, PatientRepository patientRepository, ImageSeriesRepository imageSeriesRepository,AnnotationRepository annotationRepository,CommonSteps commonSteps) {
        this.mockMvc = mockMvc;
        this.annotationPropRepository = annotationPropRepository;
        this.patientRepository = patientRepository;
        this.imageSeriesRepository = imageSeriesRepository;
        this.annotationRepository = annotationRepository;
        this.commonSteps = commonSteps;
    }
}
