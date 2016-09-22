package pegdown;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;

public class ParseSwagger {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// read a swagger description from the petstore
		  Swagger swagger = new SwaggerParser().read("d:\\Userfiles\\nghate\\Desktop\\swg.yml");
		  
		  String swaggerString = Json.pretty(swagger);
		  
		  System.out.println(swaggerString);
		
	}

}
