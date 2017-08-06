package com.edwin.android.cinerd.data.repositories;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edwin.android.cinerd.data.CineRdContract;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;
import com.edwin.android.cinerd.util.DateUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 8/5/2017.
 */

@Singleton
public class MovieRepository {

    public static final String TAG = MovieRepository.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private final RatingRepository mRatingRepository;
    private final MovieTheaterDetailRepository mMovieTheaterDetailRepository;
    private Context mContext;

    @Inject
    public MovieRepository(Context context, RatingRepository ratingRepository, MovieTheaterDetailRepository movieTheaterDetailRepository) {
        this.mContext = context;
        mContentResolver = context.getContentResolver();
        mRatingRepository = ratingRepository;
        mMovieTheaterDetailRepository = movieTheaterDetailRepository;
    }


    public Movie getMovieById(long movieId) {
        Movie movie = null;
        Cursor movieCursor = null;
        try {
            movieCursor = mContentResolver.query(CineRdContract.MovieEntry
                    .CONTENT_URI, null, CineRdContract.MovieEntry._ID+" = ?", new String[]{String.valueOf(movieId)}, null);
            if (movieCursor.moveToNext()) {
                movie = parseMovie(movieCursor);
            }
            return movie;
        }finally {
            if(movieCursor != null) {
                movieCursor.close();
            }
        }
    }


    public List<Movie> getMovies() {
        List<Movie> movies = new ArrayList<>();
        Cursor movieCursor = null;
        try {
            movieCursor = mContentResolver.query(CineRdContract.MovieEntry
                    .CONTENT_URI, null, null, null, null);
            while (movieCursor.moveToNext()) {
                Movie movie = parseMovie(movieCursor);
                movies.add(movie);
            }
            return movies;
        }finally {
            if (movieCursor != null) {
                movieCursor.close();
            }
        }
    }

    public List<com.edwin.android.cinerd.entity.Movie> getMoviesByTheaterId(int theaterId) {
        Cursor movieTheaterDetailCursor = null;
        Set<Movie> movies = new HashSet<>();
        try {
            movieTheaterDetailCursor = mContentResolver.query(CineRdContract
                    .MovieTheaterDetailEntry
                    .CONTENT_URI, null, CineRdContract.MovieTheaterDetailEntry
                    .COLUMN_NAME_THEATER_ID + " = ? ", new String[]{String.valueOf(theaterId)}, null);

            while (movieTheaterDetailCursor.moveToNext()) {
                MovieTheaterDetail movieTheaterDetail = mMovieTheaterDetailRepository.parseMovieTheaterDetail(movieTheaterDetailCursor);
                movies.add(getMovieById(movieTheaterDetail.getMovieId()));
            }
            return new ArrayList<>(movies);
        } finally {
            if(movieTheaterDetailCursor != null) {
                movieTheaterDetailCursor.close();
            }
        }
    }

    @NonNull
    private Movie parseMovie(Cursor movieCursor) {
        Movie movie;
        movie = new Movie();
        movie.setMovieId(movieCursor.getLong(movieCursor.getColumnIndex(CineRdContract.MovieEntry._ID)));
        movie.setName(movieCursor.getString(movieCursor.getColumnIndex(CineRdContract.MovieEntry.COLUMN_NAME_NAME)));
        movie.setDuration(movieCursor.getShort(movieCursor.getColumnIndex(CineRdContract.MovieEntry.COLUMN_NAME_DURATION)));

        String releaseDateText = movieCursor.getString(movieCursor.getColumnIndex(CineRdContract
                .MovieEntry.COLUMN_NAME_RELEASE_DATE));
        movie.setReleaseDate(DateUtil.getDateFromString(releaseDateText));
        movie.setSynopsis(movieCursor.getString(movieCursor.getColumnIndex(CineRdContract.MovieEntry.COLUMN_NAME_SYNOPSIS)));
        movie.setBackdropUrl(movieCursor.getString(movieCursor.getColumnIndex(CineRdContract
                .MovieEntry.COLUMN_NAME_BACKDROP_PATH)));
        movie.setPosterUrl(movieCursor.getString(movieCursor.getColumnIndex(CineRdContract
                .MovieEntry.COLUMN_NAME_POSTER_PATH)));
        movie.setTrailerUrl(movieCursor.getString(movieCursor.getColumnIndex(CineRdContract
                .MovieEntry.COLUMN_NAME_TRAILER_URL)));
        movie.setRating(mRatingRepository.getRatingByMovieId(movie.getMovieId()));
        return movie;
    }


    public long getMovieIdByName(String movieName) {
        long movieId = -1;
        Cursor movieCursor = null;
        try {
            movieCursor = mContentResolver.query(CineRdContract.MovieEntry
                    .CONTENT_URI, new String[]{CineRdContract.MovieEntry._ID}, "UPPER(NAME) = ?", new String[]{movieName.toUpperCase()}, null);

            Log.d(TAG, "movieName: " + movieName.toUpperCase());
            if (movieCursor.moveToNext()) {
                movieId = movieCursor.getLong(movieCursor.getColumnIndex(CineRdContract.MovieEntry._ID));
            }

            return movieId;
        }finally {
            if(movieCursor != null) {
                movieCursor.close();
            }
        }
    }
}
