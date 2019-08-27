package com.example.fokoproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Model object describing a roster of players on a team
 */

public class Roster implements Serializable {
    @SerializedName("roster")
    private List<Player> mPlayers;

    public List<Player> getPlayers() { return mPlayers; }
    public Player getPlayer(int index) { return mPlayers.get(index); }

}
