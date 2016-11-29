package com.amadeus.docume.pojo;


import java.util.ArrayList;
import java.util.List;

public class ModelDetail {

	private String modelTitle;
	
	private List<MyModel>	modelList;
	
	
	public ModelDetail() {
		
		modelList = new ArrayList<>();
	}


	public String getModelTitle() {
		return modelTitle;
	}


	public void setModelTitle(String modelTitle) {
		this.modelTitle = modelTitle;
	}


	public List<MyModel> getModelList() {
		return modelList;
	}


	public void setModelList(List<MyModel> modelList) {
		this.modelList = modelList;
	}
	
	
	public void addModels(){
		MyModel mm = new MyModel();
		modelList.add(mm);
	}
}
