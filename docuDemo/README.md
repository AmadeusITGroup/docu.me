Docu.Me - Documentation Portal Generator
=================================
Docu.Me is a static site generator for API documentation and is based on the current version of the [Open API specification](https://github.com/OAI/OpenAPI-Specification) f.k.a Swagger.

This repo contains the content and specifications for the [Amadeus Travel Innovation Sandbox](https://sandbox.amadeus.com).  

This is a Spring boot project built with [Spring Tool Suite](https://spring.io/tools) (STS) . You can download/clone the repo and import it as an existing project into workspace.
It is a Maven project so has all required dependencies in the pom.xml.

### Prerequisites
You need the following installed and available in your $PATH:

* [Java 7](http://java.oracle.com)
* [Apache maven 3.0.3 or greater](http://maven.apache.org/)

### From terminal

Go on the project's root folder, then type:

	$ mvn spring-boot:run

### From Eclipse (Spring Tool Suite)

* Import as *Existing Maven Project* and run it as *Spring Boot App*.
* Run it as a spring boot application, you will be asked for swagger.yaml file location.

## Output

* The output folder contains index file with navigation to different operations and models of the API documentation.