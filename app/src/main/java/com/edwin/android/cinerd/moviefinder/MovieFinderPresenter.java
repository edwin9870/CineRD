package com.edwin.android.cinerd.moviefinder;

import android.os.AsyncTask;

import com.edwin.android.cinerd.data.MovieDataRepository;
import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

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

    @Override
    public void movieNameFilterClicked() {
        new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                List<Movie> movies = MovieFinderPresenter.this.mRepository.getMovies();
                return movies;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                mView.showMovieFilterDialog(movies);
            }
        }.execute();
    }
}
