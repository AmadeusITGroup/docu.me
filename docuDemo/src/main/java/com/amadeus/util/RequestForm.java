package com.amadeus.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

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
	
	
	
	public void createRequestForm(){
		
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = mf.compile("templates//requestform.mustache");
		HashMap<String, Object> currscope = new HashMap<>();
		StringWriter writer = new StringWriter();

		currscope.put("obj", "nea");
//		currscope.put("title", modelTitle);
		template.execute(writer, currscope);
		FileUtil.createFile("tryit", writer.toString());

	}
}
