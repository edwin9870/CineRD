package com.edwin.android.cinerd.data.repositories;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.edwin.android.cinerd.data.CineRdContract;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 8/5/2017.
 */

@Singleton
public class FormatRepository {

    public static final String TAG = FormatRepository.class.getSimpleName();
    private final ContentResolver mContentResolver;
    private Context mContext;

    @Inject
    public FormatRepository(Context context) {
        this.mContext = context;
        mContentResolver = context.getContentResolver();
    }

    public String getFormatNameById(int formatId) {
        String formatName = "";
        Cursor formatCursor = null;
        try {
            formatCursor = mContentResolver.query(CineRdContract.FormatEntry

                    .CONTENT_URI, null, CineRdContract.FormatEntry._ID + "=?", new
                    String[]{String.valueOf(formatId)}, null);
            if (formatCursor.moveToNext()) {
                formatName = formatCursor.getString(formatCursor.getColumnIndex(CineRdContract.FormatEntry.COLUMN_NAME_NAME));
                Log.d(TAG, "format name: " + formatName);
            }
            return formatName;
        } finally {
            if(formatCursor != null) {
                formatCursor.close();
            }
        }
    }

}
