/**
 * 
 */
package com.amadeus.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.amadeus.docuMe.util.Trial;

/**
 * @author nghate
 *
 */
public class TrialTest {

	/**
	 * Test method for {@link com.amadeus.docuMe.util.Trial#helloWorld(java.lang.String)}.
	 */
	@Test
	public void testHelloWorld() {
		Trial t = new Trial();
		assertEquals("helloneha", t.helloWorld("neha"));
//		fail("Not yet implemented");
	}

}
