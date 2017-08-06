package com.edwin.android.cinerd.movieposter;

import android.os.AsyncTask;

import com.edwin.android.cinerd.data.ProcessMovies;
import com.edwin.android.cinerd.data.repositories.MovieRepository;
import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 7/12/2017.
 */

public class MoviePosterPresenter implements MoviePosterMVP.Presenter {

    private final MovieRepository mMovieRepository;
    MoviePosterMVP.View mView;
    private final ProcessMovies mMovieDataRepository;

    @Inject
    public MoviePosterPresenter(MoviePosterMVP.View view, ProcessMovies mMovieDataRepository,
                                MovieRepository movieRepository) {
        mView = view;
        this.mMovieDataRepository = mMovieDataRepository;
        mMovieRepository = movieRepository;
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
                return mMovieRepository.getMovies();
            }
            @Override
            protected void onPostExecute(List<Movie> movies) {
                mView.onReceiveMovies(movies);
            }
        }.execute();
    }

    @Override
    public void openMovieFilterActivity() {
        mView.onClickMovie();
    }

    @Override
    public void openTheatersActivity() {
        mView.onClickTheater();
    }
}
