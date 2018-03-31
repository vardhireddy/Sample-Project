/*
 * BoundingCube.java
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
 * {@code BoundingCube} is bean that represents an ROI formed by a bounding cube (e.g. a box or ellipsoid).
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public final class BoundingCube extends BoundingBox {

    private final List<Double> zdir;

    /**
     * Creates a new {@code BoundingCube} described by the provided origin, x-direction, y-direction, and z-direction vectors.
     *
     * @param xdir   A 3D point that when considered with the provided origin forms the x-component vector
     * @param ydir   A 3D point that when considered with the provided origin forms the y-component vector
     * @param zdir   A 3D point that when considered with the provided origin forms the z-component vector
     * @param origin The point represented the top left vertex of the bounding box
     */
    public BoundingCube(@JsonProperty("xdir") List<Double> xdir, @JsonProperty("ydir") List<Double> ydir, @JsonProperty("zdir") List<Double> zdir, @JsonProperty("origin") List<Double> origin) {
        super(xdir, ydir, origin);
        this.zdir = Collections.unmodifiableList(Objects.requireNonNull(zdir));
    }

    public List<Double> getzdir() {
        return Collections.unmodifiableList(zdir);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null) {return false;}
        if (!(o instanceof BoundingCube)) {return false;}

        BoundingCube that = (BoundingCube) o;

        if (!that.canEqual(this)) {
            return false;
        }

        if (!super.equals(that)) {
            return false;
        }

        return zdir.equals(that.zdir);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + zdir.hashCode();
        return result;
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof BoundingCube;
    }
}
