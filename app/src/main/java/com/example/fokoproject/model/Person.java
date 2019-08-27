package com.example.fokoproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model object describing a person
 */

public class Person implements Serializable {
    @SerializedName("fullName")
    private String mName;
    @SerializedName("link")
    private String mLink;
    @SerializedName("id")
    private int mId;

    private String mNationality;

    public String getName() { return mName; }
    public String getNationality() { return mNationality; }
    public void setNationality(String nationality) { mNationality = nationality; }
    public String getPersonDetailUrl() { return "https://statsapi.web.nhl.com" + mLink; }
}
