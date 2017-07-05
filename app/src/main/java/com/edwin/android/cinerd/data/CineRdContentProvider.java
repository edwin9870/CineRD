package com.edwin.android.cinerd.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Edwin Ramirez Ventura on 7/5/2017.
 */

public class CineRdContentProvider extends ContentProvider {

    public static final int FORMAT = 100;
    public static final int FORMAT_WITH_ID = 101;

    public static final int GENRE = 200;
    public static final int GENRE_WITH_ID = 201;

    public static final int LANGUAGE = 300;
    public static final int LANGUAGE_WITH_ID = 301;

    public static final int MOVIE = 400;
    public static final int MOVIE_WITH_ID = 401;

    public static final int MOVIE_GENRE = 500;
    public static final int MOVIE_GENRE_WITH_ID = 501;

    public static final int MOVIE_RATING = 600;
    public static final int MOVIE_RATING_WITH_ID = 601;

    public static final int MOVIE_THEATER_DETAIL = 700;
    public static final int MOVIE_THEATER_DETAIL_WITH_ID = 701;

    public static final int RATING = 800;
    public static final int RATING_WITH_ID = 801;

    public static final int ROOM = 900;
    public static final int ROOM_WITH_ID = 901;

    public static final int SUBTITLE = 1100;
    public static final int SUBTITLE_WITH_ID = 1101;

    public static final int THEATER = 1200;
    public static final int THEATER_WITH_ID = 1201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CineRdDbHelper mCineRdDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_FORMAT, FORMAT);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_FORMAT + "/#", FORMAT_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_LANGUAGE, LANGUAGE);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_LANGUAGE + "/#", LANGUAGE_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_MOVIE, MOVIE);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_MOVIE + "/#", MOVIE_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_MOVIE_GENRE, MOVIE_GENRE);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_MOVIE_GENRE + "/#", MOVIE_GENRE_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_GENRE, GENRE);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_GENRE + "/#", GENRE_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_MOVIE_RATING, MOVIE_RATING);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_MOVIE_RATING + "/#", MOVIE_RATING_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_MOVIE_THEATER_DETAIL, MOVIE_THEATER_DETAIL);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_MOVIE_THEATER_DETAIL + "/#", MOVIE_THEATER_DETAIL_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_RATING, RATING);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_RATING + "/#", RATING_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_ROOM, ROOM);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_ROOM + "/#", ROOM_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_SUBTITLE, SUBTITLE);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_SUBTITLE + "/#", SUBTITLE_WITH_ID);

        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_THEATER, THEATER);
        uriMatcher.addURI(CineRdContract.AUTHORITY, CineRdContract
                .PATH_THEATER + "/#", THEATER_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mCineRdDbHelper = new CineRdDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
