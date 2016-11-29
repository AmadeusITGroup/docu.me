/**
 * 
 */
package com.amadeus.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.amadeus.docuMe.util.Documentation;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * @author nghate
 *
 */
public class DocumentationTest {

	String swaggerFile = "d:\\Userfiles\\nghate\\Desktop\\hotelapi.yml";
	File file = new File(swaggerFile);

	/**
	 * Test method for
	 * {@link com.amadeus.docuMe.util.Documentation#getReponsesMap(io.swagger.models.Operation)}.
	 */
	@Test
	public void testGetReponsesMap() {
		Swagger swaggerObj;
		if (file.exists()) {
			swaggerObj = new SwaggerParser().read(swaggerFile);
		}
		Documentation doc = new Documentation();
	}

	/**
	 * Test method for
	 * {@link com.amadeus.docuMe.util.Documentation#createIndexData(io.swagger.models.Swagger)}.
	 */
	@Test
	public void testCreateIndexData() {
		Swagger swaggerObj = null;
		if (file.exists()) {
			swaggerObj = new SwaggerParser().read(swaggerFile);
		}
		List<String> operationIdList = new ArrayList<>();
		List<String> modelsList = new ArrayList<>();
		Map<String, Path> pathMap = swaggerObj.getPaths();
		Map<String, Model> modelMap = swaggerObj.getDefinitions();

		for (Map.Entry<String, Path> pathDetail : pathMap.entrySet()) {
			Path path = pathDetail.getValue();
			List<Operation> opList = path.getOperations();
			for (Operation op : opList) {
				operationIdList.add(op.getOperationId());
			}
		}
		
		if (modelMap != null) {
			// Add model's list in index file
			for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
				modelsList.add(entry.getKey());
			}
		}
		
		if (file.exists()) {
			swaggerObj = new SwaggerParser().read(swaggerFile);
		}
		Documentation doc = new Documentation();
		String indexData = doc.createIndexData(swaggerObj);
		for(String operation:operationIdList){
			assertThat(indexData,CoreMatchers.containsString(operation));
		}
		for(String modelName:modelsList){
			assertThat(indexData,CoreMatchers.containsString(modelName));
		}
		
	}

	/**
	 * Test method for
	 * {@link com.amadeus.docuMe.util.Documentation#createApiData(io.swagger.models.Swagger, java.lang.String)}.
	 */
	@Test
	public void testCreateApiData() {
		Swagger swaggerObj = null;
		if (file.exists()) {
			swaggerObj = new SwaggerParser().read(swaggerFile);
		}
		Documentation doc = new Documentation();
		Map<String, String> apiData = doc.createApiData(swaggerObj, false);
		for (Map.Entry<String, String> pathDetail : apiData.entrySet()) {
			String str = pathDetail.getKey();
			assertEquals("Get_hotellist_city.html", str);
			
				
		}
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.amadeus.docuMe.util.Documentation#createModelTemplate(io.swagger.models.Swagger)}.
	 */
	@Test
	public void testCreateModelTemplate() {
		fail("Not yet implemented");
	}

}
