package com.cscodetech.marwarimarts.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("ccode")
    private String mCcode;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("name")
    private String mFname;
    @SerializedName("id")
    private String mId;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("password")
    private String mPassword;

    @SerializedName("refercode")
    private String refercode;



    public String getCcode() {
        return mCcode;
    }

    public void setCcode(String ccode) {
        mCcode = ccode;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFname() {
        return mFname;
    }

    public void setFname(String fname) {
        mFname = fname;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }


    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getRefercode() {
        return refercode;
    }

    public void setRefercode(String refercode) {
        this.refercode = refercode;
    }
}


