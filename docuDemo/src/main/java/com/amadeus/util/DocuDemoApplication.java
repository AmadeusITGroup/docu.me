package com.amadeus.util;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocuDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocuDemoApplication.class, args);
		Documentation cf = new Documentation();

		 // create a scanner so we can read the command-line input
	    Scanner scanner = new Scanner(System.in);

	    //  prompt for the file location
	    System.out.print("Enter your Swagger.yaml file location: ");
	    

	    // get their input as a String
	    String swaggerFile = scanner.next();
		
//		cf.createIndexApiTemplate(swaggerFile);
//		cf.createModelTemplate(swaggerFile);
		scanner.close();
		
		RequestForm rf = new RequestForm();
//		rf.getReq();
		rf.createRequestForm();
		
		System.out.print("The docs are generated in this location: /docuDemo/Portal");
	}
}
