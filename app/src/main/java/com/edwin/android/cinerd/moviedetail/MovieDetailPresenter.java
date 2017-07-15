package com.edwin.android.cinerd.moviedetail;

import android.util.Log;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.MovieDataRepository;
import com.edwin.android.cinerd.entity.Movie;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 7/14/2017.
 */

public class MovieDetailPresenter implements MovieDetailMVP.Presenter {

    public static final String TAG = MovieDetailPresenter.class.getSimpleName();
    private final MovieDetailMVP.View mView;
    private final MovieDataRepository mMovieDataRepository;

    @Inject
    public MovieDetailPresenter(MovieDetailMVP.View mView, MovieDataRepository
            mMovieDataRepository) {
        this.mView = mView;
        this.mMovieDataRepository = mMovieDataRepository;
    }

    @Inject
    public void setupListener() {
        mView.setPresenter(this);
    }

    @Override
    public void showMovieDetail(Movie movie) {
        Log.d(TAG, "Start executing showMovieDetail method");
        mView.setBackdropImage(R.drawable.maxmaxbackdrop);
        mView.setMovieName(movie.getName());
        mView.setMovieGenreDuration(movie.getGenre(), movie.getDuration());

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        mView.setMovieReleaseDate(df.format(movie.getReleaseDate()));
        mView.setRating(movie.getRating().getImdb(), movie.getRating().getRottentomatoes());
    }
}
