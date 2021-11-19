package com.cscodetech.marwarimarts.model;

import com.google.gson.annotations.SerializedName;

public class TotaldatesItem{

	@SerializedName("date")
	private String date;

	@SerializedName("is_complete")
	private int isComplete;

	@SerializedName("format_date")
	private String formatDate;

	public String getDate(){
		return date;
	}

	public int getIsComplete(){
		return isComplete;
	}

	private int isSelect;

	public int getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public String getFormatDate(){
		return formatDate;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setIsComplete(int isComplete) {
		this.isComplete = isComplete;
	}

	public void setFormatDate(String formatDate) {
		this.formatDate = formatDate;
	}
}