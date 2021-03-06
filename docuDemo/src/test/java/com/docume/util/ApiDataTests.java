/**
 * 
 */
package com.docume.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.docume.pojo.Entity;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * @author nghate
 *
 */
public class ApiDataTests {
	ClassLoader classLoader = getClass().getClassLoader();
	File file = new File(classLoader.getResource("swg.yml").getFile());
	Swagger swagger = null;
	List<String> operationParamList = new ArrayList<>();
	
	/**
	 * Test method for
	 * {@link com.amadeus.docuMe.util.ApiData#createApiData(io.swagger.models.Swagger, java.lang.String)}.
	 */

	@Before
	public void createSwagger() {
		if (file.exists()) {
			swagger = new SwaggerParser().read("swg.yml");
		}

	}

	@Test
	public void testCreateApiData() {

		ApiData apiDataObj = new ApiData();
		List<Entity> apiEntityList = apiDataObj.createApiData(swagger, false);

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
		assertEquals(expectedList, actualList);
		
		int airportAutocompleteIndex = 0;
		String apiAirportAutocompleteHtml = apiEntityList.get(airportAutocompleteIndex).getEntityHtmlPage();
		assertThat(apiAirportAutocompleteHtml, containsString("Airport Autocomplete"));
		assertThat(apiAirportAutocompleteHtml,containsString("country"));
		assertThat(apiAirportAutocompleteHtml,containsString("https://api.sandbox.amadeus.com/v1.2/airports/autocomplete"));
		assertThat(apiAirportAutocompleteHtml,containsString("Flight, Reference"));
		assertThat(apiAirportAutocompleteHtml,containsString("AirportAutocompleteResponse"));
		
	}

}
