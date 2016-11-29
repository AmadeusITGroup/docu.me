package com.amadeus.docume.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileUtil {

	private FileUtil(){
		
	}
	static final  Logger logger = Logger.getLogger(Documentation.class);

	public static void createFile(String filename, String content) {
		try {

			File filedir = new File("Portal\\docs\\");
			filedir.mkdirs();
			File file = new File(filedir, filename + ".html");

			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(content);
			bufferedWriter.close();
			fileWriter.close();

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
