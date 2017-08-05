package com.edwin.android.cinerd.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.edwin.android.cinerd.entity.Theater;
import com.edwin.android.cinerd.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 8/5/2017.
 */

@Singleton
public class TheaterRepository {

    public static final String TAG = TheaterRepository.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private Context mContext;

    @Inject
    public TheaterRepository(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public List<Theater> getAllTheatersByMinDate(Date minDate) {
        Cursor movieTheaterDetailCursor = null;

        try {
            Set<Theater> theaters = new HashSet<>();
            movieTheaterDetailCursor = mContentResolver.query(CineRdContract
                    .MovieTheaterDetailEntry
                    .CONTENT_URI, null, " date(" + CineRdContract
                    .MovieTheaterDetailEntry
                    .COLUMN_NAME_AVAILABLE_DATE + ") >= date('" + DateUtil.formatDate
                    (minDate) + "')", null, null);

            while (movieTheaterDetailCursor.moveToNext()) {
                int theaterId = movieTheaterDetailCursor.getInt(movieTheaterDetailCursor
                        .getColumnIndex

                        (CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_THEATER_ID));
                theaters.add(getTheaterById(theaterId));
            }
            return new ArrayList<>(theaters);
        } finally {
            if (movieTheaterDetailCursor != null) {
                movieTheaterDetailCursor.close();
            }
        }
    }


    public com.edwin.android.cinerd.entity.Theater getTheaterById(int theaterId) {
        Cursor theaterCursor = null;

        try {
            theaterCursor = mContentResolver.query(CineRdContract.TheaterEntry
                            .CONTENT_URI, null,
                    CineRdContract.TheaterEntry._ID + " = ?", new String[]{String.valueOf
                            (theaterId)},

                    null);

            if (theaterCursor.moveToNext()) {
                String theaterName = theaterCursor.getString(theaterCursor.getColumnIndex
                        (CineRdContract.TheaterEntry.COLUMN_NAME_NAME));
                Log.d(TAG, "theaterName: " + theaterName);
                return new com.edwin.android.cinerd
                        .entity.Theater(theaterName, theaterId);
            }
            return null;
        } finally {
            if (theaterCursor != null) {
                theaterCursor.close();
            }
        }
    }

}
