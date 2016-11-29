package com.amadeus.docume.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amadeus.docume.util.Documentation.MustacheVariables;
import com.amadeus.docume.util.Documentation.Template;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;

public class IndexData {

	MustacheFactory mf = new DefaultMustacheFactory();

	// Create index template

	/**
	 * @param swagger
	 */
	public String createIndexData(Swagger swagger) {
		Mustache indexTemplate = mf.compile(Template.INDEX);
		HashMap<String, Object> indexScope = new HashMap<>();
		List<String> operationIdList = new ArrayList<>();
		List<String> modelsList = new ArrayList<>();
		StringWriter indexWriter = new StringWriter();

		// Iterate through Definitions,then models in the Swagger file
		Map<String, Model> modelMap = swagger.getDefinitions();

		if (modelMap != null) {
			// Add model's list in index file
			for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
				modelsList.add(entry.getKey());
			}
		}
		// Iterate through Paths,then operations in the Swagger file
		Map<String, Path> pathMap = swagger.getPaths();

		for (Map.Entry<String, Path> pathDetail : pathMap.entrySet()) {
			Path path = pathDetail.getValue();
			List<Operation> operationList = path.getOperations();
			for (Operation operation : operationList) {
				operationIdList.add(operation.getOperationId());
			}

		}

		indexScope.put(MustacheVariables.OPERATION_ID, operationIdList);
		indexScope.put(MustacheVariables.MODEL_LIST, modelsList);
		indexTemplate.execute(indexWriter, indexScope);
		return indexWriter.toString();

	}

}
