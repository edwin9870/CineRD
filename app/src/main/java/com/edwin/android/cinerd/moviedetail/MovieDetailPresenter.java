package com.edwin.android.cinerd.moviedetail;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.repositories.GenreRepository;
import com.edwin.android.cinerd.data.ProcessMovies;
import com.edwin.android.cinerd.data.repositories.MovieRepository;
import com.edwin.android.cinerd.data.repositories.RatingRepository;
import com.edwin.android.cinerd.entity.Genre;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Rating;
import com.edwin.android.cinerd.util.NetworkUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import static com.edwin.android.cinerd.util.StringUtil.capitalizeFirstWord;

/**
 * Created by Edwin Ramirez Ventura on 7/14/2017.
 */

public class MovieDetailPresenter implements MovieDetailMVP.Presenter {

    public static final String TAG = MovieDetailPresenter.class.getSimpleName();
    private final MovieDetailMVP.View mView;
    private final ProcessMovies mMovieDataRepository;
    private final RatingRepository mRatingRepository;
    private final GenreRepository mGenreRepository;
    private final MovieRepository mMovieRepository;

    @Inject
    public MovieDetailPresenter(MovieDetailMVP.View mView,
                                ProcessMovies mMovieDataRepository,
                                RatingRepository ratingRepository,
                                GenreRepository genreRepository,
                                MovieRepository movieRepository) {
        this.mView = mView;
        this.mMovieDataRepository = mMovieDataRepository;
        this.mRatingRepository = ratingRepository;
        mGenreRepository = genreRepository;
        mMovieRepository = movieRepository;
    }

    @Inject
    public void setupListener() {
        mView.setPresenter(this);
    }

    @Override
    public void showMovieDetail(Context context, long movieId) {
        Log.d(TAG, "Start executing showMovieDetail method");

        com.edwin.android.cinerd.entity.Movie movie = mMovieRepository.getMovieById(movieId);
        Log.d(TAG, "movieId: " + movieId + ", movie found: " + movie);
        if(movie == null) {
            return;
        }
        mView.setImage(movie);

        mView.setMovieName(movie.getName());
        List<Genre> genres = mGenreRepository.getGenresByMovieId(movieId);
        Set<String> genresName = new HashSet<>();

        for(Genre genre: genres) {
            genresName.add(capitalizeFirstWord(genre.getName()));
        }
        mView.setMovieGenreDuration(new ArrayList<>(genresName), movie.getDuration());

        SimpleDateFormat df = new SimpleDateFormat(context.getString(R.string.date_calendar), Locale.US);
        mView.setMovieReleaseDate(df.format(movie.getReleaseDate()));
        Rating rating = mRatingRepository.getRatingByMovieId(movieId);
        mView.setRating(rating.getImdb(), rating.getRottenTomatoes());
    }

    @Override
    public void showTrailer(Context context, final long movieId) {

        boolean isOnline = NetworkUtil.isOnline(context);
        if(!isOnline) {
            mView.showMessage(context.getString(R.string.no_internet_to_play_video));
            return;
        }

        new AsyncTask<Void, Void, Movie>() {

            @Override
            protected Movie doInBackground(Void... voids) {
                return mMovieRepository.getMovieById(movieId);
            }

            @Override
            protected void onPostExecute(Movie movie) {
                mView.openTrailer(movie.getTrailerUrl());
            }
        }.execute();

    }
}
