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

import io.swagger.models.ComposedModel;
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

	interface Template {
		String INDEX = "templates\\index.mustache";
		String API = "templates\\api.mustache";
		String MODEL = "templates//model.mustache";

	}

	interface MustacheVariables {

		String OPERATION_ID = "operationId";
		String MODEL_LIST = "modelList";
		String URL = "url";
		String APIKEY = "apiKey";
		String OPERATION = "operations";
		String HTTP_METHOD = "httpMethod";
		String RESPONSES = "responses";
		String EXAMPLE = "example";
		String MODEL_DETAILS_LIST = "modelDetailsList";
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
	public void createIndexApiTemplate(Swagger swaggerObj, String isExample) {
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
			Map<HttpMethod, Operation> httpMethodMap = path.getOperationMap();

			List<Operation> opList = path.getOperations();
			String url = "https://" + host + basePath;
			url = url + pathUrl;

			createApiTemplate(url, isExample, httpMethodMap);

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

		indexScope.put(MustacheVariables.OPERATION_ID, operationIdList);
		indexScope.put(MustacheVariables.MODEL_LIST, modelsList);
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
	private void createApiTemplate(String url, String isExample, Map<HttpMethod, Operation> httpMethodMap) {

		Mustache apiTemplate = mf.compile(Template.API);
		HashMap<String, Object> apiScope = new HashMap<>();

		Map<String, Response> responses;
		String simpleReference;
		// Retrieve the api key which is set as environment variable
		String apiKey = System.getenv("APIKEY");
		apiScope.put(MustacheVariables.URL, url);
		apiScope.put(MustacheVariables.APIKEY, apiKey);
		JSONObject example = null;

		for (Map.Entry<HttpMethod, Operation> httpMethod : httpMethodMap.entrySet()) {
			HttpMethod method = httpMethod.getKey();
			Operation operation = httpMethod.getValue();
			apiScope.put(MustacheVariables.OPERATION, operation);
			apiScope.put(MustacheVariables.HTTP_METHOD, method);
			if (isExample.equalsIgnoreCase("yes")) {
				example = GenerateExample.getReq(operation, url);
			}
			List<MyResponse> responseList = new ArrayList<>();
			responses = getReponses(operation);
			for (Map.Entry<String, Response> responseObj : responses.entrySet()) {

				MyResponse response = makeResponseList(responseObj);
				responseList.add(response);

			}
			apiScope.put(MustacheVariables.RESPONSES, responseList);
			apiScope.put(MustacheVariables.EXAMPLE, example);
			StringWriter apiWriter = new StringWriter();
			apiTemplate.execute(apiWriter, apiScope);
			FileUtil.createFile(operation.getOperationId(), apiWriter.toString());

		}

	}

	/**
	 * @param responseList
	 * @param responseMap
	 */
	private MyResponse makeResponseList(Map.Entry<String, Response> responseMap) {
		String simpleReference;
		String resName = responseMap.getKey();
		Response resValue = responseMap.getValue();
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

		} else {
			response.setReference("No property reference was found.");
		}
		return response;
	}

	// create models
	/**
	 * @param swaggerObj
	 */
	public void createModelTemplate(Swagger swaggerObj) {

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache modelTemplate = mf.compile(Template.MODEL);
		HashMap<String, Object> modelScope = new HashMap<>();
		Map<String, Model> modelMap = swaggerObj.getDefinitions();

		List<ModelDetail> modelDetailsList = new ArrayList<>();

		List<MyModel> myModelList;
		ModelDetail modelDetail;

		for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
			modelDetail = iterateModels(entry);
			modelDetailsList.add(modelDetail);
		}
		modelScope.put("modelDetailsList", modelDetailsList);

		StringWriter writer = new StringWriter();
		modelTemplate.execute(writer, modelScope);
		FileUtil.createFile("modelnew", writer.toString());

	}

	private ModelDetail iterateModels(Map.Entry<String, Model> modelMap) {
		List<Model> modelList;
		Model model = modelMap.getValue();
		ModelDetail md = null;
		if (model instanceof ComposedModel) {
			ComposedModel cm = (ComposedModel) model;
			modelList = cm.getAllOf();
			for (Model m : modelList) {
				Map<String, Property> modelPropMap = m.getProperties();
				md = iterateModelProperties(modelPropMap);
			}
		} else {
			Map<String, Property> modelPropMap = model.getProperties();
			md = iterateModelProperties(modelPropMap);
		}

		md.setModelTitle(modelMap.getKey());
		return md;

	}

	private ModelDetail iterateModelProperties(Map<String, Property> modelPropertyMap) {
		List<MyModel> myModelList;
		ModelDetail modelDetail = new ModelDetail();

		if (modelPropertyMap != null) {
			for (Map.Entry<String, Property> modelProp : modelPropertyMap.entrySet()) {
				String modelName = modelProp.getKey();
				Property property = modelProp.getValue();
				myModelList = modelDetail.getModelList();

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
		}
		return modelDetail;
	}

}
