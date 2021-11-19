package com.cscodetech.marwarimarts.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class City{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("Cityitem")
	private ArrayList<CityitemItem> cityitem;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public ArrayList<CityitemItem> getCityitem(){
		return cityitem;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}