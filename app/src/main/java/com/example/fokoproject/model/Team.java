package com.example.fokoproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model Object describing a team of players
 */

public class Team implements Serializable {
    @SerializedName("name")
    private String mName;
    @SerializedName("roster")
    private Roster mRoster;
    @SerializedName("id")
    private int mId;
    @SerializedName("link")
    private String mLink;


    public Team (String name) {
        mName = name;
    }

    public String getName() { return mName; }
    public Roster getRoster() { return mRoster; }
    public void setRoster(Roster roster) { mRoster = roster; }

    //The url location where this team's roster can be found
    public String getRosterUrl() { return "https://statsapi.web.nhl.com" + mLink + "/roster"; }
}
