package com.docume.util;



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
		String RESPONSE_SCHEMA = "responseSchema";
		String INDEX = "index";
		String MODEL = "model";
	}
}


