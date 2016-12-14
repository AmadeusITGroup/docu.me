package com.docume.pojo;


public class MyModel {


	private String name;
	private String description;
	private String type;
	private String referenceModel;
	

	public String getModelRef() {
		return referenceModel;
	}

	public void setModelRef(String modelRef) {
		this.referenceModel = modelRef;
	}

	public String getModelName() {
		return name;
	}

	public void setModelName(String modelName) {
		this.name = modelName;
	}

	public String getModelDesc() {
		return description;
	}

	public void setModelDesc(String modelDesc) {
		this.description = modelDesc;
	}

	public String getModelType() {
		return type;
	}

	public void setModelType(String modelType) {
		this.type = modelType;
	}

	

}
