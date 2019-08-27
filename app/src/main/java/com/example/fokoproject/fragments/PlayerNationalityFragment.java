package com.example.fokoproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fokoproject.MainActivity;
import com.example.fokoproject.R;
import com.example.fokoproject.RequestQueue;
import com.example.fokoproject.model.Player;
import com.example.fokoproject.utils.Utils;
import com.haipq.android.flagkit.FlagImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment responsible for looking up and showing a players nationality.
 */

public class PlayerNationalityFragment extends Fragment {
    //Key for the extra to be passed to the fragment
    public static final String EXTRA_PLAYER_TO_DISPLAY = "playerToDisplay";

    /**
     * Static function to allow for the creation of itself
     * @param playerToDisplay team to be displayed in the list
     * @return A new fragment ready to be placed
     */
    public static PlayerNationalityFragment newFragment (Player playerToDisplay) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PLAYER_TO_DISPLAY, playerToDisplay);
        PlayerNationalityFragment fragment = new PlayerNationalityFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private Player mPlayer;
    private TextView mCountryNameView;
    private FlagImageView mFlagView;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayer = (Player) getArguments().getSerializable(EXTRA_PLAYER_TO_DISPLAY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_nationality, container, false);

        mCountryNameView = view.findViewById(R.id.nationality);
        mFlagView = view.findViewById(R.id.flagView);

        //If a player's nationality hasn't be looked up previously the make a request for it
        if (mPlayer.getPerson().getNationality() == null) {
            fetchNationality();
        } else {
            populateView();
        }

        //Set the tool bar title to be the name of the player's nationality being displayed
        ((MainActivity)getActivity()).setToolBarTitle(mPlayer.getPerson().getName());

        return view;
    }

    /**
     * Use Volley to fetch the player's nationality
     */
    public void fetchNationality() {
        String url = mPlayer.getPerson().getPersonDetailUrl();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        try {
                            //Look up the single field needed
                            mPlayer.getPerson().setNationality(response.getJSONArray("people").getJSONObject(0).getString("nationality"));
                            populateView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                error.printStackTrace();
            }
        });
        //Singleton instance of Volley as per the documentation
        RequestQueue.getInstance(getContext()).addToRequestQueue(request);
    }

    /**
     * Populate the fragment view with the data on the player's nationality
     */
    public void populateView() {
        String iso2ConturyCode = Utils.iso3CountryCodeToIso2CountryCode(mPlayer.getPerson().getNationality());
        mCountryNameView.setText(Utils.countryNameFromIso(iso2ConturyCode));
        mFlagView.setCountryCode(iso2ConturyCode);

        //Only show the views then the data is set to avoid the user seeing any changing of text/icons
        mFlagView.setVisibility(View.VISIBLE);
        mCountryNameView.setVisibility(View.VISIBLE);
    }
}
