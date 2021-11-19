package com.cscodetech.marwarimarts.model;

import com.google.gson.annotations.SerializedName;

public class Home{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResultData")
	private ResultData resultData;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;



	public String getResponseCode(){
		return responseCode;
	}

	public ResultData getResultData(){
		return resultData;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}