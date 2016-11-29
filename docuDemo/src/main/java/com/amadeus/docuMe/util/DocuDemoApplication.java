package com.amadeus.docume.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amadeus.docume.pojo.Entity;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

@SpringBootApplication
public class DocuDemoApplication {

	static final Logger logger = Logger.getLogger(Documentation.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DocuDemoApplication.class, args);
//		String swaggerFile = args[0];
		boolean isExample = FileUtil.toBoolean(args[1]);
		 String swaggerFile = "d:\\Userfiles\\nghate\\Desktop\\swg.yml";
		File file = new File(swaggerFile);
		ApiData apiData = new ApiData();
		IndexData indexData = new IndexData();
		ResponseModelData responseModelData = new ResponseModelData();

		if (file.exists()) {

			Swagger swagger = new SwaggerParser().read(swaggerFile);
			String indexFile = indexData.createIndexData(swagger);
			FileUtil.createFile("index", indexFile);

			List<Entity> apiEntityList = apiData.createApiData(swagger, isExample);
			for (Entity entity : apiEntityList) {
				FileUtil.createFile(entity.getEntityName(), entity.getEntityValue());
			}
			
			responseModelData.createModelTemplate(swagger);
			FileUtil.createLib();
			logger.info("The docs are generated in this location: /docuDemo/Portal");

		} else {
			logger.error("Please input a valid file location");

		}
		System.exit(0);

	}

}
