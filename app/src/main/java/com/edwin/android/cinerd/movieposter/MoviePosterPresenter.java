package com.edwin.android.cinerd.movieposter;

import android.os.AsyncTask;

import com.edwin.android.cinerd.data.MovieDataRepository;
import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 7/12/2017.
 */

public class MoviePosterPresenter implements MoviePosterMVP.Presenter {

    MoviePosterMVP.View mView;
    private final MovieDataRepository mMovieDataRepository;

    @Inject
    public MoviePosterPresenter(MoviePosterMVP.View view, MovieDataRepository mMovieDataRepository) {
        mView = view;
        this.mMovieDataRepository = mMovieDataRepository;
    }

    @Inject
    void setupListener() {
        mView.setPresenter(this);
    }

    @Override
    public void getMovies() {
        new AsyncTask<Void, Void, List<Movie>>(){
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                return mMovieDataRepository.getMovies();
            }
            @Override
            protected void onPostExecute(List<Movie> movies) {
                mView.onReceiveMovies(movies);
            }
        }.execute();
    }

    @Override
    public void fabButtonAction() {
        mView.showMovieAndTheaterDialog();
    }
}
