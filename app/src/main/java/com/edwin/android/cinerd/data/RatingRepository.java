package com.edwin.android.cinerd.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 8/5/2017.
 */

@Singleton
public class RatingRepository {

    public static final String TAG = TheaterRepository.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private Context mContext;

    public static final String ROTTEN_TOMATOES = "RottenTomatoes";
    public static final String IMDB = "IMDB";

    @Inject
    public RatingRepository(Context context) {
        this.mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public com.edwin.android.cinerd.entity.Rating getRatingByMovieId(long movieId) {
        com.edwin.android.cinerd.entity.Rating rating = new com.edwin.android.cinerd.entity.Rating();

        Cursor movieRatingCursor = mContentResolver.query(CineRdContract.MovieRatingEntry.CONTENT_URI, null,
                CineRdContract
                        .MovieRatingEntry.COLUMN_NAME_MOVIE_ID + " = ?", new String[]{String.valueOf
                        (movieId)}, null);
        rating.setMovieId(movieId);

        while(movieRatingCursor.moveToNext()) {
            String ratingValue = movieRatingCursor.getString(movieRatingCursor.getColumnIndex
                    (CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING));
            short ratingId = movieRatingCursor.getShort(movieRatingCursor.getColumnIndex
                    (CineRdContract
                            .MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER));
            String ratingName = getRatingNameByRatingId(ratingId);
            if (ratingName.toUpperCase().equals(IMDB)) {
                rating.setImdb(ratingValue);
            } else {
                rating.setRottenTomatoes(ratingValue);
            }
        }
        return rating;
    }


    @Nullable
    private String getRatingNameByRatingId(short ratingId) {
        Cursor ratingCursor = null;
        try {
            ratingCursor = mContentResolver.query(CineRdContract.RatingEntry.CONTENT_URI, null,
                    CineRdContract
                            .RatingEntry._ID + " = ?", new String[]{String.valueOf(ratingId)}, null);

            if(ratingCursor.moveToNext()) {
                String ratingName = ratingCursor.getString(ratingCursor.getColumnIndex(CineRdContract
                        .RatingEntry.COLUMN_NAME_NAME));
                return ratingName;
            } else {
                return null;
            }
        } finally {
            if(ratingCursor != null) {
                ratingCursor.close();
            }
        }
    }

}
