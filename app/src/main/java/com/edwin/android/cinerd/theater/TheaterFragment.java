package com.edwin.android.cinerd.theater;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.repositories.TheaterRepository;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Theater;
import com.edwin.android.cinerd.moviedetail.MovieDetailActivity;
import com.edwin.android.cinerd.movieposter.MoviePosterAdapter;
import com.edwin.android.cinerd.util.FirebaseUtil;
import com.edwin.android.cinerd.util.SpacesItemDecoration;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class TheaterFragment extends Fragment implements TheaterMVP.View, MoviePosterAdapter.MoviePosterListener {


    public static final String TAG = TheaterFragment.class.getSimpleName();
    public static final String BUNDLE_THEATER_ID = "BUNDLE_THEATER_ID";
    public static final int LOADER_THEATER_LIST_ID = 54515;
    @BindView(R.id.recycler_view_theater_movie_poster)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private TheaterMVP.Presenter mPresenter;
    private MoviePosterAdapter mAdapter;
    private int mTheaterId;
    private FirebaseAnalytics mFirebaseAnalytics;


    public TheaterFragment() {
    }

    public static TheaterFragment newInstance() {
        return new TheaterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theater, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(savedInstanceState == null) {
            mPresenter.showTheatersDialog();
            mPresenter.setActivityTitle(getResources().getString(R.string.theater_activity_title));
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundleFcm = new Bundle();
        bundleFcm.putString(FirebaseUtil.PARAM.ACTIVITY_NAME, TheaterActivity.class.getSimpleName());
        mFirebaseAnalytics.logEvent(FirebaseUtil.EVENT.OPEN_ACTIVITY, bundleFcm);

            mAdapter = new MoviePosterAdapter(getActivity(), this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.theater_columns));

            mRecyclerView.setLayoutManager(gridLayoutManager);
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen
                    .space_between_movie_poster);
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getInteger(R
                    .integer.movie_poster_space_item), spacingInPixels, false));
            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mTheaterId = savedInstanceState.getInt(BUNDLE_THEATER_ID);
            if(mTheaterId > 0) {
                mPresenter.setActivityTitle(mTheaterId);
                mPresenter.getMovies(mTheaterId);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void setPresenter(TheaterMVP.Presenter presenter) {
        Log.d(TAG, "Setting mPresenter");
        mPresenter = presenter;
    }

    @Override
    public void onReceiveMovies(List<Movie> movies) {
        mAdapter.setMovies(movies);
    }

    @Override
    public void setActivityTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setTheaterId(int theaterId) {
        mTheaterId = theaterId;
    }

    @Override
    public void showTheatersDialog(TheaterRepository theaterRepository, Date minDate) {
        LoaderManager loaderManager = getLoaderManager();
        TheaterListLoaderCallback theaterListLoaderCallback = new TheaterListLoaderCallback
                (getActivity(), this, mPresenter, theaterRepository, minDate);
        loaderManager.initLoader(LOADER_THEATER_LIST_ID, null, theaterListLoaderCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mTheaterId > 0) {
            outState.putInt(BUNDLE_THEATER_ID, mTheaterId);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClickMovie(Movie movie) {
        Log.d(TAG, "Movie clicked: " + movie);
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.BUNDLE_MOVIE_ID, movie.getMovieId());
        getActivity().startActivity(intent);
    }
}
