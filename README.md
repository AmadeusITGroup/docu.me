Docu.Me - Documentation Portal Generator
=================================
Docu.Me is a static site generator for API documentation built with Java and UX is designed with Bootstrap and Jquery.
It is based on the current version of the [Open API specification](https://github.com/OAI/OpenAPI-Specification) formerly known as Swagger.
You can create your own swagger.yaml file with [Swagger Editor](http://editor.swagger.io/#/)

This repo contains the content and specifications for the [Amadeus Travel Innovation Sandbox](https://sandbox.amadeus.com).  

This is a Spring boot project built with [Spring Tool Suite](https://spring.io/tools/sts/all) (STS) .
You can download/clone the repo and import it as an existing project into workspace in STS.
It is a Maven project so has all required dependencies in the pom.xml.

### Prerequisites
You need the following installed and available in your $PATH:

* [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Apache maven 3.0.3 or greater](http://maven.apache.org/install.html)

Simple [tutorial](https://www.mkyong.com/maven/how-to-install-maven-in-windows/) I followed, incase it helps !

## Set API key as environment variable.
You can follow this [tutorial](https://www.java.com/en/download/help/path.xml).
Enter variable name as "APIKEY" and value with your apikey.

### From terminal

Go on the project's root folder, then type:

	$ mvn spring-boot:run -Drun.arguments = "args1,args2"
	args1 = Swagger yaml file location. For example "d:\\Userfiles\\nghate\\Desktop\\swg.yml"
	args2 = This parameter should be *true* if you want to generate examples on the fly by callling the API with default values or set to *false*.
	Your command will look like $ mvn spring-boot:run -Drun.arguments = "d:\\Userfiles\\nghate\\Desktop\\swg.yml, false"

### From Eclipse (Spring Tool Suite)

* Import docuDemo folder as *Existing Maven Project*. Go to the DocuDemoApplication class and click Run configurations.
* Go to Arguments and pass  "args1,args2" as explained above.
* Go to Environment > New . Enter variable name as "APIKEY" and value with your apikey.
* Click Apply > Run.

## Output

* The output folder produced in your project folder contains index file with navigation to different APIs and response models of the API documentation.