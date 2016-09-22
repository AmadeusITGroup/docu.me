package pegdown;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;
import io.swagger.models.parameters.Parameter;

public class Generate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CreateFile cf = new CreateFile();
//		cf.create("new1");

		Swagger sw = cf.swaggerParse();

		Map<String, Model> tList = sw.getDefinitions();
		// List<String> consumesList = sw.getConsumes();
		
//		Map<String,Parameter> paramList = sw.getParameters();
		
		
		
//		System.out.println(pathMap);
		
		cf.getApiTemplate("Neha", "active");
		
//		cf.displayModels();
	}

}
