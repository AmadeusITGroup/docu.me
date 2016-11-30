package com.amadeus.docume.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.amadeus.docume.pojo.Entity;
import com.amadeus.docume.pojo.MyResponse;
import com.amadeus.docume.util.Documentation.MustacheVariables;
import com.amadeus.docume.util.Documentation.Template;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

public class ApiData {

	MustacheFactory mf = new DefaultMustacheFactory();

	/**
	 * @param swagger
	 * @param isExample
	 */

	interface Variables {
		String SUCCESS_RESPONSE_CODE = "200";
	}

	public List<Entity> createApiData(Swagger swagger, boolean isExample) {

		ResponseModelData responseModel = new ResponseModelData();
		HashMap<String, org.json.simple.JSONObject> jsonResponseMap = (HashMap<String, org.json.simple.JSONObject>) responseModel
				.createResponseJsonSchema(swagger);

		String baseURL = createURL(swagger);

		List<Entity> apiEntityList = new ArrayList<>();
		HashMap<String, String> apiPageDetails = null;

		// Iterate through Paths for operations in the Swagger file
		Map<String, Path> pathMap = swagger.getPaths();

		for (Map.Entry<String, Path> pathDetail : pathMap.entrySet()) {
			
			// Create path url based on each path described in swagger.yml
			String pathUrl = pathDetail.getKey();
			String url = swagger.getSchemes().get(0).toString().toLowerCase() + "://" + baseURL;
			url = url + pathUrl;

			Path path = pathDetail.getValue();
			Map<HttpMethod, Operation> httpMethodMap = path.getOperationMap();

			for (Map.Entry<HttpMethod, Operation> httpMethod : httpMethodMap.entrySet()) {
				
				apiPageDetails = generateApiPage(url, isExample, httpMethod.getKey(), httpMethod.getValue(),jsonResponseMap);

			}
			for (Map.Entry<String, String> apiPage : apiPageDetails.entrySet()) {
				Entity entity = new Entity();
				entity.setEntityTitle(apiPage.getKey());
				entity.setEntityHtmlPage(apiPage.getValue());
				apiEntityList.add(entity);
			}
		}
		return apiEntityList;
	}

	private String createURL(Swagger swagger) {
		// creating the url
		String basePath = swagger.getBasePath();
		String host = swagger.getHost();

		return host + basePath;
	}

	// Create api template
	/**
	 * @param url
	 * @param isExample
	 * @param httpMethod
	 * @param jsonResponseMap
	 */
	private HashMap<String, String> generateApiPage(String url, boolean isExample, HttpMethod httpMethod,
			Operation operation, HashMap<String, org.json.simple.JSONObject> jsonResponseMap) {

		Mustache apiTemplate = mf.compile(Template.API);
		HashMap<String, String> apiDetails = new HashMap<>();

		HashMap<String, Object> apiScope = buildApiScope(url, isExample, httpMethod, jsonResponseMap, operation);

		StringWriter apiWriter = new StringWriter();
		apiTemplate.execute(apiWriter, apiScope);
		apiDetails.put(operation.getOperationId(), apiWriter.toString());

		return apiDetails;

	}

	/**
	 * @param url
	 * @param isExample
	 * @param httpMethod
	 * @param jsonResponseMap
	 * @param operation
	 * @return
	 */
	private HashMap<String, Object> buildApiScope(String url, boolean isExample, HttpMethod method,
			HashMap<String, org.json.simple.JSONObject> jsonResponseMap, Operation operation) {
		// Retrieve the api key which is set as environment variable
		String apiKey = System.getenv("APIKEY");
		HashMap<String, Object> apiScope = new HashMap<>();
		apiScope.put(MustacheVariables.URL, url);
		apiScope.put(MustacheVariables.APIKEY, apiKey);
		apiScope.put(MustacheVariables.OPERATION, operation);
		apiScope.put(MustacheVariables.HTTP_METHOD, method);

		if (isExample) {
			JSONObject example = GenerateExample.getLiveExample(operation, url);
			apiScope.put(MustacheVariables.EXAMPLE, example);
		}

		List<MyResponse> responseList = createResponsesList(operation);
		apiScope.put(MustacheVariables.RESPONSES, responseList);

		for (Map.Entry<String, org.json.simple.JSONObject> responseObj : jsonResponseMap.entrySet()) {
			for (MyResponse response : responseList) {
				if (responseObj.getKey().equalsIgnoreCase(response.getSimpleReference())
						&& (Variables.SUCCESS_RESPONSE_CODE.equals(response.getResponseNumber()))) {
					apiScope.put(MustacheVariables.RESPONSE_SCHEMA, responseObj.getValue());
				}
			}
		}

		return apiScope;
	}

	/**
	 * @param operation
	 * @return
	 */
	private List<MyResponse> createResponsesList(Operation operation) {
		List<MyResponse> responsesList = new ArrayList<>();
		Map<String, Response> responsesMap = operation.getResponses();
		
		for (Map.Entry<String, Response> responseObj : responsesMap.entrySet()) {

			MyResponse response = getResponseList(responseObj);
			responsesList.add(response);

		}
		return responsesList;
	}

	/**
	 * @param responseMap
	 */
	private MyResponse getResponseList(Map.Entry<String, Response> responseMap) {
		MyResponse response = null;
		String resName = responseMap.getKey();
		Response resValue = responseMap.getValue();
		Property property = resValue.getSchema();

		
		if (property != null) {
			if (property instanceof ArrayProperty) {
				ArrayProperty ap = (ArrayProperty) property;
				Property p2 = ap.getItems();
				response = extractSimple(resName, resValue, p2);
			} else {
				response = extractSimple(resName, resValue, property);
			}

		} else {
			response.setReference("No property reference was found.");
		}
		return response;
	}

	/**
	 * @param resName
	 * @param resValue
	 * @param property
	 * @return
	 */
	private MyResponse extractSimple(String resName, Response resValue, Property property) {
		RefProperty rp = (RefProperty) property;
		String ref = rp.get$ref();
		String simpleReference = rp.getSimpleRef();
		
		MyResponse response = new MyResponse();
		response.setDescription(resValue.getDescription());
		response.setReference(ref);
		response.setResponseNumber(resName);
		response.setSimpleReference(simpleReference);

		return response;
	}

}
