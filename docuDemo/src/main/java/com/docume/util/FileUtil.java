package com.docume.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileUtil {

	
	static final Logger logger = Logger.getLogger(Documentation.class);

	public static void createFile(String filename, String content) {
		try {

			File filedir = new File("Portal\\docs\\");
			filedir.mkdirs();
			File file = new File(filedir, filename + ".html");
			logger.info(filename + ".html is created!");
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(content);
			bufferedWriter.close();
			fileWriter.close();

		} catch (IOException e) {
			logger.error(e);
		}
	}

	public  void createLib() throws IOException {
		
		File destDir = new File("portal\\lib\\");
		ClassLoader classLoader = getClass().getClassLoader();
		File srcCSSFile = new File(classLoader.getResource("static/jquery.json-view.css").getFile());
		File srcJSFile = new File(classLoader.getResource("static/jquery.json-view.js").getFile());

		FileUtils.copyFileToDirectory(srcCSSFile, destDir);
		FileUtils.copyFileToDirectory(srcJSFile, destDir);

	}

	public static boolean toBoolean(String s) {
		return Boolean.parseBoolean(s);
	}

}
