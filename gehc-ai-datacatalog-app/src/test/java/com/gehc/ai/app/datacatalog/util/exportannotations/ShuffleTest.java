package com.gehc.ai.app.datacatalog.util.exportannotations;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

public class ShuffleTest {
	@Test
	public void testShuffle() {
		final int sz = 1000000;
		final int it = 100;
		final int [] ind = new int[sz];
		
		for (int k = 0; k < it; k++) {
			System.out.println("it " + k);
			long tt = System.currentTimeMillis();
			Shuffle.shuffle(sz,sz,ind);
			System.out.println("shuffle: " + (System.currentTimeMillis()-tt) + "ms");

			assertTrue(!testDuplicates(ind));
			assertTrue(testRange(ind, sz));
		}
		
		/** test supplying a null sized return container **/
		int [] ind1 = Shuffle.shuffle(10, 10, null);
		assertTrue(!testDuplicates(ind1));
		assertTrue(testRange(ind1, 10));
		
		/** test supplying a container with the same size */
		Shuffle.shuffle(10, 10, ind1);
		assertTrue(!testDuplicates(ind1));
		assertTrue(testRange(ind1, 10));
		
		/** a partial shuffle w/ null container */
		ind1 = Shuffle.shuffle(10, 6, null);
		assertTrue(!testDuplicates(ind1));
		assertTrue(testRange(ind1, 10));
		
		/** a partial shuffle w/ container of the same size */
		Shuffle.shuffle(10, 6, ind1);
		assertTrue(!testDuplicates(ind1));
		assertTrue(testRange(ind1, 10));	
	}
	
	private boolean testRange(int [] ind, int max) {
		for (int k = 0; k < ind.length; k++) {
			if (ind[k] < 0 || ind[k] > max)
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
}
