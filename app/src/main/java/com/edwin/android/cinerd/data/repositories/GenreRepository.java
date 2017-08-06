package com.edwin.android.cinerd.data.repositories;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.edwin.android.cinerd.data.CineRdContract;
import com.edwin.android.cinerd.entity.Genre;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 8/5/2017.
 */

@Singleton
public class GenreRepository {

    public static final String TAG = GenreRepository.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private Context mContext;
    @Inject
    public GenreRepository(Context context) {
        this.mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public List<Genre> getGenresByMovieId(long movieId) {
        Cursor movieGenreCursor = null;
        try {
            List<Genre> genres = new ArrayList<>();
            movieGenreCursor = mContentResolver.query(CineRdContract.MovieGenreEntry.CONTENT_URI, new
                            String[]{CineRdContract.MovieGenreEntry.COLUMN_NAME_GENRE_ID},
                    CineRdContract.MovieGenreEntry.COLUMN_NAME_MOVIE_ID + " = ?", new
                            String[]{String
                            .valueOf(movieId)}, null);
            while (movieGenreCursor.moveToNext()) {
                short genreId = movieGenreCursor.getShort(movieGenreCursor.getColumnIndex
                        (CineRdContract.MovieGenreEntry.COLUMN_NAME_GENRE_ID));
                genres.add(getGenreById(genreId));
            }
            return genres;
        } finally {
            if(movieGenreCursor != null) {
                movieGenreCursor.close();
            }
        }
    }



    @Nullable
    private Genre getGenreById(short genreId) {
        Cursor genreCursor = null;
        try {
            genreCursor = mContentResolver.query(CineRdContract.GenreEntry.CONTENT_URI, null,
                    CineRdContract
                            .GenreEntry._ID + " = ?", new String[]{String.valueOf(genreId)}, null);

            if (genreCursor.moveToNext()) {
                String genreName = genreCursor.getString(genreCursor.getColumnIndex(CineRdContract
                        .GenreEntry.COLUMN_NAME_NAME));
                return new Genre(genreId, genreName);
            } else {
                return null;
            }
        } finally {
            if(genreCursor != null) {
                genreCursor.close();
            }
        }
    }
}
