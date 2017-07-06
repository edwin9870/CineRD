package com.edwin.android.cinerd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edwin.android.cinerd.data.CineRdContract.FormatEntry;
import com.edwin.android.cinerd.data.CineRdContract.GenreEntry;
import com.edwin.android.cinerd.data.CineRdContract.LanguageEntry;
import com.edwin.android.cinerd.data.CineRdContract.MovieEntry;
import com.edwin.android.cinerd.data.CineRdContract.MovieGenreEntry;
import com.edwin.android.cinerd.data.CineRdContract.MovieRatingEntry;
import com.edwin.android.cinerd.data.CineRdContract.MovieTheaterDetailEntry;
import com.edwin.android.cinerd.data.CineRdContract.RatingEntry;
import com.edwin.android.cinerd.data.CineRdContract.RoomEntry;
import com.edwin.android.cinerd.data.CineRdContract.SubtitleEntry;
import com.edwin.android.cinerd.data.CineRdContract.TheaterEntry;

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
                MovieEntry.COLUMN_NAME_SYNOPSIS + " TEXT NOT NULL," +
                MovieEntry.COLUMN_NAME_RELEASE_DATE +  " RELEASE_DATE DATETIME NOT NULL);" +
                "CREATE UNIQUE INDEX MOVIE_NAME_uindex ON "+MovieEntry.TABLE_NAME+" ("+MovieEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_FORMAT_TABLE = "CREATE TABLE " +
                FormatEntry.TABLE_NAME + " (" +
                FormatEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FormatEntry.COLUMN_NAME_NAME + " VARCHAR(10) NOT NULL);" +
                "CREATE UNIQUE INDEX FORMAT_NAME_uindex ON "+FormatEntry.TABLE_NAME+" ("+FormatEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_FORMAT_TABLE);

        final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " +
                GenreEntry.TABLE_NAME + " (" +
                GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                GenreEntry.COLUMN_NAME_NAME + " VARCHAR(30) NOT NULL);" +
                "CREATE UNIQUE INDEX GENRE_NAME_uindex ON "+GenreEntry.TABLE_NAME+" ("+GenreEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_GENRE_TABLE);

        final String SQL_CREATE_LANGUAGE_TABLE = "CREATE TABLE " +
                LanguageEntry.TABLE_NAME + " (" +
                GenreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                GenreEntry.COLUMN_NAME_NAME + " VARCHAR(15) NOT NULL);" +
                "CREATE UNIQUE INDEX LANGUAGE_NAME_uindex ON "+LanguageEntry.TABLE_NAME+" ("+LanguageEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_LANGUAGE_TABLE);

        final String SQL_CREATE_MOVIE_GENRE_TABLE = "CREATE TABLE " +
                MovieGenreEntry.TABLE_NAME + " (" +
                MovieGenreEntry.COLUMN_NAME_MOVIE_ID + " INTEGER NOT NULL," +
                MovieGenreEntry.COLUMN_NAME_GENRE_ID + " INTEGER NOT NULL," +
                " CONSTRAINT MOVIE_GENRE_MOVIE_ID_fk FOREIGN KEY ("+MovieGenreEntry.COLUMN_NAME_MOVIE_ID+") REFERENCES "+MovieEntry.TABLE_NAME+" ("+MovieEntry._ID+")," +
                " CONSTRAINT MOVIE_GENRE_GENRE_ID_fk FOREIGN KEY ("+MovieGenreEntry.COLUMN_NAME_GENRE_ID+") REFERENCES "+GenreEntry.TABLE_NAME+" ("+GenreEntry._ID+"));";
        db.execSQL(SQL_CREATE_MOVIE_GENRE_TABLE);

        final String SQL_CREATE_RATING_TABLE = "CREATE TABLE " +
                RatingEntry.TABLE_NAME + " (" +
                RatingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RatingEntry.COLUMN_NAME_NAME + " VARCHAR(10) NOT NULL);" +
                "CREATE UNIQUE INDEX RATING_NAME_uindex ON "+RatingEntry.TABLE_NAME+" ("+RatingEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_RATING_TABLE);

        final String SQL_CREATE_THEATER_TABLE = "CREATE TABLE " +
                TheaterEntry.TABLE_NAME + " (" +
                TheaterEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TheaterEntry.COLUMN_NAME_NAME + " VARCHAR(40) NOT NULL);" +
                " CREATE UNIQUE INDEX THEATER_NAME_uindex ON "+TheaterEntry.TABLE_NAME+" ("+TheaterEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_THEATER_TABLE);

        final String SQL_CREATE_MOVIE_RATING_TABLE = "CREATE TABLE " +
                MovieRatingEntry.TABLE_NAME + " (" +
                MovieRatingEntry.COLUMN_NAME_MOVIE_ID + " INTEGER NOT NULL," +
                MovieRatingEntry.COLUMN_NAME_RATING + " VARCHAR(3) NOT NULL," +
                MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER + " INTEGER NOT NULL," +
                " CONSTRAINT MOVIE_RATING_MOVIE_ID_fk FOREIGN KEY ("+MovieRatingEntry.COLUMN_NAME_MOVIE_ID+") REFERENCES "+MovieEntry.TABLE_NAME+" ("+MovieEntry._ID+")," +
                " CONSTRAINT MOVIE_RATING_RATING_ID_fk FOREIGN KEY ("+MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER+") REFERENCES "+RatingEntry.TABLE_NAME+" ("+RatingEntry._ID+"));";
        db.execSQL(SQL_CREATE_MOVIE_RATING_TABLE);

        final String SQL_CREATE_SUBTITLE_TABLE = "CREATE TABLE " +
                SubtitleEntry.TABLE_NAME + " (" +
                SubtitleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SubtitleEntry.COLUMN_NAME_NAME + " VARCHAR(10) NOT NULL);" +
                "CREATE UNIQUE INDEX SUBTITLE_NAME_uindex ON "+SubtitleEntry.TABLE_NAME+" ("+SubtitleEntry.COLUMN_NAME_NAME+");";
        db.execSQL(SQL_CREATE_SUBTITLE_TABLE);

        final String SQL_CREATE_ROOM_TABLE = "CREATE TABLE " +
                RoomEntry.TABLE_NAME + " (" +
                RoomEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RoomEntry.COLUMN_NAME_NUMBER + " INTEGER(20) NOT NULL," +
                RoomEntry.COLUMN_NAME_THEATER_ID + " INTEGER NOT NULL," +
                " CONSTRAINT ROOM_THEATER_ID_fk FOREIGN KEY ("+RoomEntry.COLUMN_NAME_THEATER_ID+") REFERENCES "+ TheaterEntry.TABLE_NAME+" ("+TheaterEntry._ID+"));";
        db.execSQL(SQL_CREATE_ROOM_TABLE);

        final String SQL_CREATE_MOVIE_THEATER_DETAIL_TABLE = "CREATE TABLE "+
                MovieTheaterDetailEntry.TABLE_NAME+" (" +
                "    "+MovieTheaterDetailEntry.COLUMN_NAME_MOVIE_ID+" INTEGER NOT NULL," +
                "    "+MovieTheaterDetailEntry.COLUMN_NAME_THEATER_ID+" INTEGER NOT NULL," +
                "    "+MovieTheaterDetailEntry.COLUMN_NAME_ROOM_ID+" INTEGER NOT NULL," +
                "    "+MovieTheaterDetailEntry.COLUMN_NAME_AVAILABLE_DATE+" DATETIME NOT NULL," +
                "    "+MovieTheaterDetailEntry.COLUMN_NAME_SUBTITLE_ID+" INTEGER," +
                "    "+MovieTheaterDetailEntry.COLUMN_NAME_FORMAT_ID+" INTEGER NOT NULL," +
                "    "+MovieTheaterDetailEntry.COLUMN_NAME_LANGUAGE_ID+" INTEGER NOT NULL," +
                "    CONSTRAINT MOVIE_THEATER_DETAIL_MOVIE_ID_fk FOREIGN KEY ("+MovieTheaterDetailEntry.COLUMN_NAME_MOVIE_ID+") REFERENCES MOVIE ("+MovieEntry._ID+")," +
                "    CONSTRAINT MOVIE_THEATER_DETAIL_THEATER_ID_fk FOREIGN KEY (THEATER_ID) REFERENCES "+TheaterEntry.TABLE_NAME+" ("+TheaterEntry._ID+")," +
                "    CONSTRAINT MOVIE_THEATER_DETAIL_ROOM_ID_fk FOREIGN KEY (ROOM_ID) REFERENCES "+RoomEntry.TABLE_NAME+" ("+RoomEntry._ID+")," +
                "    CONSTRAINT MOVIE_THEATER_DETAIL_SUBTITLE_ID_fk FOREIGN KEY (SUBTITLE_ID) REFERENCES "+SubtitleEntry.TABLE_NAME+" ("+SubtitleEntry._ID+")," +
                "    CONSTRAINT MOVIE_THEATER_DETAIL_FORMAT_ID_fk FOREIGN KEY (FORMAT_ID) REFERENCES "+FormatEntry.TABLE_NAME+" ("+FormatEntry._ID+")," +
                "    CONSTRAINT MOVIE_THEATER_DETAIL_LANGUAGE_ID_fk FOREIGN KEY (LANGUAGE_ID) REFERENCES "+LanguageEntry.TABLE_NAME+" ("+LanguageEntry._ID+")" +
                ");";
        db.execSQL(SQL_CREATE_MOVIE_THEATER_DETAIL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
