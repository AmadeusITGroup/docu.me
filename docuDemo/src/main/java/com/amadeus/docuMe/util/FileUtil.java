package com.amadeus.docuMe.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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

	public static void createLib() throws IOException {
		File destDir = new File("Portal\\lib\\");
		File srcCSSFile = new File("src\\main\\resources\\static\\jquery.json-view.css");

		File srcJSFile = new File("src\\main\\resources\\static\\jquery.json-view.js");

		FileUtils.copyFileToDirectory(srcCSSFile, destDir);
		FileUtils.copyFileToDirectory(srcJSFile, destDir);

	}

	public static boolean toBoolean(String s) {
			 return Boolean.parseBoolean(s); 
	 }
	 

}
