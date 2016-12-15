package com.docume.pojo;

import java.util.ArrayList;
import java.util.List;

public class ModelDetail {

	private String modelTitle;

	private List<ModelParameter> modelParameterList;

	public ModelDetail() {

		modelParameterList = new ArrayList<>();
	}

	public String getModelTitle() {
		return modelTitle;
	}

	public void setModelTitle(String modelTitle) {
		this.modelTitle = modelTitle;
	}

	public List<ModelParameter> getModelParameterList() {
		return modelParameterList;
	}

	public void setModelParameterList(List<ModelParameter> modelParameterList) {
		this.modelParameterList = modelParameterList;
	}

	public void addModels() {
		ModelParameter mm = new ModelParameter();
		modelParameterList.add(mm);
	}
}
