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

    private final List<Double> xdir;

    private final List<Double> ydir;

    private final List<Double> origin;

    /**
     * Creates a new {@code BoundingBox} described by the provided origin, x-direction, and y-direction vectors.
     *
     * @param xdir   A 2D point that when considered with the provided origin forms the x-component vector
     * @param ydir   A 2D point that when considered with the provided origin forms the y-component vector
     * @param origin The point represented the top left vertex of the bounding box
     */
    public BoundingBox(@JsonProperty("xdir") List<Double> xdir, @JsonProperty("ydir") List<Double> ydir, @JsonProperty("origin") List<Double> origin) {
        this.xdir = Collections.unmodifiableList(Objects.requireNonNull(xdir));
        this.ydir = Collections.unmodifiableList(Objects.requireNonNull(ydir));
        this.origin = Collections.unmodifiableList(Objects.requireNonNull(origin));
    }

    public List<Double> getxdir() {
        return Collections.unmodifiableList(xdir);
    }

    public List<Double> getydir() {
        return Collections.unmodifiableList(ydir);
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

        if (!getxdir().equals(that.getxdir())) {
            return false;
        }
        if (!getydir().equals(that.getydir())) {
            return false;
        }
        return getOrigin().equals(that.getOrigin());
    }

    @Override
    public int hashCode() {
        int result = getxdir().hashCode();
        result = 31 * result + getydir().hashCode();
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
