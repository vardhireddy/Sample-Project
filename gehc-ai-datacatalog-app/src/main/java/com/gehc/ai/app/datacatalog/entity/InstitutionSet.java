/*
 * InstitutionSet.java
 *
 * Copyright (c) 2017 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.entity;

/**
 * Created by sowjanyanaidu on 1/3/18.
 */

public class InstitutionSet {
    private String[] seriesUIds;
    private String institution;

    public String[] getSeriesUIds() {
        return seriesUIds.clone();
    }

    public void setSeriesUIds(String[] seriesUIds) {

        this.seriesUIds = seriesUIds.clone();
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
