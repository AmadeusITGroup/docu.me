package com.docume.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.docume.pojo.ModelDetail;
import com.docume.pojo.ModelParameter;
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

	@SuppressWarnings("unchecked") //Remove JSONObject untyped warnings
	public Map<String, JSONObject> createResponseJsonSchema(Swagger swagger) {
		List<ModelDetail> modelDetailsList = createModelList(swagger);
		HashMap<String, JSONObject> jsonResponseMap = new HashMap<>();

		for (ModelDetail modelDetail : modelDetailsList) {
			JSONObject jObject = new JSONObject();
			List<ModelParameter> modelList = modelDetail.getModelParameterList();
			for (ModelParameter model : modelList) {
				if ("ref".equals(model.getType()) || "array".equals(model.getType())) {
					String ref = model.getReferenceModel();
					JSONObject internalrefObject = iterateJson(modelDetailsList, ref);
					jObject.put(model.getName(), internalrefObject);
				} else {
					jObject.put(model.getName(), model.getDescription());
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

	@SuppressWarnings("unchecked") //Remove JSONObject untyped warnings
	private JSONObject iterateJson(List<ModelDetail> modelDetailsList, String ref) {
		JSONObject jObject = new JSONObject();

		for (ModelDetail modelDetail : modelDetailsList) {

			if (modelDetail.getModelTitle().equals(ref)) {
				List<ModelParameter> modelList = modelDetail.getModelParameterList();
				for (ModelParameter model : modelList) {
					if ("ref".equals(model.getType())) {
						String reference = model.getReferenceModel();
						JSONObject internalrefObject = iterateJson(modelDetailsList, reference);
						jObject.put(model.getName(), internalrefObject);
					} else {

						jObject.put(model.getName(), model.getDescription());
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
		List<ModelParameter> myModelList;
		ModelDetail modelDetail = new ModelDetail();

		if (modelPropertyMap != null) {
			for (Map.Entry<String, Property> modelProp : modelPropertyMap.entrySet()) {
				String modelName = modelProp.getKey();
				Property property = modelProp.getValue();
				myModelList = modelDetail.getModelParameterList();

				ModelParameter myModel = new ModelParameter();
				myModel.setDescription(property.getDescription());
				myModel.setName(modelName);
				myModel.setType(property.getType());
				RefProperty refProperty;
				if (property instanceof RefProperty) {
					RefProperty rp = (RefProperty) property;
					String simpleReference = rp.getSimpleRef();
					myModel.setReferenceModel(simpleReference);
				} else if (property instanceof ArrayProperty) {
					ArrayProperty arrayProperty = (ArrayProperty) property;
					Property internalProperty = arrayProperty.getItems();
					if (internalProperty instanceof RefProperty) {
						refProperty = (RefProperty) internalProperty;
						String simpleReference = refProperty.getSimpleRef();
						myModel.setReferenceModel(simpleReference);
					}
				}
				myModelList.add(myModel);

				modelDetail.setModelParameterList(myModelList);
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
