package com.docume.pojo;

public class ModelParameter {

	private String name;
	private String description;
	private String type;
	private String referenceModel;

	public String getReferenceModel() {
		return referenceModel;
	}

	public void setReferenceModel(String modelRef) {
		this.referenceModel = modelRef;
	}

	public String getName() {
		return name;
	}

	public void setName(String modelName) {
		this.name = modelName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String modelDesc) {
		this.description = modelDesc;
	}

	public String getType() {
		return type;
	}

	public void setType(String modelType) {
		this.type = modelType;
	}

}
