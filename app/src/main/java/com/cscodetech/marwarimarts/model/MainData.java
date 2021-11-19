package com.cscodetech.marwarimarts.model;

import com.google.gson.annotations.SerializedName;

public class MainData{

	@SerializedName("r_key")
	private String rKey;

	@SerializedName("d_title")
	private String dTitle;

	@SerializedName("one_key")
	private String oneKey;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("r_hash")
	private String rHash;

	@SerializedName("about")
	private String about;

	@SerializedName("one_hash")
	private String oneHash;

	@SerializedName("terms")
	private String terms;

	@SerializedName("p_limit")
	private String pLimit;

	@SerializedName("contact")
	private String contact;

	@SerializedName("pdbanner")
	private String pdbanner;

	@SerializedName("logo")
	private String logo;

	@SerializedName("currency")
	private String currency;

	@SerializedName("id")
	private String id;

	@SerializedName("policy")
	private String policy;

	public String getRKey(){
		return rKey;
	}

	public String getDTitle(){
		return dTitle;
	}

	public String getOneKey(){
		return oneKey;
	}

	public String getTimezone(){
		return timezone;
	}

	public String getRHash(){
		return rHash;
	}

	public String getAbout(){
		return about;
	}

	public String getOneHash(){
		return oneHash;
	}

	public String getTerms(){
		return terms;
	}

	public String getPLimit(){
		return pLimit;
	}

	public String getContact(){
		return contact;
	}

	public String getPdbanner(){
		return pdbanner;
	}

	public String getLogo(){
		return logo;
	}

	public String getCurrency(){
		return currency;
	}

	public String getId(){
		return id;
	}

	public String getPolicy(){
		return policy;
	}
}