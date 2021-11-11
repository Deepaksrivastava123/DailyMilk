package com.cscodetech.dailymilk.model;

import com.google.gson.annotations.SerializedName;

public class OrderHistoryItem{

	@SerializedName("date")
	private String date;

	@SerializedName("total_product")
	private int totalProduct;

	@SerializedName("total")
	private String total;

	@SerializedName("Delivery_name")
	private String deliveryName;

	@SerializedName("id")
	private String id;

	@SerializedName("status")
	private String status;

	public String getDate(){
		return date;
	}

	public int getTotalProduct(){
		return totalProduct;
	}

	public String getTotal(){
		return total;
	}

	public String getDeliveryName(){
		return deliveryName;
	}

	public String getId(){
		return id;
	}

	public String getStatus(){
		return status;
	}
}