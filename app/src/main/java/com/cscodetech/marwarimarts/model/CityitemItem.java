package com.cscodetech.marwarimarts.model;

import com.google.gson.annotations.SerializedName;

public class CityitemItem{

	@SerializedName("id")
	private String id;



	@SerializedName("title")
	private String title;

	@SerializedName("cimg")
	private String cimg;

	@SerializedName("status")
	private String status;

	@SerializedName("total_product")
	private String totalProduct;

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getCimg(){
		return cimg;
	}

	public String getStatus(){
		return status;
	}


	public boolean select;

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}


	public String getTotalProduct() {
		return totalProduct;
	}

	public void setTotalProduct(String totalProduct) {
		this.totalProduct = totalProduct;
	}
}