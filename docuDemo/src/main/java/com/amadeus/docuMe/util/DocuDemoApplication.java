package com.amadeus.docuMe.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

@SpringBootApplication
public class DocuDemoApplication {

	final static Logger logger = Logger.getLogger(Documentation.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DocuDemoApplication.class, args);
		Documentation document = new Documentation();
		// String swaggerFile = args[0];
		String isExample = "false";
		String swaggerFile = "d:\\Userfiles\\nghate\\Desktop\\swg.yml";
		File file = new File(swaggerFile);
		ApiData ad = new ApiData();
		IndexData id = new IndexData();
		ResponseModel rm = new ResponseModel();

		if (file.exists()) {

			Swagger swaggerObj = new SwaggerParser().read(swaggerFile);
			String indexFile = id.createIndexData(swaggerObj);
			FileUtil.createFile("index", indexFile);

			Map<String, String> hm = ad.createApiData(swaggerObj, isExample);
			for (Map.Entry<String, String> api : hm.entrySet()) {
				FileUtil.createFile(api.getKey(), api.getValue());
			}

			rm.createModelTemplate(swaggerObj);
			FileUtil.createLib();
			logger.info("The docs are generated in this location: /docuDemo/Portal");

		} else {
			logger.error("Please input a valid file location");

		}
		System.exit(0);

	}

}
