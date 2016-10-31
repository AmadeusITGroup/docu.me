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
import org.json.JSONArray;
import org.json.JSONObject;

import com.amadeus.pojo.ModelDetail;
import com.amadeus.pojo.MyModel;
import com.amadeus.pojo.MyResponse;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.swagger.models.HttpMethod;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

public class Documentation {
	
	interface Template{
		String INDEX = "templates\\index.mustache";
	}

	final static Logger logger = Logger.getLogger(Documentation.class);
	MustacheFactory mf = new DefaultMustacheFactory();

	public Map<String, Response> getReponses(Operation op) {

		return op.getResponses();

	}

	// Create index template

	/**
	 * @param swaggerObj
	 */
	public void createIndexApiTemplate(Swagger swaggerObj,String isExample) {
		Mustache indexTemplate = mf.compile(Template.INDEX);

		HashMap<String, Object> indexScope = new HashMap<>();

		List<String> operationIdList = new ArrayList<>();
		List<String> modelsList = new ArrayList<>();

		StringWriter indexWriter = new StringWriter();

		// creating the url
		String basePath = swaggerObj.getBasePath();
		String host = swaggerObj.getHost();

		// Iterate through Paths,then operations in the Swagger file
		Map<String, Path> pathMap = swaggerObj.getPaths();

		// Iterate through Definitions,then models in the Swagger file
		Map<String, Model> modelMap = swaggerObj.getDefinitions();

		for (Map.Entry<String, Path> pathDetail : pathMap.entrySet()) {
			String pathUrl = pathDetail.getKey();
			Path path = pathDetail.getValue();
			Map<HttpMethod,Operation> httpMethodMap =path.getOperationMap();
			
			List<Operation> opList = path.getOperations();
			String url = "https://" + host + basePath;
			url = url + pathUrl;

			createApiTemplate(url,isExample,httpMethodMap);

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

		indexScope.put("operationId", operationIdList);
		indexScope.put("modelList", modelsList);
		indexTemplate.execute(indexWriter, indexScope);
		FileUtil.createFile("index", indexWriter.toString());

	}

	// Create api template
	/**
	 * @param opList
	 * @param url
	 * @param isExample 
	 * @param httpMethodMap 
	 */
	private void createApiTemplate( String url, String isExample, Map<HttpMethod, Operation> httpMethodMap) {

		Mustache apiTemplate = mf.compile("templates\\api.mustache");
		HashMap<String, Object> apiScope = new HashMap<>();

		Map<String, Response> responses;
		String simpleReference;
		// Retrieve the api key which is set as environment variable
		String apiKey = System.getenv("AMADEUS_APIKEY");
		apiScope.put("url", url);
		apiScope.put("apiKey", apiKey);
		JSONObject example = null;

		for (Map.Entry<HttpMethod, Operation> httpMethod : httpMethodMap.entrySet()) {
			HttpMethod method = httpMethod.getKey();
			Operation op = httpMethod.getValue();
			apiScope.put("operations", op);
			apiScope.put("httpMethod", method);
			if(isExample.equalsIgnoreCase("yes")){
				example = GenerateExample.getReq(op, url);
			}
			List<MyResponse> resList = new ArrayList<>();
			responses = getReponses(op);
			for (Map.Entry<String, Response> responseObj : responses.entrySet()) {

				makeResponseList(resList, responseObj);

			}
			apiScope.put("responses", resList);
			apiScope.put("example", example);
			StringWriter apiWriter = new StringWriter();
			apiTemplate.execute(apiWriter, apiScope);
			FileUtil.createFile(op.getOperationId(), apiWriter.toString());

		}
		

	}

	/**
	 * @param resList
	 * @param res
	 */
	private void makeResponseList(List<MyResponse> resList, Map.Entry<String, Response> res) {
		String simpleReference;
		String resName = res.getKey();
		Response resValue = res.getValue();
		Property property = resValue.getSchema();

		MyResponse response = new MyResponse();
		response.setDescription(resValue.getDescription());

		if (property != null) {
			if (property instanceof ArrayProperty) {
				ArrayProperty ap = (ArrayProperty) property;
				Property p2 = ap.getItems();
				RefProperty rp = (RefProperty) p2;
				String ref = rp.get$ref();
				simpleReference = rp.getSimpleRef();
				response.setReference(ref);
				response.setResponseNumber(resName);
			} else {
				RefProperty rp = (RefProperty) property;
				String ref = rp.get$ref();
				simpleReference = rp.getSimpleRef();
				response.setReference(ref);
				response.setResponseNumber(resName);
			}
			response.setSimpleReference(simpleReference);
			resList.add(response);

		} else {
			response.setReference("No property reference was found.");
			resList.add(response);
		}
	}

	// create models
	public void createModelTemplate(Swagger swaggerObj) {

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache modelTemplate = mf.compile("templates//model.mustache");
		HashMap<String, Object> modelScope = new HashMap<>();
		Map<String, Model> modelMap = swaggerObj.getDefinitions();

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
					RefProperty r;
					if (property instanceof RefProperty) {
						RefProperty rp = (RefProperty) property;
						String simpleReference = rp.getSimpleRef();
						myModel.setModelRef(simpleReference);
					} else if (property instanceof ArrayProperty) {
						ArrayProperty ap = (ArrayProperty) property;
						Property ap1 = ap.getItems();
						if (ap1 instanceof RefProperty) {
							r = (RefProperty) ap1;
							String simpleReference = r.getSimpleRef();
							myModel.setModelDesc(simpleReference);
						}
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

	

}
