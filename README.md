Docu.Me - Documentation Portal Generator
=================================
Docu.Me is a static site generator for API documentation built with Java and UX is designed with Bootstrap and Jquery.
It is based on the current version of the [Open API specification](https://github.com/OAI/OpenAPI-Specification) formerly known as Swagger.

This repo contains the content and specifications for the [Amadeus Travel Innovation Sandbox](https://sandbox.amadeus.com).  

This is a Spring boot project built with [Spring Tool Suite](https://spring.io/tools/sts/all) (STS) .
You can download/clone the repo and import it as an existing project into workspace in STS.
It is a Maven project so has all required dependencies in the pom.xml.

### Prerequisites
You need the following installed and available in your $PATH:

* [Java 7](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Apache maven 3.0.3 or greater](http://maven.apache.org/install.html)

Simple [tutorial](https://www.mkyong.com/maven/how-to-install-maven-in-windows/) I followed, incase it helps !

### From terminal

Go on the project's root folder, then type:

	$ mvn spring-boot:run -Drun.arguments = "args1,args2"
	

### From Eclipse (Spring Tool Suite)

* Import docuDemo folder as *Existing Maven Project*.Go to the DocuDemoApplication class and Run it as *Spring Boot App*.
* Run it as a spring boot application, you will be asked for swagger.yaml file location.

## Output

* The output folder produced in your project folder contains index file with navigation to different operations and models of the API documentation.