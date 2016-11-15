package com.amadeus.docuMe.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.amadeus.docuMe.pojo.MyResponse;
import com.amadeus.docuMe.util.Documentation.MustacheVariables;
import com.amadeus.docuMe.util.Documentation.Template;
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
	 * @param swaggerObj
	 * @param isExample
	 */
	public Map<String, String> createApiData(Swagger swaggerObj, String isExample) {
		// creating the url
		String basePath = swaggerObj.getBasePath();
		String host = swaggerObj.getHost();
		HashMap<String, String> apiDataMap = new HashMap<>();
		// Iterate through Paths,then operations in the Swagger file
		Map<String, Path> pathMap = swaggerObj.getPaths();
		HashMap<String, String> apiDetails;
		for (Map.Entry<String, Path> pathDetail : pathMap.entrySet()) {
			String pathUrl = pathDetail.getKey();
			Path path = pathDetail.getValue();
			Map<HttpMethod, Operation> httpMethodMap = path.getOperationMap();
			String url = swaggerObj.getSchemes().get(0).toString().toLowerCase() + "://" + host + basePath;
			url = url + pathUrl;
			apiDetails = getApiData(url, isExample, httpMethodMap);
			for (Map.Entry<String, String> api : apiDetails.entrySet()) {
				apiDataMap.put(api.getKey(), api.getValue());
			}
		}
		return apiDataMap;
	}

	// Create api template
	/**
	 * @param opList
	 * @param url
	 * @param isExample
	 * @param httpMethodMap
	 */
	private HashMap<String, String> getApiData(String url, String isExample, Map<HttpMethod, Operation> httpMethodMap) {

		Mustache apiTemplate = mf.compile(Template.API);
		HashMap<String, Object> apiScope = new HashMap<>();
		HashMap<String, String> apiDetails = new HashMap<>();
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
			if ("true".equalsIgnoreCase(isExample)) {
				example = GenerateExample.getReq(operation, url);
			}
			List<MyResponse> responseList = new ArrayList<>();
			responses = getReponsesMap(operation);
			for (Map.Entry<String, Response> responseObj : responses.entrySet()) {

				MyResponse response = getResponseList(responseObj);
				responseList.add(response);

			}
			apiScope.put(MustacheVariables.RESPONSES, responseList);
			apiScope.put(MustacheVariables.EXAMPLE, example);
			StringWriter apiWriter = new StringWriter();
			apiTemplate.execute(apiWriter, apiScope);
			apiDetails.put(operation.getOperationId(), apiWriter.toString());

		}
		return apiDetails;

	}

	/**
	 * @param responseList
	 * @param responseMap
	 */
	private MyResponse getResponseList(Map.Entry<String, Response> responseMap) {
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

	/**
	 * @param op
	 * @return
	 */
	public Map<String, Response> getReponsesMap(Operation op) {

		return op.getResponses();

	}

}
