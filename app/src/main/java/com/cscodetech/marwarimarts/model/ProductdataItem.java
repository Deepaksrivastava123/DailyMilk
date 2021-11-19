package com.cscodetech.marwarimarts.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ProductdataItem implements Parcelable {

	@SerializedName("catid")
	private String catid;

	@SerializedName("product_variation")
	private String productVariation;

	@SerializedName("product_discount")
	private String productDiscount;

	@SerializedName("product_regularprice")
	private String productRegularprice;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("cityid")
	private String cityid;

	@SerializedName("product_subscribeprice")
	private String productSubscribeprice;

	@SerializedName("subcatid")
	private String subcatid;

	@SerializedName("product_title")
	private String productTitle;

	@SerializedName("product_img")
	private String productImg;

	@SerializedName("catname")
	private String catname;

	private String productQty;
	private String day;
	private String tdelivery;
	private String sdate;
	private String type;
	private String tdeliverydigit;
	private String stime;

	public ProductdataItem(Parcel in) {
		catid = in.readString();
		productVariation = in.readString();
		productDiscount = in.readString();
		productRegularprice = in.readString();
		productId = in.readString();
		cityid = in.readString();
		productSubscribeprice = in.readString();
		subcatid = in.readString();
		productTitle = in.readString();
		productImg = in.readString();
		productQty = in.readString();
		day = in.readString();
		tdelivery = in.readString();
		sdate = in.readString();
		type = in.readString();
		tdeliverydigit = in.readString();
		catname = in.readString();
		stime = in.readString();
	}
	public ProductdataItem() {

	}
	public static final Creator<ProductdataItem> CREATOR = new Creator<ProductdataItem>() {
		@Override
		public ProductdataItem createFromParcel(Parcel in) {
			return new ProductdataItem(in);
		}

		@Override
		public ProductdataItem[] newArray(int size) {
			return new ProductdataItem[size];
		}
	};

	public String getCatid(){
		return catid;
	}

	public String getProductVariation(){
		return productVariation;
	}

	public String getProductDiscount(){
		return productDiscount;
	}

	public String getProductRegularprice(){
		return productRegularprice;
	}

	public String getProductId(){
		return productId;
	}

	public String getCityid(){
		return cityid;
	}

	public String getProductSubscribeprice(){
		return productSubscribeprice;
	}

	public String getSubcatid(){
		return subcatid;
	}

	public String getProductTitle(){
		return productTitle;
	}

	public String getProductImg(){
		return productImg;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(catid);
		dest.writeString(productVariation);
		dest.writeString(productDiscount);
		dest.writeString(productRegularprice);
		dest.writeString(productId);
		dest.writeString(cityid);
		dest.writeString(productSubscribeprice);
		dest.writeString(subcatid);
		dest.writeString(productTitle);
		dest.writeString(productImg);
		dest.writeString(productQty);
		dest.writeString(day);
		dest.writeString(tdelivery);
		dest.writeString(sdate);
		dest.writeString(type);
		dest.writeString(tdeliverydigit);
		dest.writeString(catname);
		dest.writeString(stime);
	}

	public String getProductQty() {
		return productQty;
	}

	public void setProductQty(String productQty) {
		this.productQty = productQty;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public void setProductVariation(String productVariation) {
		this.productVariation = productVariation;
	}

	public void setProductDiscount(String productDiscount) {
		this.productDiscount = productDiscount;
	}

	public void setProductRegularprice(String productRegularprice) {
		this.productRegularprice = productRegularprice;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public void setProductSubscribeprice(String productSubscribeprice) {
		this.productSubscribeprice = productSubscribeprice;
	}

	public void setSubcatid(String subcatid) {
		this.subcatid = subcatid;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTdelivery() {
		return tdelivery;
	}

	public void setTdelivery(String tdelivery) {
		this.tdelivery = tdelivery;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTdeliverydigit() {
		return tdeliverydigit;
	}

	public void setTdeliverydigit(String tdeliverydigit) {
		this.tdeliverydigit = tdeliverydigit;
	}

	public String getCatname() {
		return catname;
	}

	public void setCatname(String catname) {
		this.catname = catname;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}
}