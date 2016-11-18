/**
 * 
 */
package com.amadeus.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.amadeus.docuMe.util.ApiData;
import com.amadeus.docuMe.util.Documentation;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * @author nghate
 *
 */
public class ApiDataTests {
	String swaggerFile = "d:\\Userfiles\\nghate\\Desktop\\hotelapi.yml";
	File file = new File(swaggerFile);
	List<String> operationParamList = new ArrayList<>();
	/**
	 * Test method for {@link com.amadeus.docuMe.util.ApiData#createApiData(io.swagger.models.Swagger, java.lang.String)}.
	 */
	@Test
	public void testCreateApiData() {
		Swagger swaggerObj = null;
		if (file.exists()) {
			swaggerObj = new SwaggerParser().read(swaggerFile);
		}
	ApiData apiDataObj = new ApiData();
		Map<String, String> apiData = apiDataObj.createApiData(swaggerObj, "false");
		for (Map.Entry<String, String> pathDetail : apiData.entrySet()) {
			String str = pathDetail.getKey();
			assertEquals("Get_hotellist_city.html", str);
			
				
		}
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.amadeus.docuMe.util.ApiData#getReponsesMap(io.swagger.models.Operation)}.
	 */
	@Test
	public void testGetReponsesMap() {
		fail("Not yet implemented");
	}

}
