package com.docume.util;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.docume.pojo.Example;

import io.swagger.models.Operation;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;

/**
 * @author nghate
 *
 */
public class GenerateExample {


	interface Variables {
		int SUCCESS_RESPONSE_CODE = 200;
		String APIKEY = "apikey";
	}

	static final Logger logger = Logger.getLogger(GenerateExample.class);

	public static Example createLiveExample(Operation operation, String baseUrl) {
		String requestUrl = null;
		JSONObject output = null;
		try {
			requestUrl = getURL(operation, baseUrl);
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
			int responseCode = connection.getResponseCode();

			if (responseCode == Variables.SUCCESS_RESPONSE_CODE) {
				Object json = new JSONTokener(url.openStream()).nextValue();

				if (json instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) json;
					output = jsonObject;
				} else if (json instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) json;
					output = (JSONObject) jsonArray.get(0);
				}
			} else {
				logger.warn("Response generated with error code " + responseCode +"\n"+ " One of the default parameters in "+url+" is incorrect.");
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
		Example example = new Example();
		example.setExampleJson(output);
		example.setExampleURL(requestUrl);
		return example;

	}

	public static String getURL(Operation operation, String baseUrl) {
		URIBuilder builder = new URIBuilder();
		List<Parameter> paramList = operation.getParameters();
		String url = baseUrl;
		for (Parameter parameter : paramList) {
			if (parameter.getRequired() && parameter instanceof QueryParameter) {
				QueryParameter qp = (QueryParameter) parameter;

				if (Variables.APIKEY.equals(parameter.getName())) {
					builder.setParameter(parameter.getName(), System.getenv("APIKEY"));
				} else {
					builder.setParameter(parameter.getName(), qp.getDefaultValue());
				}
			} else if (parameter instanceof PathParameter) {
				PathParameter pathParameter = (PathParameter) parameter;
				url = url.replace("{" + parameter.getName() + "}", pathParameter.getDefaultValue());
			}
		}
		builder.setPath(url);

		URI uri = null;
		try {
			uri = builder.build();

		} catch (URISyntaxException e) {

			logger.error(e);
		}

		HttpGet httpget = new HttpGet(uri);

		logger.info(httpget.getURI());
		return httpget.getURI().toString();
	}

}
