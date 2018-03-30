/*
 * BoundingBox.java
 *
 * Copyright (c) 2016 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.util.exportannotations.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * {@code BoundingBox} is bean that represents an ROI formed by a bounding box (e.g. a rectangle or ellipse).
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public class BoundingBox {

    private final List<Double> xDir;

    private final List<Double> yDir;

    private final List<Double> origin;

    /**
     * Creates a new {@code BoundingBox} described by the provided origin, x-direction, and y-direction vectors.
     *
     * @param xDir   A 2D point that when considered with the provided origin forms the x-component vector
     * @param yDir   A 2D point that when considered with the provided origin forms the y-component vector
     * @param origin The point represented the top left vertex of the bounding box
     */
    public BoundingBox(@JsonProperty("xDir") List<Double> xDir, @JsonProperty("yDir") List<Double> yDir, @JsonProperty("origin") List<Double> origin) {
        this.xDir = Collections.unmodifiableList(Objects.requireNonNull(xDir));
        this.yDir = Collections.unmodifiableList(Objects.requireNonNull(yDir));
        this.origin = Collections.unmodifiableList(Objects.requireNonNull(origin));
    }

    public List<Double> getxDir() {
        return Collections.unmodifiableList(xDir);
    }

    public List<Double> getyDir() {
        return Collections.unmodifiableList(yDir);
    }

    public List<Double> getOrigin() {
        return Collections.unmodifiableList(origin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof BoundingBox)) {
            return false;
        }

        BoundingBox that = (BoundingBox) o;
        if (!that.canEqual(this)) {
            return false;
        }

        if (!getxDir().equals(that.getxDir())) {
            return false;
        }
        if (!getyDir().equals(that.getyDir())) {
            return false;
        }
        return getOrigin().equals(that.getOrigin());
    }

    @Override
    public int hashCode() {
        int result = getxDir().hashCode();
        result = 31 * result + getyDir().hashCode();
        result = 31 * result + getOrigin().hashCode();
        return result;
    }

    /**
     * Returns whether if it is possible for provided object to potentially equal an instance of {@code BoundingBox}.
     *
     * @param other The object} to evaluate
     * @return {@code true} if it possible for the provided object to potentially equal an instance of {@code BoundingBox}; otherwise, {@code false}
     */
    public boolean canEqual(Object other) {
        return other instanceof BoundingBox;
    }
}
