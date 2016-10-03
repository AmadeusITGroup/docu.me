package com.amadeus.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amadeus.pojo.ModelDetail;
import com.amadeus.pojo.MyModel;
import com.amadeus.pojo.MyResponse;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

public class Documentation {

	final static Logger logger = Logger.getLogger(Documentation.class);

	public  Map<String, Response> getReponses(Operation op) {

		return op.getResponses();

	}
	
	//Create index template 
	
	public void createIndexApiTemplate(String swaggerFile) {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache apiTemplate = mf.compile("templates\\api.mustache");
		Mustache indexTemplate = mf.compile("templates\\index.mustache");

		HashMap<String, Object> apiScope = new HashMap<>();
		HashMap<String, Object> indexScope = new HashMap<>();

		List<String> operationIdList = new ArrayList<>();
		List<String> modelsList = new ArrayList<>();
		List<MyResponse> responsesList = new ArrayList<>();

		StringWriter indexWriter = new StringWriter();
		
		//Iterate through Paths,then operations in the Swagger file
		Map<String, Path> pathMap = FileUtil.parseSwaggerFile(swaggerFile).getPaths();
		
		//Iterate through Definitions,then models in the Swagger file 
		Map<String, Model> modelMap = FileUtil.parseSwaggerFile(swaggerFile).getDefinitions();

		for (Map.Entry<String, Path> entry : pathMap.entrySet()) {
			Path path = entry.getValue();
			List<Operation> opList = path.getOperations();
			Map<String, Response> responses = null;
			String simpleRef = "noRef";

			createApiTemplate(opList, apiScope, responses, simpleRef, responsesList, apiTemplate, operationIdList);

		}
		
		
		//Add model's list in index file
		for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
			modelsList.add(entry.getKey());
		}
		
		
		indexScope.put("operationId", operationIdList);
		indexScope.put("modelList", modelsList);
		indexTemplate.execute(indexWriter, indexScope);
		FileUtil.createFile("index", indexWriter.toString());

	}

	private void createApiTemplate(List<Operation> opList, HashMap<String, Object> apiScope,
			Map<String, Response> responses, String simpleRef, List<MyResponse> responsesList, Mustache apiTemplate,
			List<String> operationIdList) {

		for (Operation op : opList) {
			apiScope.put("operations", op);
			List<MyResponse> resList = new ArrayList<>();
			responses = getReponses(op);
			for (Map.Entry<String, Response> res : responses.entrySet()) {
				String resName = res.getKey();
				Response resValue = res.getValue();
				apiScope.put("resName", resName);
				apiScope.put("resValue", resValue.getDescription());
				MyResponse response = new MyResponse();
				response.setDescription(resValue.getDescription());
				Property property = resValue.getSchema();
				if (property instanceof ArrayProperty) {
					ArrayProperty ap = (ArrayProperty) property;
					Property p2 = ap.getItems();
					RefProperty rp = (RefProperty) p2;
					String ref = rp.get$ref();
					simpleRef = rp.getSimpleRef();
					response.setReference(ref);
					response.setResponseNumber(resName);
				} else {
					RefProperty rp = (RefProperty) property;
					String ref = rp.get$ref();
					simpleRef = rp.getSimpleRef();
					response.setReference(ref);
					response.setResponseNumber(resName);
				}
				responsesList.add(response);
				resList.add(response);

			}
			apiScope.put("responses", resList);
			operationIdList.add(op.getOperationId());

			StringWriter apiWriter = new StringWriter();
			apiTemplate.execute(apiWriter, apiScope);
			FileUtil.createFile(op.getOperationId(), apiWriter.toString());

		}

	}

	public void createModelTemplate(String swaggerFile) {

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache modelTemplate = mf.compile("templates//model.mustache");
		HashMap<String, Object> modelScope = new HashMap<>();
		Map<String, Model> modelMap = FileUtil.parseSwaggerFile(swaggerFile).getDefinitions();

		List<ModelDetail> modelDetailsList = new ArrayList<>();
		List<MyModel> myModelList = null;
		ModelDetail modelDetail;

		for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
			Model model = entry.getValue();
			modelDetail = new ModelDetail();
			modelDetail.setModelTitle(entry.getKey());
			Map<String, Property> modelPropMap = model.getProperties();

			if (modelPropMap != null) {
				for (Map.Entry<String, Property> modelProp : modelPropMap.entrySet()) {
					String modelName = modelProp.getKey();
					Property property = modelProp.getValue();
					myModelList = modelDetail.getModelList();
					
					modelScope.put("refName", entry.getKey()); 
					
					MyModel myModel = new MyModel();
					myModel.setModelDesc(property.getDescription());
					myModel.setModelName(modelName);
					myModel.setModelType(property.getType());
					if("ref".equals(property.getType())){
						RefProperty rp = (RefProperty) property;
						String simpleRef= rp.getSimpleRef();
					}
					else{
//						System.out.println("example: "+property.getExample());
					}
					myModelList.add(myModel);
					
					modelDetail.setModelList(myModelList);
				}
				modelDetailsList.add(modelDetail);
			}
		}

		modelScope.put("obj", myModelList);
		modelScope.put("List", modelDetailsList);
		
		StringWriter writer = new StringWriter();
		modelTemplate.execute(writer, modelScope);
		FileUtil.createFile("modelnew", writer.toString());

	}

	public void createExample(List<MyModel> modelOb, String modelTitle) {

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = mf.compile("exampleJson.mustache");
		HashMap<String, Object> currscope = new HashMap<>();
		StringWriter writer = new StringWriter();

		currscope.put("obj", modelOb);
		currscope.put("title", modelTitle);
		template.execute(writer, currscope);
		FileUtil.createFile("exampleJson", writer.toString());

	}

}
