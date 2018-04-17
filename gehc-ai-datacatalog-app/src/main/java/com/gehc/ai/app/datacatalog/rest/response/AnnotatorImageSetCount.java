/*
 * AnnotatorImageSetCount.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.rest.response;

public class AnnotatorImageSetCount {

    private String annotatorId;
    private int countfImagesAnnotated;

    public AnnotatorImageSetCount(String annotatorId, int countfImagesAnnotated) {
        this.annotatorId = annotatorId;
        this.countfImagesAnnotated = countfImagesAnnotated;
    }

    public AnnotatorImageSetCount() {
    }

    public String getAnnotatorId() {
        return annotatorId;
    }

    public void setAnnotatorId(String annotatorId) {
        this.annotatorId = annotatorId;
    }

    public int getCountfImagesAnnotated() {
        return countfImagesAnnotated;
    }

    public void setCountfImagesAnnotated(int countfImagesAnnotated) {
        this.countfImagesAnnotated = countfImagesAnnotated;
    }
}
