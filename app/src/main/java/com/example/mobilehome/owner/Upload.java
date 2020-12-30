package com.example.mobilehome.owner;

import com.google.firebase.database.Exclude;

public class Upload {

    private String mImgeUrl;
    private String mRoom;
    private String mGender;
    private String mTaka;
    private String mPlace;
    private String mHomeAddress;

    private String mKey;
    private String mUserId;


    public Upload() {

    }

    public Upload(String mImgeUrl, String mRoom, String mGender, String mTaka,String mPlace,String mHomeAddress,String mUserId,String mKey) {
        this.mImgeUrl = mImgeUrl;
        this.mRoom = mRoom;
        this.mGender = mGender;
        this.mTaka = mTaka;
        this.mPlace=mPlace;
        this.mHomeAddress=mHomeAddress;
        this.mUserId = mUserId;
        this.mKey=mKey;
    }

    public String getmImgeUrl() {
        return mImgeUrl;
    }

    public void setmImgeUrl(String mImgeUrl) {
        this.mImgeUrl = mImgeUrl;
    }

    public String getmRoom() {
        return mRoom;
    }

    public void setmRoom(String mRoom) {
        this.mRoom = mRoom;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmTaka() {
        return mTaka;
    }

    public void setmTaka(String mTaka) {
        this.mTaka = mTaka;
    }

    public String getmPlace() {
        return mPlace;
    }

    public void setmPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public String getmHomeAddress() {
        return mHomeAddress;
    }

    public void setmHomeAddress(String mHomeAddress) {
        this.mHomeAddress = mHomeAddress;
    }

    public String getmKey() {
        return mKey;
    }


    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }
}
