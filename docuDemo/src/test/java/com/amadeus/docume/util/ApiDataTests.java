/**
 * 
 */
package com.amadeus.docume.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.amadeus.docume.pojo.Entity;
import com.amadeus.docume.util.ApiData;
import com.amadeus.docume.util.FileUtil;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * @author nghate
 *
 */
public class ApiDataTests {
	String swaggerFile = "d:\\Userfiles\\nghate\\Desktop\\swg.yml";
	File file = new File(swaggerFile);
	List<String> operationParamList = new ArrayList<>();

	/**
	 * Test method for
	 * {@link com.amadeus.docuMe.util.ApiData#createApiData(io.swagger.models.Swagger, java.lang.String)}.
	 */
	@Test
	public void testCreateApiData() {
		Swagger swaggerObj = null;
		if (file.exists()) {
			swaggerObj = new SwaggerParser().read(swaggerFile);
		}
		ApiData apiDataObj = new ApiData();
		List<Entity> apiEntityList = apiDataObj.createApiData(swaggerObj, false);
		List<String> actualList = new ArrayList<>();
		List<String> expectedList = new ArrayList<>();
		expectedList.add("Airport Autocomplete");
		expectedList.add("Nearest Relevant Airport");
		expectedList.add("Car Rental Airport Search");
		expectedList.add("Car Rental Geosearch");
		expectedList.add("Flight Affiliate Search");
		expectedList.add("Flight Extensive Search");
		expectedList.add("Flight Inspiration Search");
		expectedList.add("Flight Low-Fare Search");
		expectedList.add("Hotel Airport Search");
		expectedList.add("Hotel Geosearch by box");
		expectedList.add("Hotel Geosearch by circle");
		expectedList.add("Hotel Property Code Search");
		expectedList.add("Location Information");
		expectedList.add("YapQ Geosearch");
		expectedList.add("YapQ City Name Search");
		expectedList.add("Rail-Station Information");
		expectedList.add("Rail-Station Autocomplete");
		expectedList.add("Train Extensive Search");
		expectedList.add("Train Schedule Search");
		expectedList.add("Flight Traffic API");
		expectedList.add("Top Flight Destinations");
		expectedList.add("Top Flight Searches");
		expectedList.add("Travel Record Retrieve");
		
		
		for (Entity entity : apiEntityList) {
			actualList.add(entity.getEntityTitle());
		}
		assertArrayEquals(expectedList.toArray(), actualList.toArray());
//		fail("Not yet implemented");
	}


}
