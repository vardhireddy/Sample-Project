/*
 *  FreeformRoi.java
 *
 *  Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 *  The copyright to the computer software herein is the property of
 *  General Electric Company. The software may be used and/or copied only
 *  with the written permission of General Electric Company or in accordance
 *  with the terms and conditions stipulated in the agreement/contract
 *  under which the software has been supplied.
 */
package com.gehc.ai.app.datacatalog.util.exportannotations.bean.json;

/**
 * A {@code FreeformRoi} is a type of ROI that is formed by multiple vertices which have no inherent spatial order.
 *
 * @author andrew.c.wong@ge.com (212069153)
 */
public enum FreeformRoi {
    POLYGON, CONTOUR
}
