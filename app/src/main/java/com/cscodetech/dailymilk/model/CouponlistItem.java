package com.cscodetech.dailymilk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CouponlistItem implements Parcelable {

	@SerializedName("coupn_code")
	private String coupnCode;

	@SerializedName("coupon_img")
	private String couponImg;

	@SerializedName("id")
	private String id;

	@SerializedName("coupon_expire_date")
	private String couponExpireDate;

	protected CouponlistItem(Parcel in) {
		coupnCode = in.readString();
		couponImg = in.readString();
		id = in.readString();
		couponExpireDate = in.readString();
	}

	public static final Creator<CouponlistItem> CREATOR = new Creator<CouponlistItem>() {
		@Override
		public CouponlistItem createFromParcel(Parcel in) {
			return new CouponlistItem(in);
		}

		@Override
		public CouponlistItem[] newArray(int size) {
			return new CouponlistItem[size];
		}
	};

	public String getCoupnCode(){
		return coupnCode;
	}

	public String getCouponImg(){
		return couponImg;
	}

	public String getId(){
		return id;
	}

	public String getCouponExpireDate(){
		return couponExpireDate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(coupnCode);
		dest.writeString(couponImg);
		dest.writeString(id);
		dest.writeString(couponExpireDate);
	}
}