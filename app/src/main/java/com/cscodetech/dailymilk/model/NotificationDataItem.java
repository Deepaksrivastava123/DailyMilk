package com.cscodetech.dailymilk.model;

import com.google.gson.annotations.SerializedName;

public class NotificationDataItem{

	@SerializedName("uid")
	private String uid;

	@SerializedName("datetime")
	private String datetime;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	public String getUid(){
		return uid;
	}

	public String getDatetime(){
		return datetime;
	}

	public String getDescription(){
		return description;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}