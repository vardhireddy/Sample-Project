package com.gehc.ai.app.datacatalog.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by sowjanyanaidu on 1/3/18.
 */

public class InstitutionSet {
    private List<String> seriesUIds;
    private String institution;
    public List<String> getSeriesUIds() {
        return seriesUIds;
    }

    public void setSeriesUIds(List<String> seriesUIds) {
        this.seriesUIds = seriesUIds;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    @Override
    public String toString() {
        return "InstitutionSet{" +
                ", institution='" + institution + '\'' +
                ", seriesUIds='" + seriesUIds + '\'' +
                '}';
    }

}
