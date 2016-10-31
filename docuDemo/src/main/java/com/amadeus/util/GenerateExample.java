/**
 * 
 */
package com.amadeus.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.mozilla.javascript.ast.ContinueStatement;

import io.swagger.models.Operation;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;

/**
 * @author nghate
 *
 */
public class GenerateExample {

	final static Logger logger = Logger.getLogger(GenerateExample.class);

	public static JSONObject getReq(Operation op, String url2) {
		String requestUrl = null;
		JSONObject output = null;
		try {
			requestUrl = getURL(op, url2);
			URL url = new URL(requestUrl);
			HttpURLConnection  connection = (HttpURLConnection) new URL(requestUrl).openConnection();
			int responseCode = connection.getResponseCode();
		
			if (responseCode == 200) {
				Object json = new JSONTokener(url.openStream()).nextValue();

				if (json instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) json;
					output = jsonObject;
				} else if (json instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) json;
					output = (JSONObject) jsonArray.get(0);
				}
			} else {
				logger.info("Response could not be generated.");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return output;

	}

	public static String getURL(Operation op, String url2) {
		URIBuilder builder = new URIBuilder();
		List<Parameter> paramList = op.getParameters();
		String url = url2;
		for (Parameter p : paramList) {
			if (p.getRequired() && p instanceof QueryParameter) {
				QueryParameter qp = (QueryParameter) p;
				
				if (p.getName().equals("apikey")) {
					builder.setParameter(p.getName(), System.getenv("AMADEUS_APIKEY"));
				} else {
					builder.setParameter(p.getName(), qp.getDefaultValue());
				}
			} else if (p instanceof PathParameter) {
				PathParameter pp = (PathParameter) p;
				url = url.replace("{" + p.getName() + "}", pp.getDefaultValue());
			}
		}
		builder.setPath(url);

		URI uri = null;
		try {
			uri = builder.build();

		} catch (URISyntaxException e) {

			logger.error(e.getMessage());
		}

		HttpGet httpget = new HttpGet(uri);

		System.out.println(httpget.getURI());
		return httpget.getURI().toString();
	}

}
