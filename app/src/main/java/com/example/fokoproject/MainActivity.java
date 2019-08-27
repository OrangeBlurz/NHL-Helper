package com.example.fokoproject;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fokoproject.fragments.PlayerListFragment;
import com.example.fokoproject.fragments.PlayerNationalityFragment;
import com.example.fokoproject.fragments.WelcomeFragment;
import com.example.fokoproject.model.Player;
import com.example.fokoproject.model.Team;
import com.example.fokoproject.model.Teams;
import com.example.fokoproject.utils.Utils;
import com.google.gson.Gson;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Teams mTeams;
    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolBarElements();

        //Present the default Welcome Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WelcomeFragment()).commit();
        }

        fetchTeams();
    }

    /**
     * Initialize the elements of the toolbar and the navigation view and drawer
     */
    private void initializeToolBarElements() {
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        //Remove all tint from Nav Drawer icons
        mNavigationView.setItemIconTintList(null);

        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        //Identify the item in the nav drawer that was clicked and place appropriate fragment
        Team teamClicked = mTeams.getTeam(String.valueOf(menuItem.getTitle()));
        if (teamClicked != null) {
            placeTeamListFragment(teamClicked);
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Fetch all teams and populate the nav drawer
     */
    private void fetchTeams() {
        String url = Teams.TEAMS_URL;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Gson gson = new Gson();
                        //Deserialize
                        mTeams = gson.fromJson(response.toString(), Teams.class);
                        for (Team team : mTeams.getTeams()) {
                            //Add each team to the nav drawer with the appropriate icon
                            mNavigationView.getMenu().add(team.getName()).setIcon(Utils.getDrawableForTeam(team));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        error.printStackTrace();
                    }
                });
        //Singleton instance of Volley as per the documentation
        RequestQueue.getInstance(this).addToRequestQueue(request);
    }

    /**
     * Place a team list fragment of a particular team
     * @param team team to be presented in the fragment
     */
    public void placeTeamListFragment(Team team) {
        mToolBar.setTitle(team.getName());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        PlayerListFragment.newFragment(team))
                //Allow users to backup through the fragments
                .addToBackStack(team.getName())
                .commit();
    }

    /**
     * Place a player nationality fragment for a particular player
     * @param player the player to be presented
     */
    public void placePlayerFragment(Player player) {
        mToolBar.setTitle(player.getPerson().getName());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        PlayerNationalityFragment.newFragment(player))
                //Allow users to backup through the fragments
                .addToBackStack(player.getPerson().getName())
                .commit();

    }

    /**
     * Utility method to allow child fragments to change the name of the toolbar
     * @param title
     */
    public void setToolBarTitle(String title) {
        mToolBar.setTitle(title);
    }
}
