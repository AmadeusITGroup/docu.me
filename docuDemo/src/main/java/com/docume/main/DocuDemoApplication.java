package com.docume.main;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.docume.util.ApiData;
import com.docume.util.FileUtil;
import com.docume.util.IndexData;
import com.docume.util.ResponseModelData;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

@SpringBootApplication
public class DocuDemoApplication {


	static final Logger logger = Logger.getLogger(DocuDemoApplication.class);

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DocuDemoApplication.class, args);
		String swaggerFile = args[0];
		boolean example = FileUtil.toBoolean(args[1]);
		File file = new File(swaggerFile);
		ApiData apiData = new ApiData();
		IndexData indexData = new IndexData();
		ResponseModelData responseModelData = new ResponseModelData();

		if (file.exists()) {

			Swagger swagger = new SwaggerParser().read(swaggerFile);

			indexData.buildIndexPage(swagger);
			apiData.buildAPIPages(swagger, example);
			responseModelData.buildResponseModelPage(swagger);

			// JS and CSS files for creating tree structure for json object
			FileUtil fileUtil = new FileUtil();
			fileUtil.createLib();
			logger.info("The documentation portal is generated at this location: /docuDemo/Portal");
			System.exit(0);

		} else {
			logger.error("======================Error===============================");
			logger.error("Please input a valid file location");
			System.exit(1);
		}
		

	}

}
