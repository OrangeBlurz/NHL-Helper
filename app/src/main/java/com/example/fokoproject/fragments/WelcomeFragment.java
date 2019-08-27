package com.example.fokoproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fokoproject.MainActivity;
import com.example.fokoproject.R;

/**
 * Welcom fragment responsible for showing a welcome message and prompt the start browsing the teams
 */

public class WelcomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        ((MainActivity)getActivity()).setToolBarTitle(getContext().getString(R.string.welcome_fragment_title));
        return view;
    }
}
