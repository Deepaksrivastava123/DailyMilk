package com.cscodetech.dailymilk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BannerItem implements Parcelable {

	@SerializedName("img")
	private String img;

	@SerializedName("id")
	private String id;

	@SerializedName("cat_id")
	private String catId;

	@SerializedName("title")
	private String Title;

	@SerializedName("cat_img")
	private String catImg;

	protected BannerItem(Parcel in) {
		img = in.readString();
		id = in.readString();
		catId = in.readString();
		Title = in.readString();
		catImg = in.readString();
	}

	public static final Creator<BannerItem> CREATOR = new Creator<BannerItem>() {
		@Override
		public BannerItem createFromParcel(Parcel in) {
			return new BannerItem(in);
		}

		@Override
		public BannerItem[] newArray(int size) {
			return new BannerItem[size];
		}
	};

	public String getImg(){
		return img;
	}

	public String getId(){
		return id;
	}


	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getCatImg() {
		return catImg;
	}

	public void setCatImg(String catImg) {
		this.catImg = catImg;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(img);
		dest.writeString(id);
		dest.writeString(catId);
		dest.writeString(Title);
		dest.writeString(catImg);
	}
}