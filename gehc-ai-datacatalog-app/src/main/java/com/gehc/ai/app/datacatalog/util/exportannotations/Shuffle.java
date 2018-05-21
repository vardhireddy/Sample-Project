/*
 * Shuffle.java
 *
 * Copyright (c) 2018 by General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.gehc.ai.app.datacatalog.util.exportannotations;

/**
 * utility functions to generate shuffled list index 
 * @author litaoyan
 *
 */
public class Shuffle {
	/**
	 * 
	 * @param n total # of objects
	 * @param m # of objects to be picked
	 * @param indices array to store 
	 * @return indices of randomized result
	 */
	public static final int [] shuffle(int n, int m, int [] indices) {
		int [] tempIndices;
		if (indices == null || indices.length < n) {
			tempIndices = new int[n];
		} else
			tempIndices = indices;
		
		for (int k = 0; k < n; k++)
			tempIndices[k] = k;
		
		final int cnt = Math.min(m, n-1);
		for (int k = 0; k < cnt; k++) {
			int j = (int) (k + Math.random() * (n-k));
			int t = tempIndices[k];
			tempIndices[k] = tempIndices[j];
			tempIndices[j] = t;
		}
		
		if (indices == null || indices.length < m)
			indices = new int[m];
		if (tempIndices != indices) {
			System.arraycopy(tempIndices, 0, indices, 0, m);
		}
		return indices;
	}
}
