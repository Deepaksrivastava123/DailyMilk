package com.cscodetech.dailymilk.model;

import com.google.gson.annotations.SerializedName;

public class DeliverylistItem{

	@SerializedName("de_digit")
	private String deDigit;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("status")
	private String status;

	public String getDeDigit(){
		return deDigit;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
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
}