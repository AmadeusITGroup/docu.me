package com.amadeus.util;


import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;


@SpringBootApplication
public class DocuDemoApplication {

	final static Logger logger = Logger.getLogger(Documentation.class);

	public static void main(String[] args) {
		SpringApplication.run(DocuDemoApplication.class, args);
		Documentation document = new Documentation();
		
		 // get their input as a String
	    String swaggerFile = args[0];
	    File fileName = new File(swaggerFile);
	    Swagger swaggerObj = new SwaggerParser().read(swaggerFile);
	    
		document.createIndexApiTemplate(swaggerObj);
//		document.createModelTemplate(swaggerObj);
		
		
		logger.info("The docs are generated in this location: /docuDemo/Portal");
	}
}
