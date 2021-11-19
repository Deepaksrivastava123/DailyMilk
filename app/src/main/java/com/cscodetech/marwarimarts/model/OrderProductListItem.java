package com.cscodetech.marwarimarts.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderProductListItem{

	@SerializedName("Delivery_charge")
	private String deliveryCharge;

	@SerializedName("p_method_name")
	private String pMethodName;

	@SerializedName("customer_address")
	private String customerAddress;

	@SerializedName("Order_Status")
	private String orderStatus;

	@SerializedName("customer_mobile")
	private String customerMobile;

	@SerializedName("Order_Total")
	private String orderTotal;

	@SerializedName("Coupon_Amount")
	private String couponAmount;

	@SerializedName("Order_Product_Data")
	private List<OrderProductDataItem> orderProductData;

	@SerializedName("Additional_Note")
	private String additionalNote;

	@SerializedName("Order_Transaction_id")
	private String orderTransactionId;

	@SerializedName("Order_SubTotal")
	private String orderSubTotal;

	@SerializedName("customer_name")
	private String customerName;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("Wallet_Amount")
	private String WalletAmount;

	@SerializedName("Delivery_Timeslot")
	private String DeliveryTimeslot;

	public String getDeliveryCharge(){
		return deliveryCharge;
	}

	public String getPMethodName(){
		return pMethodName;
	}

	public String getCustomerAddress(){
		return customerAddress;
	}

	public String getOrderStatus(){
		return orderStatus;
	}

	public String getCustomerMobile(){
		return customerMobile;
	}

	public String getOrderTotal(){
		return orderTotal;
	}

	public String getCouponAmount(){
		return couponAmount;
	}

	public List<OrderProductDataItem> getOrderProductData(){
		return orderProductData;
	}

	public String getAdditionalNote(){
		return additionalNote;
	}

	public String getOrderTransactionId(){
		return orderTransactionId;
	}

	public String getOrderSubTotal(){
		return orderSubTotal;
	}

	public String getCustomerName(){
		return customerName;
	}

	public String getOrderId(){
		return orderId;
	}

	public String getWalletAmount() {
		return WalletAmount;
	}

	public void setWalletAmount(String walletAmount) {
		WalletAmount = walletAmount;
	}

	public String getDeliveryTimeslot() {
		return DeliveryTimeslot;
	}

	public void setDeliveryTimeslot(String deliveryTimeslot) {
		DeliveryTimeslot = deliveryTimeslot;
	}
}