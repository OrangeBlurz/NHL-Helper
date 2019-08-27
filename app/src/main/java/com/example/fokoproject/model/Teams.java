package com.example.fokoproject.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Model Object describing a list of teams
 */

public class Teams implements Serializable {
    //The url location of the master list of teams
    public static final String TEAMS_URL = "https://statsapi.web.nhl.com/api/v1/teams";

    @SerializedName("teams")
    private List<Team> mTeams;

    public List<Team> getTeams() { return mTeams; }

    public Team getTeam(String teamName) {
        for (Team team : mTeams) {
            if (team.getName().equalsIgnoreCase(teamName)) {
                return team;
            }
        }
        return null;
    }
}
