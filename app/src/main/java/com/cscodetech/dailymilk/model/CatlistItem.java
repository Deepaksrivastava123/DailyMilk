package com.cscodetech.dailymilk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CatlistItem implements Parcelable {

	@SerializedName("cat_img")
	private String catImg;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	public CatlistItem(String catImg, String id, String title) {
		this.catImg = catImg;
		this.id = id;
		this.title = title;
	}

	public CatlistItem(Parcel in) {
		catImg = in.readString();
		id = in.readString();
		title = in.readString();
	}

	public static final Creator<CatlistItem> CREATOR = new Creator<CatlistItem>() {
		@Override
		public CatlistItem createFromParcel(Parcel in) {
			return new CatlistItem(in);
		}

		@Override
		public CatlistItem[] newArray(int size) {
			return new CatlistItem[size];
		}
	};

	public String getCatImg(){
		return catImg;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(catImg);
		dest.writeString(id);
		dest.writeString(title);
	}

	public void setCatImg(String catImg) {
		this.catImg = catImg;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}