package com.cscodetech.marwarimarts.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResultData{

	@SerializedName("wallet")
	private String wallet;

	@SerializedName("Main_Data")
	private MainData mainData;

	@SerializedName("Banner")
	private List<BannerItem> banner;

	@SerializedName("Couponlist")
	private ArrayList<CouponlistItem> couponlist;

	@SerializedName("Catlist")
	private ArrayList<CatlistItem> catlist;

	@SerializedName("Collection")
	private ArrayList<CollectionItem> collection;

	@SerializedName("f_stock")
	private ArrayList<ProductdataItem> fStock;

	public MainData getMainData(){
		return mainData;
	}

	public List<BannerItem> getBanner(){
		return banner;
	}

	public ArrayList<CouponlistItem> getCouponlist(){
		return couponlist;
	}

	public ArrayList<CatlistItem> getCatlist(){
		return catlist;
	}

	public ArrayList<CollectionItem> getCollection(){
		return collection;
	}

	public ArrayList<ProductdataItem> getFStock(){
		return fStock;
	}

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}
}