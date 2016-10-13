package com.amadeus.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;

public class RequestForm {

	String requestUrl;
	final static Logger logger = Logger.getLogger(RequestForm.class);

	public void getReq(){
		
		try{
		requestUrl = "https://api.sandbox.amadeus.com/v1.2/airports/autocomplete?apikey=Uz3HSvIwhIhqE0MuB0R6pvNDERwoAnqA&term=BOS&country=US";
        URL url = new URL(requestUrl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();

        uc.setRequestProperty("Accept", "application/json");
        System.out.println(uc.getResponseMessage());
        
        BufferedReader br = new BufferedReader(new InputStreamReader(
    			(uc.getInputStream())));

    		String output;
    		System.out.println("Output from Server .... \n");
    		while ((output = br.readLine()) != null) {
    			System.out.println(output);
    		}


        uc.disconnect();
		}
		catch (Exception ex) {
            logger.error(ex.getMessage());
        }
		
	}

	public void createRequestForm(String swaggerFile){
		
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = mf.compile("templates//requestform.mustache");
		HashMap<String, Object> currscope = new HashMap<>();
		
		//Iterate through Paths,then operations in the Swagger file
		Map<String, Path> pathMap = FileUtil.parseSwaggerFile(swaggerFile).getPaths();
			Swagger swagger = FileUtil.parseSwaggerFile(swaggerFile);
			String basePath = swagger.getBasePath();
			String host = swagger.getHost();
			
		for (Map.Entry<String, Path> entry : pathMap.entrySet()) {
			String pathUrl = entry.getKey();
			Path path = entry.getValue();
			List<Operation> opList = path.getOperations();
			String url = "https://"+host+basePath;
			url = url+pathUrl;
			currscope.put("url", url);
			for (Operation op : opList) {
				currscope.put("operations", op);
				StringWriter writer = new StringWriter();
				template.execute(writer, currscope);
				FileUtil.createFile(op.getOperationId()+"request", writer.toString());
			}

		}

	}
}
