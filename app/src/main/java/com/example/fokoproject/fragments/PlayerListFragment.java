package com.example.fokoproject.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fokoproject.MainActivity;
import com.example.fokoproject.R;
import com.example.fokoproject.RequestQueue;
import com.example.fokoproject.adapters.PlayerAdapter;
import com.example.fokoproject.model.Player;
import com.example.fokoproject.model.Roster;
import com.example.fokoproject.model.Team;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Fragment responsible for looking up and showing a list of players for a team.
 * Also sorting by jersey number and name, and filtering by position
 */

public class PlayerListFragment extends Fragment {
    //Key for the extra to be passed to the fragment
    public static final String EXTRA_TEAM_TO_DISPLAY = "teamToDisplay";

    /**
     * Static function to allow for the creation of itself
     * @param teamToDisplay team to be displayed in the list
     * @return A new fragment ready to be placed
     */
    public static PlayerListFragment newFragment (Team teamToDisplay) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_TEAM_TO_DISPLAY, teamToDisplay);
        PlayerListFragment fragment = new PlayerListFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private Team mTeam;
    private RecyclerView mRecyclerView;
    private PlayerAdapter mPlayerAdapter;
    private List<Player> mPlayerList;

    private LinearLayout mPlayerNameHeader;
    private LinearLayout mPlayerNumberHeader;
    private ImageView mPlayerNameSortIndicator;
    private ImageView mPlayerNumberSortIndicator;
    private enum SortStrategy {NONE, ASCENDING, DESCENDING}
    private SortStrategy mPlayerNameSortStrategy = SortStrategy.NONE;
    private SortStrategy mPlayerNumberSortStrategy = SortStrategy.NONE;

    private EditText mSearchField;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTeam = (Team) getArguments().getSerializable(EXTRA_TEAM_TO_DISPLAY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_list, container, false);

        mSearchField = view.findViewById(R.id.search_field);
        mPlayerNameHeader = view.findViewById(R.id.player_name_header);
        mPlayerNumberHeader = view.findViewById(R.id.player_number_header);
        mPlayerNameSortIndicator = view.findViewById(R.id.name_sort_indicator);
        mPlayerNumberSortIndicator = view.findViewById(R.id.number_sort_indicator);
        mRecyclerView = view.findViewById(R.id.player_list_view);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        updateSortingIndicators();

        //If a roster hasn't be looked up previously the make a request for it
        if (mTeam.getRoster() == null) {
            fetchRoster();
        } else {
            populateRecyclerView();
        }

        initializeListeners();

        //Set the tool bar title to be the title of the team being displayed
        ((MainActivity)getActivity()).setToolBarTitle(mTeam.getName());

        return view;
    }

    /**
     * Initialize header listeners for sorting and edit text listener for filtering
     */
    private void initializeListeners() {
        mPlayerNumberHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                sortByNumber();
            }
        });

        mPlayerNameHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                sortByName();
            }
        });

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) { }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                mPlayerAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(final Editable editable) { }
        });
    }

    /**
     * Use Volley to fetch the roster for the team
     */
    private void fetchRoster () {
        String url = mTeam.getRosterUrl();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Gson gson = new Gson();
                        //Deserialize
                        mTeam.setRoster(gson.fromJson(response.toString(), Roster.class));
                        //Populate the recycler view
                        populateRecyclerView();
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
     * Populate the recycler view, set the adapter and register the click handlers
     */
    private void populateRecyclerView () {
        if (mPlayerList == null) {
            mPlayerList = new ArrayList<>();
        }

        mPlayerList.clear();
        mPlayerList.addAll(mTeam.getRoster().getPlayers());

        mPlayerAdapter = new PlayerAdapter(mPlayerList);

        mRecyclerView.setAdapter(mPlayerAdapter);
        mPlayerAdapter.setOnItemClickListener(new PlayerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClicked(final int position) {
                Player playerClicked = mPlayerList.get(position);
                ((MainActivity)getActivity()).placePlayerFragment(playerClicked);
            }
        });
    }

    /**
     * Update the icons for sorting based on the current sorting strategy
     */
    private void updateSortingIndicators() {
        if (mPlayerNameSortStrategy == SortStrategy.NONE) {
            mPlayerNameSortIndicator.setVisibility(View.INVISIBLE);
        }
        if (mPlayerNumberSortStrategy == SortStrategy.NONE) {
            mPlayerNumberSortIndicator.setVisibility(View.INVISIBLE);
        }

        if (mPlayerNameSortStrategy == SortStrategy.ASCENDING) {
            mPlayerNameSortIndicator.setImageResource(R.drawable.ic_ascending);
            mPlayerNameSortIndicator.setVisibility(View.VISIBLE);
        }
        if (mPlayerNameSortStrategy == SortStrategy.DESCENDING) {
            mPlayerNameSortIndicator.setImageResource(R.drawable.ic_descending);
            mPlayerNameSortIndicator.setVisibility(View.VISIBLE);
        }

        if (mPlayerNumberSortStrategy == SortStrategy.ASCENDING) {
            mPlayerNumberSortIndicator.setImageResource(R.drawable.ic_ascending);
            mPlayerNumberSortIndicator.setVisibility(View.VISIBLE);
        }
        if (mPlayerNumberSortStrategy == SortStrategy.DESCENDING) {
            mPlayerNumberSortIndicator.setImageResource(R.drawable.ic_descending);
            mPlayerNumberSortIndicator.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sort list of players by name
     */
    public void sortByName() {
        mPlayerNumberSortStrategy = SortStrategy.NONE;
        if (mPlayerNameSortStrategy == SortStrategy.NONE) {
            mPlayerNameSortStrategy = SortStrategy.ASCENDING;
        } else if (mPlayerNameSortStrategy == SortStrategy.ASCENDING) {
            mPlayerNameSortStrategy = SortStrategy.DESCENDING;
        } else if (mPlayerNameSortStrategy == SortStrategy.DESCENDING) {
            mPlayerNameSortStrategy = SortStrategy.ASCENDING;
        }
        updateSortingIndicators();

        //Comparator responsible for sorting
        Collections.sort(mPlayerList, new Comparator<Player>() {
            @Override
            public int compare(final Player player1, final Player player2) {
                if (mPlayerNameSortStrategy == SortStrategy.ASCENDING) {
                    return player1.getPerson().getName().compareTo(player2.getPerson().getName());
                } else {
                    return player2.getPerson().getName().compareTo(player1.getPerson().getName());
                }

            }
        });

        //Update the recycler view
        mPlayerAdapter.setPlayers(mPlayerList);
    }

    /**
     * Sort list of players by number
     */
    public void sortByNumber() {
        mPlayerNameSortStrategy = SortStrategy.NONE;
        if (mPlayerNumberSortStrategy == SortStrategy.NONE) {
            mPlayerNumberSortStrategy = SortStrategy.ASCENDING;
        } else if (mPlayerNumberSortStrategy == SortStrategy.ASCENDING) {
            mPlayerNumberSortStrategy = SortStrategy.DESCENDING;
        } else if (mPlayerNumberSortStrategy == SortStrategy.DESCENDING) {
            mPlayerNumberSortStrategy = SortStrategy.ASCENDING;
        }
        updateSortingIndicators();

        //Comparator responsible for sorting the players by number
        Collections.sort(mPlayerList, new Comparator<Player>() {
            @Override
            public int compare(final Player player1, final Player player2) {
                if (mPlayerNumberSortStrategy == SortStrategy.ASCENDING) {
                    //Some players to not have a number
                    //Players with no number are considered to be the smallest number
                    if (player1.getNumber() == null) { return -1; }
                    if (player2.getNumber() == null) { return 1; }
                    return Integer.valueOf(player1.getNumber()) - Integer.valueOf(player2.getNumber());
                } else {
                    if (player1.getNumber() == null) { return 1; }
                    if (player2.getNumber() == null) { return -1; }
                    return Integer.valueOf(player2.getNumber()) - Integer.valueOf(player1.getNumber());
                }

            }
        });

        //Update the recycler view
        mPlayerAdapter.setPlayers(mPlayerList);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayerNameSortStrategy = SortStrategy.NONE;
        mPlayerNumberSortStrategy = SortStrategy.NONE;
        updateSortingIndicators();
    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
