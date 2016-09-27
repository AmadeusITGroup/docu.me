package com.amadeus.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;

public class Documentation {

	String dir = "d:\\Userfiles\\nghate\\Desktop\\genSmall\\models\\";

	public void createFile(String filename, String content) {
		try {

			File file = new File(dir, filename + ".html");

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();

			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println(filename + ".html already exists.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Swagger parseSwaggerFile(String swaggerFile) {

		String swaggerFile1 = "d:\\Userfiles\\nghate\\Desktop\\swg.yml";

		return new SwaggerParser().read(swaggerFile1);
	}

	public Map<String, Response> getReponses(Operation op) {

		return op.getResponses();

	}

	public void createApiTemplate(String swaggerFile) {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache apiTemplate = mf.compile("templates\\api.mustache");
		Mustache indexTemplate = mf.compile("templates\\index.mustache");

		HashMap<String, Object> apiScope = new HashMap<>();
		HashMap<String, Object> indexScope = new HashMap<>();

		List<String> operationIdList = new ArrayList<>();
		List<String> modelsList = new ArrayList<>();
		List<MyResponse> responsesList = new ArrayList<>();

		StringWriter indexWriter = new StringWriter();
		Map<String, Path> pathMap = parseSwaggerFile(swaggerFile).getPaths();
		Map<String, Model> modelMap = parseSwaggerFile(swaggerFile).getDefinitions();

		for (Map.Entry<String, Path> entry : pathMap.entrySet()) {
			Path path = entry.getValue();
			List<Operation> opList = path.getOperations();
			Map<String, Response> responses = null;
			String simpleRef = "noRef";

			getOperations(opList, apiScope, responses, simpleRef, responsesList, apiTemplate, operationIdList);

		}
		
		for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
			modelsList.add(entry.getKey());
		}
		
		
		indexScope.put("operationId", operationIdList);
		indexScope.put("modelList", modelsList);
		indexTemplate.execute(indexWriter, indexScope);
		createFile("index", indexWriter.toString());

	}

	private void getOperations(List<Operation> opList, HashMap<String, Object> apiScope,
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
			createFile(op.getOperationId(), apiWriter.toString());

		}

	}

	public void displayModels(String swaggerFile) {

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = mf.compile("templates//model.mustache");
		HashMap<String, Object> currscope = new HashMap<>();
		Map<String, Model> modelMap = parseSwaggerFile(swaggerFile).getDefinitions();

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
					
					currscope.put("refName", entry.getKey()); 
					
					MyModel myModel = new MyModel();
					myModel.setModelDesc(property.getDescription());
					myModel.setModelName(modelName);
					myModel.setModelType(property.getType());
					if("ref".equals(property.getType())){
						RefProperty rp = (RefProperty) property;
						String simpleRef= rp.getSimpleRef();
					}
					else{
						System.out.println("example: "+property.getExample());
					}
					myModelList.add(myModel);
					
					modelDetail.setModelList(myModelList);
				}
				modelDetailsList.add(modelDetail);
			}
		}

		currscope.put("obj", myModelList);
		currscope.put("List", modelDetailsList);
		
		StringWriter writer = new StringWriter();
		template.execute(writer, currscope);
		createFile("modelnew", writer.toString());

	}

	public void createExample(List<MyModel> modelOb, String modelTitle) {

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = mf.compile("exampleJson.mustache");
		HashMap<String, Object> currscope = new HashMap<>();
		StringWriter writer = new StringWriter();

		currscope.put("obj", modelOb);
		currscope.put("title", modelTitle);
		template.execute(writer, currscope);
		createFile("exampleJson", writer.toString());

	}

}
