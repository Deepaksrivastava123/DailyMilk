package com.cscodetech.marwarimarts.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Country{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("CountryCode")
	private List<CountryCodeItem> countryCode;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<CountryCodeItem> getCountryCode(){
		return countryCode;
	}

	public String getResult(){
		return result;
	}
}