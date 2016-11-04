package com.amadeus.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

@SpringBootApplication
public class DocuDemoApplication {

	final static Logger logger = Logger.getLogger(Documentation.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DocuDemoApplication.class, args);
		Documentation document = new Documentation();
		// get their input as a String
//		String swaggerFile = args[0];
		String isExample = "no";
		String swaggerFile = "d:\\Userfiles\\nghate\\Desktop\\dapimessages.yml";
		File file = new File(swaggerFile);

		if (file.exists()) {

			Swagger swaggerObj = new SwaggerParser().read(swaggerFile);
			document.createIndexApiTemplate(swaggerObj,isExample);
			document.createModelTemplate(swaggerObj);
			FileUtil.createLib();
			logger.info("The docs are generated in this location: /docuDemo/Portal");

			
		} else {
			logger.error("Please input a valid file location");
			
			
		}
		System.exit(0);

	}
	
	
}
