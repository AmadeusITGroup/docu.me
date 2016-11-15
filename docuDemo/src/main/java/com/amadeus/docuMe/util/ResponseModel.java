package com.amadeus.docuMe.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amadeus.docuMe.pojo.ModelDetail;
import com.amadeus.docuMe.pojo.MyModel;
import com.amadeus.docuMe.util.Documentation.Template;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.swagger.models.ComposedModel;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;

public class ResponseModel {

	// create models
	/**
	 * @param swaggerObj
	 */
	public void createModelTemplate(Swagger swaggerObj) {

		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache modelTemplate = mf.compile(Template.MODEL);
		HashMap<String, Object> modelScope = new HashMap<>();
		Map<String, Model> modelMap = swaggerObj.getDefinitions();

		List<ModelDetail> modelDetailsList = new ArrayList<>();

		List<MyModel> myModelList;
		ModelDetail modelDetail;

		for (Map.Entry<String, Model> entry : modelMap.entrySet()) {
			modelDetail = iterateModels(entry);
			modelDetailsList.add(modelDetail);
		}
		modelScope.put("modelDetailsList", modelDetailsList);

		StringWriter writer = new StringWriter();
		modelTemplate.execute(writer, modelScope);
		FileUtil.createFile("modelnew", writer.toString());

	}

	/**
	 * @param modelMap
	 * @return
	 */
	private ModelDetail iterateModels(Map.Entry<String, Model> modelMap) {
		List<Model> modelList;
		Model model = modelMap.getValue();
		ModelDetail modelDetail = null ;
		if (model instanceof ComposedModel) {
			ComposedModel cm = (ComposedModel) model;
			modelList = cm.getAllOf();
			for (Model m : modelList) {
				Map<String, Property> modelPropMap = m.getProperties();
				modelDetail = iterateModelProperties(modelPropMap);
			}
		} else {
			Map<String, Property> modelPropMap = model.getProperties();
			modelDetail = iterateModelProperties(modelPropMap);
		}

		modelDetail.setModelTitle(modelMap.getKey());
		return modelDetail;

	}

	/**
	 * @param modelPropertyMap
	 * @return
	 */
	private ModelDetail iterateModelProperties(Map<String, Property> modelPropertyMap) {
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
				RefProperty r;
				if (property instanceof RefProperty) {
					RefProperty rp = (RefProperty) property;
					String simpleReference = rp.getSimpleRef();
					myModel.setModelRef(simpleReference);
				} else if (property instanceof ArrayProperty) {
					ArrayProperty ap = (ArrayProperty) property;
					Property ap1 = ap.getItems();
					if (ap1 instanceof RefProperty) {
						r = (RefProperty) ap1;
						String simpleReference = r.getSimpleRef();
						myModel.setModelDesc(simpleReference);
					}
				}
				myModelList.add(myModel);

				modelDetail.setModelList(myModelList);
			}
		}
		return modelDetail;
	}
}
