package com.example.fokoproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model object describing a position a player can play
 */

public class Position implements Serializable {
    @SerializedName("name")
    private String mPositionName;

    public String getPositionName() { return mPositionName; }
}
