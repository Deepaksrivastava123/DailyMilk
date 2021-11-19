package com.cscodetech.marwarimarts.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderProductDataItem{

	@SerializedName("Product_quantity")
	private String productQuantity;

	@SerializedName("totaldates")
	private List<TotaldatesItem> totaldates;

	@SerializedName("totaldelivery")
	private String totaldelivery;

	@SerializedName("Product_image")
	private String productImage;

	@SerializedName("Product_variation")
	private String productVariation;

	@SerializedName("Product_price")
	private String productPrice;

	@SerializedName("Product_name")
	private String productName;

	@SerializedName("Product_discount")
	private String productDiscount;

	@SerializedName("Product_total")
	private double productTotal;

	@SerializedName("startdate")
	private String startdate;

	@SerializedName("Subscribe_Id")
	private String SubscribeId;

	@SerializedName("Delivery_Timeslot")
	private String DeliveryTimeslot;

	public String getProductQuantity(){
		return productQuantity;
	}

	public List<TotaldatesItem> getTotaldates(){
		return totaldates;
	}

	public String getTotaldelivery(){
		return totaldelivery;
	}

	public String getProductImage(){
		return productImage;
	}

	public String getProductVariation(){
		return productVariation;
	}

	public String getProductPrice(){
		return productPrice;
	}

	public String getProductName(){
		return productName;
	}

	public String getProductDiscount(){
		return productDiscount;
	}

	public double getProductTotal(){
		return productTotal;
	}

	public String getStartdate(){
		return startdate;
	}

	public String getSubscribeId() {
		return SubscribeId;
	}

	public void setSubscribeId(String subscribeId) {
		SubscribeId = subscribeId;
	}

	public String getDeliveryTimeslot() {
		return DeliveryTimeslot;
	}

	public void setDeliveryTimeslot(String deliveryTimeslot) {
		DeliveryTimeslot = deliveryTimeslot;
	}
}