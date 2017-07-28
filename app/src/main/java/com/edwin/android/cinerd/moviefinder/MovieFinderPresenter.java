package com.edwin.android.cinerd.moviefinder;

import com.edwin.android.cinerd.data.MovieDataRepository;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 7/27/2017.
 */

public class MovieFinderPresenter implements MovieFinderMVP.Presenter {


    private final MovieFinderMVP.View mView;
    private final MovieDataRepository mRepository;

    @Inject
    public MovieFinderPresenter(MovieFinderMVP.View view, MovieDataRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Inject
    public void setupListener() {
        mView.setPresenter(this);
    }
}
