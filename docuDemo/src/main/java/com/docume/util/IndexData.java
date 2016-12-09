package com.docume.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.docume.util.Documentation.MustacheVariables;
import com.docume.util.Documentation.Template;
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
		HashMap<String, Object> indexScope = buildIndexScope(swagger);
		StringWriter indexWriter = new StringWriter();
		indexTemplate.execute(indexWriter, indexScope);
		return indexWriter.toString();

	}

	/**
	 * @param swagger
	 * @return
	 */
	private HashMap<String, Object> buildIndexScope(Swagger swagger) {
		
		HashMap<String, Object> indexScope = new HashMap<>();
		List<String> modelsList = generateModelTitleList(swagger);
		indexScope.put(MustacheVariables.MODEL_LIST, modelsList);
		
		List<String> operationIdList = generateOperationIdList(swagger);
		indexScope.put(MustacheVariables.OPERATION_ID, operationIdList);
		
		String info = swagger.getInfo().getTitle();
		indexScope.put("info", info);
		return indexScope;
	}

	/**
	 * @param modelMap
	 * @return
	 */
	private List<String> generateModelTitleList(Swagger swagger) {
		Map<String, Model> modelMap = swagger.getDefinitions();

		List<String> modelsList = new ArrayList<>();
		if (modelMap != null) {
			// Add model's list in index file
			for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
				modelsList.add(entry.getKey());
			}
		}
		return modelsList;
	}

	/**
	 * @param swagger
	 * @return
	 */
	private List<String> generateOperationIdList(Swagger swagger) {
		// Iterate through Paths,then operations in the Swagger file
		Map<String, Path> pathMap = swagger.getPaths();
		List<String> operationIdList = new ArrayList<>();
		
		for (Map.Entry<String, Path> pathDetail : pathMap.entrySet()) {
			Path path = pathDetail.getValue();
			List<Operation> operationList = path.getOperations();
			for (Operation operation : operationList) {
				operationIdList.add(operation.getOperationId());
			}

		}
		return operationIdList;
	}
	
	
	public void buildIndexPage(Swagger swagger){
		// Creating the Index page
					String indexHtmlFile = createIndexData(swagger);
					FileUtil.createFile(MustacheVariables.INDEX, indexHtmlFile);

	}

}
