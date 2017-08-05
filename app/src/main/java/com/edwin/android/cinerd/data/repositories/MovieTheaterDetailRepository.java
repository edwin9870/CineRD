package com.edwin.android.cinerd.data.repositories;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edwin.android.cinerd.data.CineRdContract;
import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;
import com.edwin.android.cinerd.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 8/5/2017.
 */

@Singleton
public class MovieTheaterDetailRepository {


    public static final String TAG = MovieTheaterDetailRepository.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private Context mContext;

    @Inject
    public MovieTheaterDetailRepository(Context context) {
        this.mContext = context;
        mContentResolver = context.getContentResolver();
    }


    public List<MovieTheaterDetail> getMoviesTheaterDetailByMovieIdAvailableDate(long movieId,
                                                                                 Date availableDate,
                                                                                 long theaterId) {
        List<MovieTheaterDetail> movieTheaterDetailList;
        Cursor movieTheaterDetailCursor = null;
        try {
            movieTheaterDetailCursor = mContentResolver.query(CineRdContract
                    .MovieTheaterDetailEntry
                    .CONTENT_URI, null, CineRdContract.MovieTheaterDetailEntry
                    .COLUMN_NAME_MOVIE_ID + " = ? AND " + CineRdContract.MovieTheaterDetailEntry
                    .COLUMN_NAME_THEATER_ID + " = ? AND date(" + CineRdContract
                    .MovieTheaterDetailEntry
                    .COLUMN_NAME_AVAILABLE_DATE + ") = date('" + DateUtil.formatDate
                    (availableDate) + "')", new String[]{String.valueOf(movieId), String.valueOf
                    (theaterId)}, null);

            movieTheaterDetailList = parseMovieTheaterDetails(movieTheaterDetailCursor);
            movieTheaterDetailCursor.close();

            return movieTheaterDetailList;
        } finally {
            if (movieTheaterDetailCursor != null) {
                movieTheaterDetailCursor.close();
            }
        }
    }


    private List<MovieTheaterDetail> parseMovieTheaterDetails(Cursor movieTheaterDetailCursor) {
        List<MovieTheaterDetail> movieTheaterDetailList = new ArrayList<>();
        while (movieTheaterDetailCursor.moveToNext()) {
            Log.d(TAG, "movieTheaterDetailCursor.getCount(): " + movieTheaterDetailCursor
                    .getCount());
            MovieTheaterDetail movieTheaterDetail = parseMovieTheaterDetail
                    (movieTheaterDetailCursor);
            movieTheaterDetailList.add(movieTheaterDetail);
        }

        return movieTheaterDetailList;
    }


    @NonNull
    public MovieTheaterDetail parseMovieTheaterDetail(Cursor movieTheaterDetailCursor) {
        MovieTheaterDetail movieTheaterDetail;
        long movieId = movieTheaterDetailCursor.getLong(movieTheaterDetailCursor
                .getColumnIndexOrThrow(CineRdContract.MovieTheaterDetailEntry
                        .COLUMN_NAME_MOVIE_ID));
        short roomId = movieTheaterDetailCursor.getShort(movieTheaterDetailCursor
                .getColumnIndexOrThrow(CineRdContract.MovieTheaterDetailEntry
                        .COLUMN_NAME_ROOM_ID));
        short subtitleId = movieTheaterDetailCursor.getShort(movieTheaterDetailCursor
                .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                        .COLUMN_NAME_SUBTITLE_ID));
        short formatId = movieTheaterDetailCursor.getShort(movieTheaterDetailCursor
                .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                        .COLUMN_NAME_FORMAT_ID));
        short languageId = movieTheaterDetailCursor.getShort(movieTheaterDetailCursor
                .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                        .COLUMN_NAME_LANGUAGE_ID));
        int theaterId = movieTheaterDetailCursor.getInt(movieTheaterDetailCursor
                .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                        .COLUMN_NAME_THEATER_ID));

        String availableDateString = movieTheaterDetailCursor.getString(movieTheaterDetailCursor
                .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                        .COLUMN_NAME_AVAILABLE_DATE));
        Date availableDate = DateUtil.getDateFromString(availableDateString);

        movieTheaterDetail = new MovieTheaterDetail();
        movieTheaterDetail.setRoomId(roomId);
        movieTheaterDetail.setSubtitleId(subtitleId);
        movieTheaterDetail.setFormatId(formatId);
        movieTheaterDetail.setLanguageId(languageId);
        movieTheaterDetail.setTheaterId(theaterId);
        movieTheaterDetail.setMovieId(movieId);
        movieTheaterDetail.setAvailableDate(availableDate);

        return movieTheaterDetail;
    }

    public List<MovieTheaterDetail> getMoviesTheaterDetailByMovieIdAvailableDate(long movieId, Date availableDate) {
        List<MovieTheaterDetail> movieTheaterDetailList;
        Cursor movieTheaterDetailCursor = null;
        try {
            movieTheaterDetailCursor = mContentResolver.query(CineRdContract
                    .MovieTheaterDetailEntry
                    .CONTENT_URI, null, CineRdContract.MovieTheaterDetailEntry
                    .COLUMN_NAME_MOVIE_ID + " = ? AND date(" + CineRdContract
                    .MovieTheaterDetailEntry
                    .COLUMN_NAME_AVAILABLE_DATE + ") = date('" + DateUtil.formatDate
                    (availableDate) + "')", new String[]{String.valueOf(movieId)}, null);

            movieTheaterDetailList = parseMovieTheaterDetails(movieTheaterDetailCursor);
            movieTheaterDetailCursor.close();

            return movieTheaterDetailList;
        } finally {
            if(movieTheaterDetailCursor != null) {
                movieTheaterDetailCursor.close();
            }
        }
    }

}
