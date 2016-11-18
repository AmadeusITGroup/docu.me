/**
 * 
 */
package com.amadeus.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.amadeus.docuMe.util.ApiData;
import com.amadeus.docuMe.util.Documentation;
import com.amadeus.docuMe.util.IndexData;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

/**
 * @author nghate
 *
 */
public class IndexDataTests {

	String swaggerFile = "d:\\Userfiles\\nghate\\Desktop\\hotelapi.yml";
	File file = new File(swaggerFile);

	/**
	 * Test method for {@link com.amadeus.docuMe.util.IndexData#createIndexData(io.swagger.models.Swagger)}.
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
		IndexData indexDataObj = new IndexData();
		String indexData = indexDataObj.createIndexData(swaggerObj);
		for(String operation:operationIdList){
			assertThat(indexData,CoreMatchers.containsString(operation));
		}
		for(String modelName:modelsList){
			assertThat(indexData,CoreMatchers.containsString(modelName));
		}
		
	}

}
