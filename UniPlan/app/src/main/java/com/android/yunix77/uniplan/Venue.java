package com.android.yunix77.uniplan;

import com.google.gson.annotations.SerializedName;

public class Venue {
    @SerializedName("streetAddr")
    String streetAddr;
    @SerializedName("Area")
    String area;
    @SerializedName("City")
    String city;
    @SerializedName("State")
    String state;
    @SerializedName("Pincode")
    int pincode;

    public String getStreetAddr() {
        return streetAddr;
    }

    public void setStreetAddr(String streetAddr) {
        this.streetAddr = streetAddr;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
}
