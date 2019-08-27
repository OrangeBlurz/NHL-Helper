package com.example.fokoproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model object describing a player
 */

public class Player implements Serializable {
    @SerializedName("jerseyNumber")
    private String mNumber;
    @SerializedName("person")
    private Person mPerson;
    @SerializedName("position")
    private Position mPosition;

    public String getNumber() { return mNumber; }
    public Person getPerson() { return mPerson; }
    public Position getPosition() { return mPosition; }
}
