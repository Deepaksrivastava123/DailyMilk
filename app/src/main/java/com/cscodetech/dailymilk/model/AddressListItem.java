package com.cscodetech.dailymilk.model;

import com.google.gson.annotations.SerializedName;

public class AddressListItem{

	@SerializedName("pincode")
	private String pincode;

	@SerializedName("address")
	private String address;

	@SerializedName("long_map")
	private String longMap;

	@SerializedName("hno")
	private String hno;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("lat_map")
	private String latMap;

	@SerializedName("type")
	private String type;

	@SerializedName("address_image")
	private String addressImage;

	@SerializedName("uid")
	private String uid;

	@SerializedName("delivery_charge")
	private String deliveryCharge;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("landmark")
	private String landmark;

	public String getPincode(){
		return pincode;
	}

	public String getAddress(){
		return address;
	}

	public String getLongMap(){
		return longMap;
	}

	public String getHno(){
		return hno;
	}

	public String getMobile(){
		return mobile;
	}

	public String getLatMap(){
		return latMap;
	}

	public String getType(){
		return type;
	}

	public String getAddressImage(){
		return addressImage;
	}

	public String getUid(){
		return uid;
	}

	public String getDeliveryCharge(){
		return deliveryCharge;
	}

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}

	public String getLandmark(){
		return landmark;
	}
}