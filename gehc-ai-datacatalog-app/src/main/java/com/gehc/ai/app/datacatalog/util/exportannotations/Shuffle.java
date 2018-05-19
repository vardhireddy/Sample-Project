package com.gehc.ai.app.datacatalog.util.exportannotations;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class Shuffle {
	/**
	 * 
	 * @param n total # of objects
	 * @param m # of objects to be picked
	 * @param indices array to store 
	 * @return
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
	
	@Test
	public void testShuffle() {
		final int sz = 1000000;
		final int it = 100;
		final int [] ind = new int[sz];
		
		for (int k = 0; k < it; k++) {
			System.out.println("it " + k);
			long tt = System.currentTimeMillis();
			shuffle(sz,sz,ind);
			System.out.println("shuffle: " + (System.currentTimeMillis()-tt) + "ms");

			assertTrue(!testDuplicates(ind));
			assertTrue(testRange(ind));
		}
	}
	
	private boolean testRange(int [] ind) {
		for (int k = 0; k < ind.length; k++) {
			if (ind[k] < 0 || ind[k] > ind.length)
				return false;
		}
		return true;
	}
	
	private boolean testDuplicates(int [] a) {
		long t = System.currentTimeMillis();
		HashSet<Integer> s = new HashSet<Integer>();
		for (int k = 0; k < a.length; k++) {
			if (s.contains(k))
				return true;
			s.add(k);
		}
		System.out.println("test dup: " + (System.currentTimeMillis()-t) + "ms");
		return false;
	}
	public static void main(String [] args) {
		long t = System.currentTimeMillis();
		int sz = 1000000;
		int [] inds = shuffle(sz, sz, null);
		t = System.currentTimeMillis() - t;
		System.out.println("shuffling " + sz + " elements takes " + t + "ms");
		
		inds = shuffle(10, 10, null);
		System.out.println(Arrays.toString(inds));
		
		inds = shuffle(10, 6, null);
		System.out.println(Arrays.toString(inds));
	}
}
