package com.docume.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.docume.pojo.ModelDetail;
import com.docume.pojo.MyModel;
import com.docume.util.Documentation.MustacheVariables;
import com.docume.util.Documentation.Template;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

public class ResponseModelData {

	// create models

	public Map<String, JSONObject> createResponseJsonSchema(Swagger swagger) {
		List<ModelDetail> modelDetailsList = createModelList(swagger);
		HashMap<String, JSONObject> jsonResponseMap = new HashMap<>();

		for (ModelDetail modelDetail : modelDetailsList) {
			JSONObject jObject = new JSONObject();
			List<MyModel> modelList = modelDetail.getModelList();
			for (MyModel model : modelList) {
				if ("ref".equals(model.getModelType()) || "array".equals(model.getModelType())) {
					String ref = model.getModelRef();
					JSONObject internalrefObject = iterateJson(modelDetailsList, ref);
					jObject.put(model.getModelName(), internalrefObject);
				} else {
					jObject.put(model.getModelName(), model.getModelDesc());
				}
			}
			jsonResponseMap.put(modelDetail.getModelTitle(), jObject);
		}
		return jsonResponseMap;
	}

	/**
	 * @param swagger
	 */
	public List<ModelDetail> createModelList(Swagger swagger) {

		Map<String, Model> modelMap = swagger.getDefinitions();
		List<ModelDetail> modelDetailsList = new ArrayList<>();

		for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
			ModelDetail modelDetail = createModel(entry);
			modelDetailsList.add(modelDetail);
		}

		return modelDetailsList;

	}

	private JSONObject iterateJson(List<ModelDetail> modelDetailsList, String ref) {
		JSONObject jObject = new JSONObject();

		for (ModelDetail modelDetail : modelDetailsList) {

			if (modelDetail.getModelTitle().equals(ref)) {
				List<MyModel> modelList = modelDetail.getModelList();
				for (MyModel model : modelList) {
					if ("ref".equals(model.getModelType())) {
						String reference = model.getModelRef();
						JSONObject internalrefObject = iterateJson(modelDetailsList, reference);
						jObject.put(model.getModelName(), internalrefObject);
					} else {

						jObject.put(model.getModelName(), model.getModelDesc());
					}
				}
			}
		}
		return jObject;

	}

	public String createModelTemplate(Swagger swagger) {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache modelTemplate = mf.compile(Template.MODEL);

		HashMap<String, Object> modelScope = buildModelScope(swagger);

		StringWriter writer = new StringWriter();
		modelTemplate.execute(writer, modelScope);

		return writer.toString();
	}

	/**
	 * @param swagger
	 * @return
	 */
	private HashMap<String, Object> buildModelScope(Swagger swagger) {
		HashMap<String, Object> modelScope = new HashMap<>();
		List<ModelDetail> modelDetailsList = createModelList(swagger);
		modelScope.put("modelDetailsList", modelDetailsList);
		
		String info = swagger.getInfo().getTitle();
		modelScope.put("info", info);
		return modelScope;
	}

	/**
	 * @param modelMap
	 * @return
	 */
	private ModelDetail createModel(Map.Entry<String, Model> modelMap) {
		List<Model> modelList;
		Model model = modelMap.getValue();
		ModelDetail modelDetail = null;
		if (model instanceof ComposedModel) {
			ComposedModel cm = (ComposedModel) model;
			modelList = cm.getAllOf();
			for (Model m : modelList) {
				Map<String, Property> modelPropMap = m.getProperties();
				modelDetail = createModelProperties(modelPropMap);
			}
		} else {
			Map<String, Property> modelPropMap = model.getProperties();
			modelDetail = createModelProperties(modelPropMap);
		}

		modelDetail.setModelTitle(modelMap.getKey());
		return modelDetail;

	}

	/**
	 * @param modelPropertyMap
	 * @return
	 */
	private ModelDetail createModelProperties(Map<String, Property> modelPropertyMap) {
		List<MyModel> myModelList;
		ModelDetail modelDetail = new ModelDetail();

		if (modelPropertyMap != null) {
			for (Map.Entry<String, Property> modelProp : modelPropertyMap.entrySet()) {
				String modelName = modelProp.getKey();
				Property property = modelProp.getValue();
				myModelList = modelDetail.getModelList();

				MyModel myModel = new MyModel();
				myModel.setModelDesc(property.getDescription());
				myModel.setModelName(modelName);
				myModel.setModelType(property.getType());
				RefProperty refProperty;
				if (property instanceof RefProperty) {
					RefProperty rp = (RefProperty) property;
					String simpleReference = rp.getSimpleRef();
					myModel.setModelRef(simpleReference);
				} else if (property instanceof ArrayProperty) {
					ArrayProperty arrayProperty = (ArrayProperty) property;
					Property internalProperty = arrayProperty.getItems();
					if (internalProperty instanceof RefProperty) {
						refProperty = (RefProperty) internalProperty;
						String simpleReference = refProperty.getSimpleRef();
						myModel.setModelRef(simpleReference);
					}
				}
				myModelList.add(myModel);

				modelDetail.setModelList(myModelList);
			}
		}
		return modelDetail;
	}

	public void buildResponseModelPage(Swagger swagger) {
		// Creating the response model page
		String modelHtmlFile = createModelTemplate(swagger);
		FileUtil.createFile(MustacheVariables.MODEL, modelHtmlFile);

	}
}
