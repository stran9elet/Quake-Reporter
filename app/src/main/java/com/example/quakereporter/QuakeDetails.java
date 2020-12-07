package com.example.quakereporter;

public class QuakeDetails {

    private String mMagnitude;
    private String mPlace_1;
    private String mPlace_2;
    private String mDate;
    private int mMagColorResourceId;
    private String mUrl;

    public String getDate() {
        return mDate;
    }

    public String getMagnitude() {
        return mMagnitude;
    }

    public String getPlace_1() {
        return mPlace_1;
    }

    public String getPlace_2() {
        return mPlace_2;
    }

    public int getMagColorResourceId(){
        return mMagColorResourceId;
    }

    public String grtUrl(){
        return mUrl;
    }

    public QuakeDetails(String magnitude, String place_1, String place_2, String date, int magColorResourceId,String url){
        mPlace_1 = place_1;
        mPlace_2 = place_2;
        mMagnitude = magnitude;
        mDate = date;
        mMagColorResourceId = magColorResourceId;
        mUrl = url;
    }

}

