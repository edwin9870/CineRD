package com.edwin.android.cinerd.theater;

import android.os.AsyncTask;
import android.util.Log;

import com.edwin.android.cinerd.data.MovieDataRepository;
import com.edwin.android.cinerd.data.TheaterRepository;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Theater;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 7/30/2017.
 */

public class TheaterPresenter implements TheaterMVP.Presenter {

    public static final String TAG = TheaterPresenter.class.getSimpleName();
    private final MovieDataRepository mRepository;
    private final TheaterRepository mTheaterRepository;
    private TheaterMVP.View mView;

    @Inject
    public TheaterPresenter(MovieDataRepository repository, TheaterRepository theaterRepository,
                            TheaterMVP.View view) {
        mRepository = repository;
        mView = view;
        mTheaterRepository = theaterRepository;
    }

    @Inject
    public void setupListener() {
        Log.d(TAG, "Starting listener");
        mView.setPresenter(this);
    }

    @Override
    public void getMovies(final int theaterId) {
        new AsyncTask<Void, Void, List<Movie>>() {
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
                return mTheaterRepository.getTheaterById(mTheaterId).getTitle();
            }

            @Override
            protected void onPostExecute(String title) {
                mView.setActivityTitle(title);
            }
        }.execute();

    }

    @Override
    public void setActivityTitle(String title) {
        mView.setActivityTitle(title);
    }

    @Override
    public void showTheatersDialog() {
        new AsyncTask<Void, Void, List<Theater>>() {

            @Override
            protected List<Theater> doInBackground(Void... voids) {
                return mTheaterRepository.getAllTheatersByMinDate(new Date());
            }

            @Override
            protected void onPostExecute(List<Theater> theaters) {
                super.onPostExecute(theaters);
                mView.showTheatersDialog(theaters);
            }
        }.execute();
    }

    @Override
    public void onClickFabButton() {
        showTheatersDialog();
    }
}
