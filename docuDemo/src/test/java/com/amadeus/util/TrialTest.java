/**
 * 
 */
package com.amadeus.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author nghate
 *
 */
public class TrialTest {

	/**
	 * Test method for {@link com.amadeus.util.Trial#helloWorld(java.lang.String)}.
	 */
	@Test
	public void testHelloWorld() {
		Trial t = new Trial();
		assertEquals("helloneha", t.helloWorld("neh.la"));
//		fail("Not yet implemented");
	}

}
