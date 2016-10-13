package com.amadeus.util;


import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DocuDemoApplication {

	final static Logger logger = Logger.getLogger(Documentation.class);

	public static void main(String[] args) {
		SpringApplication.run(DocuDemoApplication.class, args);
		Documentation cf = new Documentation();
		
		 // get their input as a String
	    String swaggerFile = args[0];
	     
		cf.createIndexApiTemplate(swaggerFile);
		cf.createModelTemplate(swaggerFile);
		
//		rf.createRequestForm(swaggerFile);
		
		logger.info("The docs are generated in this location: /docuDemo/Portal");
	}
}
