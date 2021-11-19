package com.cscodetech.marwarimarts.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Delivery{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Deliverylist")
	private List<DeliverylistItem> deliverylist;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<DeliverylistItem> getDeliverylist(){
		return deliverylist;
	}

	public String getResult(){
		return result;
	}
}