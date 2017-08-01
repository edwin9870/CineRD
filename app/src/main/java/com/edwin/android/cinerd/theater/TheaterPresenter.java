package com.edwin.android.cinerd.theater;

import android.os.AsyncTask;
import android.util.Log;

import com.edwin.android.cinerd.data.MovieDataRepository;
import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

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

    @Override
    public void getMovies(final int theaterId) {
        new AsyncTask<Void, Void, List<Movie>>(){
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                return mRepository.getMoviesByTheaterId(theaterId);
            }
            @Override
            protected void onPostExecute(List<Movie> movies) {
                mView.onReceiveMovies(movies);
            }
        }.execute();
    }

    @Override
    public void setActivityTitle(final int mTheaterId) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return mRepository.getTheaterNameById(mTheaterId);
            }

            @Override
            protected void onPostExecute(String title) {
                mView.setActivityTitle(title);
            }
        }.execute();

    }
}
