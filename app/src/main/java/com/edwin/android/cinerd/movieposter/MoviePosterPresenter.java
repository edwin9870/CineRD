package com.edwin.android.cinerd.movieposter;

import android.os.AsyncTask;

import com.edwin.android.cinerd.data.MovieCollectorJSON;
import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/12/2017.
 */

public class MoviePosterPresenter implements MoviePosterMVP.Presenter {

    MoviePosterMVP.View mView;
    private final MovieCollectorJSON mMovieCollector;

    public MoviePosterPresenter(MoviePosterMVP.View view, MovieCollectorJSON movieCollector) {
        mView = view;
        mMovieCollector = movieCollector;
    }

    @Override
    public void getMovies() {
        new AsyncTask<Void, Void, List<Movie>>(){
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                return mMovieCollector.getMovies();
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                mView.onReceiveMovies(movies);
            }
        }.execute();
    }
}
