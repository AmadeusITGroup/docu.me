/**
 * 
 */
package com.docume.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author nghate
 *
 */
public class FileUtilTest {

	/**
	 * Test method for {@link com.docume.util.FileUtil#toBoolean(java.lang.String)}.
	 */
	@Test
	public void testToBooleanWithSpace() {
		boolean result = FileUtil.toBoolean(" true");
		assertEquals(true, result);
		
	}

	@Test
	public void testToBooleanWithUpperCase() {
		boolean result = FileUtil.toBoolean("FALSE");
		assertEquals(false, result);
		
	}
}
