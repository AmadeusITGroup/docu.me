package com.amadeus.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

public class FileUtil {

	final static Logger logger = Logger.getLogger(Documentation.class);


	public static void createFile(String filename, String content) {
		try {
			
			 File filedir = new File("Portal\\docs\\");
			 filedir.mkdirs();
			File file = new File(filedir, filename + ".html");

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();

			if (file.createNewFile()) {
				logger.info("File is created!");
			} else {
			logger.info(filename + ".html already exists.");
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
	public static Swagger parseSwaggerFile(String swaggerFile) {

		String swaggerFile1 = "d:\\Userfiles\\nghate\\Desktop\\swg.yml";

		return new SwaggerParser().read(swaggerFile1);
	}

	
}
