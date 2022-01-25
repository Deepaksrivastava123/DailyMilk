package com.cscodetech.marwarimarts.model;

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

	public void setDeDigit(String deDigit) {
		this.deDigit = deDigit;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean select;

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}
}