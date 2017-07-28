package com.edwin.android.cinerd.moviefinder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwin.android.cinerd.R;

public class MovieFinderFragment extends Fragment implements MovieFinderMVP.View {

    public static final String TAG = MovieFinderFragment.class.getSimpleName();
    private MovieFinderMVP.Presenter mPresenter;

    public MovieFinderFragment() {

    }

    public static MovieFinderFragment newInstance() {
        MovieFinderFragment fragment = new MovieFinderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_finder, container, false);
    }

    @Override
    public void setPresenter(MovieFinderMVP.Presenter presenter) {
        Log.d(TAG, "Setting fragment");
        mPresenter = presenter;
    }
}
