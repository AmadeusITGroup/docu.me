/**
 * 
 */
package com.docume.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import com.docume.pojo.ModelDetail;
import com.docume.pojo.ModelParameter;

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
	int extremeSearchResultIndex = 2;
	int destinationModelParamIndex= 0;
	int lowFareSearchIndex = 3;
	int resultsModelParamIndex = 1;
	
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
		ModelDetail modelDetail = modelList.get(extremeSearchResultIndex); 
		// Extracting element 'destination'
		ModelParameter modelParameterForExtremeSearchResult = modelDetail.getModelParameterList().get(destinationModelParamIndex); //model list to param list
		assertEquals("destination", modelParameterForExtremeSearchResult.getName());
		assertThat(modelParameterForExtremeSearchResult.getDescription(),containsString("city or airport"));
		assertEquals("string", modelParameterForExtremeSearchResult.getType());
		
		// Unit test for  LowFareSearch with array[references] parameter
		ModelDetail modelDetailForLowFareSearch = modelList.get(lowFareSearchIndex);
		// Extracting element 'results'
		ModelParameter modelParameterForResults = modelDetailForLowFareSearch.getModelParameterList().get(resultsModelParamIndex); 
		assertEquals("results", modelParameterForResults.getName());
		assertEquals("array", modelParameterForResults.getType());
		assertEquals("LowFareSearchResult", modelParameterForResults.getReferenceModel());
	}


}
