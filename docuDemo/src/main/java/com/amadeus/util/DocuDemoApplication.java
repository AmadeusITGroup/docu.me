package com.amadeus.util;


import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DocuDemoApplication {

	final static Logger logger = Logger.getLogger(Documentation.class);

	public static void main(String[] args) {
		SpringApplication.run(DocuDemoApplication.class, args);
		Documentation document = new Documentation();
		
		 // get their input as a String
	    String swaggerFile = args[0];
	     
		document.createIndexApiTemplate(swaggerFile);
		document.createModelTemplate(swaggerFile);
		
		
		logger.info("The docs are generated in this location: /docuDemo/Portal");
	}
}
