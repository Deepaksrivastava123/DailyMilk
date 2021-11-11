package com.cscodetech.dailymilk.model;

import com.google.gson.annotations.SerializedName;

public class SubOrder{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("OrderProductList")
	private OrderProductListItem orderProductList;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public OrderProductListItem getOrderProduct(){
		return orderProductList;
	}

	public String getResult(){
		return result;
	}
}