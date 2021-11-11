package com.cscodetech.dailymilk.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Address{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("AddressList")
	private List<AddressListItem> addressList;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<AddressListItem> getAddressList(){
		return addressList;
	}

	public String getResult(){
		return result;
	}
}