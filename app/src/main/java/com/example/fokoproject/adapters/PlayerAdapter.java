package com.example.fokoproject.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.fokoproject.R;
import com.example.fokoproject.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the recycler view to display a list of players
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> implements Filterable {

    private List<Player> mPlayerList;
    //Full copy of the player list for filtering purposes
    private List<Player> mPlayerListFull;
    private OnItemClickListener mListener;

    //Define a click listener interface
    public interface OnItemClickListener {
        void OnItemClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView mPlayerName;
        private TextView mPlayerNumber;
        private TextView mPlayerPosition;

        private PlayerViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            mPlayerName = itemView.findViewById(R.id.list_item_player_name);
            mPlayerNumber = itemView.findViewById(R.id.list_item_player_number);
            mPlayerPosition = itemView.findViewById(R.id.list_item_player_position);

            //Attach click listener to the view holder
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClicked(position);
                        }
                    }
                }
            });
        }
    }

    /**
     * Constructor for the adapter
     * @param playerList full list of players to be displayed
     */
    public PlayerAdapter(List<Player> playerList) {
        mPlayerList = playerList;
        mPlayerListFull = new ArrayList<>(playerList);
    }

    /**
     * Set list of players after initialization.
     * Used for sorting the list
     * @param playerList list of players to be displayed
     */
    public void setPlayers (List<Player> playerList) {
        mPlayerList = playerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_list_item, viewGroup, false);
        PlayerViewHolder viewHolder = new PlayerViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlayerViewHolder playerViewHolder, final int position) {
        Player currentPlayer = mPlayerList.get(position);

        playerViewHolder.mPlayerNumber.setText(currentPlayer.getNumber());
        playerViewHolder.mPlayerPosition.setText(currentPlayer.getPosition().getPositionName());
        playerViewHolder.mPlayerName.setText(currentPlayer.getPerson().getName());

        //Alternate the background colour of the rows for easier reading
        if (position % 2 == 0) {
            playerViewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            playerViewHolder.itemView.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }

    //Filter methods
    @Override
    public Filter getFilter() {
        return mPlayerFilter;
    }

    private Filter mPlayerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(final CharSequence charSequence) {
            List<Player> filteredList = new ArrayList<>();

            //Filter players based on position
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(mPlayerListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Player player : mPlayerListFull) {
                    if (player.getPosition().getPositionName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(player);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(final CharSequence charSequence, final FilterResults filterResults) {
            //Alter displayed list of players to only show players matching the filter
            mPlayerList.clear();
            mPlayerList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
