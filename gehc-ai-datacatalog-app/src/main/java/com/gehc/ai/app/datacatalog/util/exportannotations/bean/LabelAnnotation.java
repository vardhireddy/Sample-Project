/*
 *  LabelAnnotation.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@code LabelAnnotation} is a bean representing a label annotation as CSV.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class LabelAnnotation extends Annotation {

    private static final String[] REQUIRED_COLUMNS = new String[]{"label"};

    private String label;

    private String severity;

    private String findings;

    private String indication;

    /**
     * Creates a new {@code LabelAnnotation} that is associated with a DICOM image set.
     *
     * @param seriesUID           (Required) The series instance UID of the DICOM data to which this {@code LabelAnnotation} is associated.
     * @param annotationTypeAsStr (Required) The string representation of the label annotation type
     * @param label               (Required) The label value (e.g. "Pneumothorax")
     * @param severity            (Optional) The severity of the specified label.  For example, if the label is 'Pneumothorax', then the severity could be 'TRACE'.
     * @param indication          (Optional) The indication associated with the specified label.  For example, if the label is 'Pneumothorax', then an indication could be 'Motor Vehicle Accident'.
     * @param findings            (Optional) The findings associated with the specified label such as a radiologist's notes
     */
    public LabelAnnotation(String seriesUID, String annotationTypeAsStr, String label, String severity, String indication, String findings) {
        super(seriesUID, annotationTypeAsStr);
        setUp(label, severity, indication, findings);
    }

    /**
     * Creates a new {@code LabelAnnotation} that is associated with a non-DICOM image set.
     *
     * @param fileName            (Required) The original file name of the non-DICOM image to which this {@code LabelAnnotation} is associated
     * @param spaceID             (Required) The S3 space ID where the non-DICOM image is stored
     * @param annotationTypeAsStr (Required) The string representation of the label annotation type
     * @param label               (Required) The label value (e.g. "Pneumothorax")
     * @param severity            (Optional) The severity of the specified label.  For example, if the label is 'Pneumothorax', then the severity could be 'TRACE'.
     * @param indication          (Optional) The indication associated with the specified label.  For example, if the label is 'Pneumothorax', then an indication could be 'Motor Vehicle Accident'.
     * @param findings            (Optional) The findings associated with the specified label such as a radiologist's notes
     */
    public LabelAnnotation(String fileName, String spaceID, String annotationTypeAsStr, String label, String severity, String findings, String indication) {
        super(fileName, spaceID, annotationTypeAsStr);
        setUp(label, severity, indication, findings);
    }

    /**
     * Initializes instance fields that are specific to the {@code LabelAnnotation}.
     *
     * @param label      (Required) The label value
     * @param severity   (Optional) The severity of the specified label
     * @param indication (Optional) The indication associated with the specified label
     * @param findings   (Optional) The findings associated with the specified label
     */
    private void setUp(String label, String severity, String indication, String findings) {
        this.label = Objects.requireNonNull(label);
        this.severity = severity;
        this.findings = findings;
        this.indication = indication;
    }

    /////////////////////////
    //
    // Getters and setters //
    //
    /////////////////////////

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    //////////////////////////////////////////////////
    //
    // APIs for defining CSV column set for a label //
    //
    //////////////////////////////////////////////////

    @Override
    public Set<String> getRequiredDicomColumns() {
        return getRequiredColumns(super.getRequiredDicomColumns());
    }

    @Override
    public Set<String> getRequiredNonDicomColumns() {
        return getRequiredColumns(super.getRequiredNonDicomColumns());
    }

    @Override
    public Set<String> getOptionalColumnsWithValues() {
        Map<String, String> optionalColumnValues = new HashMap<>();
        optionalColumnValues.put("severity", getSeverity());
        optionalColumnValues.put("indication", getIndication());
        optionalColumnValues.put("findings", getFindings());
        return optionalColumnValues.entrySet().stream().filter(entry -> entry.getValue() != null).map(nonNullEntry -> nonNullEntry.getKey()).collect(Collectors.toSet());
    }

    /**
     * Returns the set of columns required by this {@code LabelAnnotation} and its parent.
     *
     * @param requiredParentColumns The columns required by this {@code LabelAnnotation}'s parent.
     * @return a Set<String>
     */
    private Set<String> getRequiredColumns(Set<String> requiredParentColumns) {
        Set<String> requiredDicomColumns = new LinkedHashSet<>();
        requiredDicomColumns.addAll(requiredParentColumns);
        requiredDicomColumns.add("label");
        return requiredDicomColumns;
    }

}
