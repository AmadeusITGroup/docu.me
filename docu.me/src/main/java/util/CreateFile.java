package main.java.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;
import main.java.pojo.ModelDetail;
import main.java.pojo.MyModel;

public class CreateFile {

	String dir = "d:\\Userfiles\\nghate\\Desktop\\genSmall\\models\\";

	public void create(String filename, String content) {
		try {

			// File file = new File("c:\\newfile.txt");

			File file = new File(dir, filename + ".html");

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println(filename+".html already exists.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Swagger swaggerParse() {
		Swagger swagger = new SwaggerParser().read("d:\\Userfiles\\nghate\\Desktop\\smallswg.yml");

		String swaggerString = Json.pretty(swagger);

		// System.out.println(swaggerString);

		return swagger;
	}

	public Map<String, Response> getReponses(Operation op) {

		Map<String, Response> responses = op.getResponses();
		Response res = null;
		for (Map.Entry<String, Response> entry : responses.entrySet()) {
//			System.out.println("Key = " + entry.getKey() + "value = " + entry.getValue());

			res = entry.getValue();
			// System.out.println(Json.pretty(responses));

		}

		return responses;

	}

	public void getApiTemplate(String name, String status) {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache apiTemplate = (Mustache) mf.compile("api.mustache");
		Mustache indexTemplate = (Mustache) mf.compile("index.mustache");

		HashMap<String, Object> apiScope = new HashMap<>();
		HashMap<String, Object> indexScope = new HashMap<>();
		
		List<String> operationId = new ArrayList<>();
		List<main.java.pojo.Response> responsesList = new ArrayList<>();
	
		StringWriter indexWriter = new StringWriter();
		Map<String, Path> pathMap = swaggerParse().getPaths();
		
		for (Map.Entry<String, Path> entry : pathMap.entrySet()) {
			Path path = entry.getValue();
			List<Operation> opList = path.getOperations();
			Map<String, Response> responses;
			String simpleRef = "noRef";
			for (Operation op : opList) {
				apiScope.put("operations", op);
				responses = getReponses(op);
				for (Map.Entry<String, Response> res : responses.entrySet()) {
					String resName = res.getKey();
					Response resValue = res.getValue();
					apiScope.put("resName", resName);
					apiScope.put("resValue", resValue.getDescription());
					main.java.pojo.Response response = new main.java.pojo.Response();
					response.setDescription(resValue.getDescription());
					Property p1 = resValue.getSchema();
					if (p1 instanceof ArrayProperty) {
						ArrayProperty ap = (ArrayProperty) p1;
						Property p2 = ap.getItems();
						RefProperty rp = (RefProperty) p2;
						String ref = rp.get$ref();
						 simpleRef =rp.getSimpleRef();
						response.setReference(ref);
						response.setResponseNumber(resName);
					} else {
						RefProperty rp = (RefProperty) p1;
						String ref = rp.get$ref();
						 simpleRef =rp.getSimpleRef();
						response.setReference(ref);
						response.setResponseNumber(resName);
					}
					responsesList.add(response);
					// resList.add(resValue);

				}
				displayModels("SenseResponse");
				apiScope.put("responses", responsesList);
				operationId.add(op.getOperationId());

				StringWriter apiWriter = new StringWriter();
				apiTemplate.execute(apiWriter, apiScope);
				create(op.getOperationId(), apiWriter.toString());

			}

		}
		indexScope.put("operationId", operationId);
		indexTemplate.execute(indexWriter, indexScope);
		create("index", indexWriter.toString());
		// Operation op = new Operation();
		// op.setOpId("1");
		// op.setOpName("flight");
		// op.setDescription("desc for flgt");

	}

	public void getModels() {

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = (Mustache) mf.compile("model.mustache");
		Mustache template1 = (Mustache) mf.compile("modelindex.mustache");
		HashMap<String, Object> currscope = new HashMap<>();
		Map<String, Model> modelMap = swaggerParse().getDefinitions();
		StringWriter writer = new StringWriter();
		StringWriter writer1 = new StringWriter();
		List<String> modelList = new ArrayList<>();
		List<String> modelNameList = new ArrayList<>();
		List<ModelDetail> mdList = new ArrayList<>();
		
		for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
//			System.out.println("Key = " + entry.getKey());
			modelList.add(entry.getKey());
			Model model = entry.getValue();
			ModelDetail md = new ModelDetail();
			md.setModelTitle(entry.getKey());
		    Map<String,Property> modelPropMap = model.getProperties();
		    if(modelPropMap!=null){
			for (Map.Entry<String, Property> modelProp : modelPropMap.entrySet()) {
				String sKey = modelProp.getKey();
				modelNameList.add(sKey);
				Property p = modelProp.getValue();
				List<MyModel> modelOb = md.getModelList();
//				System.out.println(p);
				currscope.put("models", p);
				currscope.put("modelName", entry.getKey()); // add into list n display as ul-li
				MyModel m = new MyModel();
				m.setModelDesc(p.getDescription());
				m.setModelName(sKey);
				m.setModelType(p.getType());
				modelOb.add(m);
				mdList.add(md);
//				System.out.println("desc "+p.getDescription());
//				System.out.println("name "+sKey);
//				System.out.println("type "+p.getType());
				}
			}
		   
			
//			System.out.println(Json.pretty(model));
		}
		 currscope.put("newModel", mdList);
		 System.out.println(Json.pretty(mdList));
		    currscope.put("modelKey", modelNameList);
			template.execute(writer, currscope);
		currscope.put("modelList", modelList);
		template1.execute(writer1, currscope);
		
		create("model", writer.toString());
		create("modelindex", writer1.toString());
	}

	public void displayModels(String simpleRef)
	{
		
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = (Mustache) mf.compile("model.mustache");
		HashMap<String, Object> currscope = new HashMap<>();
		Map<String, Model> modelMap = swaggerParse().getDefinitions();
		
		List<String> modelList = new ArrayList<>();
		List<String> modelNameList = new ArrayList<>();
		List<ModelDetail> mdList = new ArrayList<>();
		List<MyModel> modelOb = null;
		String title = null;
		ModelDetail md = null;
		
		for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
			title = entry.getKey();
			Model model = entry.getValue();
			 md = new ModelDetail();
			md.setModelTitle(entry.getKey());
		    Map<String,Property> modelPropMap = model.getProperties();
		    
		    if(modelPropMap!=null){
			for (Map.Entry<String, Property> modelProp : modelPropMap.entrySet()) {
				String sKey = modelProp.getKey();
				Property p = modelProp.getValue();
				modelOb = md.getModelList();
				currscope.put("refName", entry.getKey()); // add into list n display as ul-li
				MyModel m = new MyModel();
				m.setModelDesc(p.getDescription());
				m.setModelName(sKey);
				m.setModelType(p.getType());
				modelOb.add(m);
				md.setModelList(modelOb);
				
				}
			mdList.add(md);
			//if(entry.getKey().equals(simpleRef)){
//			createExample(modelOb,entry.getKey());
			//}
			
			
			// send this object if entry.getKey matches with #defnitions simpleRef 1 {modelOb}
			
			}
		   
		    
		}
		
		currscope.put("obj", modelOb);
		currscope.put("List",mdList);
		StringWriter writer = new StringWriter();
		template.execute(writer, currscope);
	    create("modelnew", writer.toString());
		
	}

	public void createExample(List<MyModel> modelOb, String modelTitle) {
		// TODO Auto-generated method stub

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache template = (Mustache) mf.compile("exampleJson.mustache");
		HashMap<String, Object> currscope = new HashMap<>();
		StringWriter writer = new StringWriter();
		
		currscope.put("obj", modelOb);
		currscope.put("title", modelTitle);
		template.execute(writer, currscope);
		create("exampleJson", writer.toString());
		
		
	}
	
	
}
