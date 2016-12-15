/**
 * 
 */
package com.docume.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.docume.pojo.ModelDetail;
import com.docume.pojo.MyModel;

import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * @author nghate
 *
 */
public class ResponseModelDataTest {

	ClassLoader classLoader = getClass().getClassLoader();
	File file = new File(classLoader.getResource("swg.yml").getFile());
	Swagger swagger = null;
	
	@Before
	public void createSwagger() {
		if (file.exists()) {
			swagger = new SwaggerParser().read("swg.yml");
		}

	}

	/**
	 * Test method for {@link com.amadeus.docume.util.ResponseModelData#createModelList(io.swagger.models.Swagger)}.
	 */
	@Test
	public void testCreateModelList() {
		ResponseModelData responseModelData = new ResponseModelData();
		List<ModelDetail> modelList = responseModelData.createModelList(swagger);
		Set<String> expectedTitles = swagger.getDefinitions().keySet();
		
		for(ModelDetail md : modelList){
			String actualTitle = md.getModelTitle();
			assertTrue(expectedTitles.contains(actualTitle));
		}
		
		 // Unit test for  ExtremeSearchResult with string parameter
		ModelDetail modelDetail = modelList.get(2); // def 2 as const
		// Extracting element 'destination'
		MyModel model = modelDetail.getModelList().get(0); //model list to param list
		assertEquals("destination", model.getName());
		//contains for desc
		assertEquals("The <a href=\"https://en.wikipedia.org/wiki/International_Air_Transport_Association_airport_code\">IATA code</a> of the city or airport to which the traveler may go, from the provided origin", model.getDescription());
		assertEquals("string", model.getType());
		
		// Unit test for  ExtremeSearchResult with array[references] parameter
		ModelDetail modelDetailForLowFareSearch = modelList.get(3);
		// Extracting element 'results'
		MyModel modelForResults = modelDetailForLowFareSearch.getModelList().get(1); 
		assertEquals("results", modelForResults.getName());
		assertEquals("array", modelForResults.getType());
		assertEquals("LowFareSearchResult", modelForResults.getReferenceModel());
	}


}
