package com.edwin.android.cinerd.moviedetail;

import android.os.AsyncTask;
import android.util.Log;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.MovieDataRepository;
import com.edwin.android.cinerd.entity.Genre;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Rating;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    public void showMovieDetail(long movieId) {
        Log.d(TAG, "Start executing showMovieDetail method");

        com.edwin.android.cinerd.entity.Movie movie = mMovieDataRepository.getMovieById(movieId);
        mView.setImage(movie);

        mView.setMovieName(movie.getName());
        List<Genre> genres = mMovieDataRepository.getGenresByMovieId(movieId);
        Set<String> genresName = new HashSet<>();

        for(Genre genre: genres) {
            genresName.add(genre.getName().substring(0, 1).toUpperCase() + genre.getName().substring(1));
        }
        mView.setMovieGenreDuration(new ArrayList<>(genresName), movie.getDuration());

        //TODO: Pass string date to string.xml
        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        mView.setMovieReleaseDate(df.format(movie.getReleaseDate()));
        Rating rating = mMovieDataRepository.getRatingByMovieId(movieId);
        mView.setRating(rating.getImdb(), rating.getRottenTomatoes());
    }

    @Override
    public void getMoviesByDayMovieNameTheaterName(int day, String movieName, String theaterName) {

    }

    @Override
    public void showTrailer(final long movieId) {
        new AsyncTask<Void, Void, Movie>() {

            @Override
            protected Movie doInBackground(Void... voids) {
                return mMovieDataRepository.getMovieById(movieId);
            }

            @Override
            protected void onPostExecute(Movie movie) {
                mView.openTrailer(movie.getTrailerUrl());
            }
        }.execute();

    }
}
