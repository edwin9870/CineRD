package com.edwin.android.cinerd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edwin.android.cinerd.data.CineRdContract.FormatEntry;
import com.edwin.android.cinerd.data.CineRdContract.GenreEntry;
import com.edwin.android.cinerd.data.CineRdContract.LanguageEntry;
import com.edwin.android.cinerd.data.CineRdContract.MovieEntry;

/**
 * Created by Edwin Ramirez Ventura on 7/4/2017.
 */

public class CineRdDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "cine_rd.db";
    public static final String TAG = CineRdDbHelper.class.getSimpleName();

    public CineRdDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_NAME_NAME + " VARCHAR(40) NOT NULL," +
                MovieEntry.COLUMN_NAME_DURATION + " INT(3) NOT NULL," +
                MovieEntry.COLUMN_NAME_RELEASE_DATE +  " RELEASE_DATE DATETIME NOT NULL);" +
                "CREATE UNIQUE INDEX MOVIE_NAME_uindex ON MOVIE ("+MovieEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_FORMAT_TABLE = "CREATE TABLE " +
                FormatEntry.TABLE_NAME + " (" +
                FormatEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FormatEntry.COLUMN_NAME_NAME + " VARCHAR(10) NOT NULL);" +
                "CREATE UNIQUE INDEX FORMAT_NAME_uindex ON FORMAT ("+FormatEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_FORMAT_TABLE);

        final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " +
                GenreEntry.TABLE_NAME + " (" +
                GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                GenreEntry.COLUMN_NAME_NAME + " VARCHAR(30) NOT NULL);" +
                "CREATE UNIQUE INDEX GENRE_NAME_uindex ON GENRE ("+GenreEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_GENRE_TABLE);

        final String SQL_CREATE_LANGUAGE_TABLE = "CREATE TABLE " +
                LanguageEntry.TABLE_NAME + " (" +
                GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                GenreEntry.COLUMN_NAME_NAME + " VARCHAR(15) NOT NULL);" +
                "CREATE UNIQUE INDEX LANGUAGE_NAME_uindex ON LANGUAGE ("+LanguageEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_LANGUAGE_TABLE);

        final String SQL_CREATE_LANGUAGE_TABLE = "CREATE TABLE " +
                LanguageEntry.TABLE_NAME + " (" +
                GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                GenreEntry.COLUMN_NAME_NAME + " VARCHAR(15) NOT NULL);" +
                "CREATE UNIQUE INDEX GENRE_NAME_uindex ON GENRE ("+LanguageEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_LANGUAGE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
