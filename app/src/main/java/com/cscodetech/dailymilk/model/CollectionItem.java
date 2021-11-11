package com.cscodetech.dailymilk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CollectionItem implements Parcelable {

	@SerializedName("image")
	private String image;

	@SerializedName("productdata")
	private ArrayList<ProductdataItem> productdata;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	protected CollectionItem(Parcel in) {
		image = in.readString();
		productdata = in.createTypedArrayList(ProductdataItem.CREATOR);
		id = in.readString();
		title = in.readString();
	}

	public static final Creator<CollectionItem> CREATOR = new Creator<CollectionItem>() {
		@Override
		public CollectionItem createFromParcel(Parcel in) {
			return new CollectionItem(in);
		}

		@Override
		public CollectionItem[] newArray(int size) {
			return new CollectionItem[size];
		}
	};

	public String getImage(){
		return image;
	}

	public ArrayList<ProductdataItem> getProductdata(){
		return productdata;
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
		dest.writeString(image);
		dest.writeTypedList(productdata);
		dest.writeString(id);
		dest.writeString(title);
	}
}