package com.edwin.android.cinerd.theater;

import android.util.Log;

import com.edwin.android.cinerd.data.MovieDataRepository;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 7/30/2017.
 */

public class TheaterPresenter implements TheaterMVP.Presenter {

    public static final String TAG = TheaterPresenter.class.getSimpleName();
    private final MovieDataRepository mRepository;
    private TheaterMVP.View mView;

    @Inject
    public TheaterPresenter(MovieDataRepository repository, TheaterMVP.View view) {
        mRepository = repository;
        mView = view;
    }

    @Inject
    public void setupListener() {
        Log.d(TAG, "Starting listener");
        mView.setPresenter(this);
    }
}
