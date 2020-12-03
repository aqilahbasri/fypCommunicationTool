package com.example.fypcommunicationtool;

import android.widget.Spinner;

public class ApplyCertContactInfo {

    private String name;
    private String address, city, postcode, phoneNumber, state;
    private Long appliedTimestamp;

    ApplyCertContactInfo() {    //Required empty constructor

    }

    public ApplyCertContactInfo(String name) {
        this.name = name;
    }

    public ApplyCertContactInfo(String address, String city, String postcode,
                                String phoneNumber, String state) {
//        this.name = name;
        this.address = address;
        this.city = city;
        this.postcode = postcode;
        this.phoneNumber = phoneNumber;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getAppliedTimestamp() {
        return appliedTimestamp;
    }

    public void setAppliedTimestamp(Long appliedTimestamp) {
        this.appliedTimestamp = appliedTimestamp;
    }
}