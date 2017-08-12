package com.edwin.android.cinerd.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.edwin.android.cinerd.data.CineRdContract.FormatEntry;
import com.edwin.android.cinerd.data.CineRdContract.LanguageEntry;
import com.edwin.android.cinerd.data.CineRdContract.MovieGenreEntry;

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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String
            selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mCineRdDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor refCursor;
        switch (match) {
            case FORMAT:
                refCursor = db.query(FormatEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case GENRE:
                refCursor = db.query(CineRdContract.GenreEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case LANGUAGE:
                refCursor = db.query(LanguageEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case MOVIE:
                refCursor = db.query(CineRdContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case MOVIE_GENRE:
                refCursor = db.query(MovieGenreEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case MOVIE_RATING:
                refCursor = db.query(CineRdContract.MovieRatingEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case MOVIE_THEATER_DETAIL:
                refCursor = db.query(CineRdContract.MovieTheaterDetailEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case RATING:
                refCursor = db.query(CineRdContract.RatingEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case ROOM:
                refCursor = db.query(CineRdContract.RoomEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case SUBTITLE:
                refCursor = db.query(CineRdContract.SubtitleEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;
            case THEATER:
                refCursor = db.query(CineRdContract.TheaterEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                        null, sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        refCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return refCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mCineRdDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri uriToReturn;
        long id;
        switch (match) {
            case FORMAT:
                id = db.insertWithOnConflict(CineRdContract.FormatEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.FormatEntry.CONTENT_URI, id);
                break;
            case GENRE:
                id = db.insertWithOnConflict(CineRdContract.GenreEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.GenreEntry.CONTENT_URI, id);
                break;
            case LANGUAGE:
                id = db.insertWithOnConflict(CineRdContract.LanguageEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.LanguageEntry.CONTENT_URI, id);
                break;
            case MOVIE:
                id = db.insertWithOnConflict(CineRdContract.MovieEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.MovieEntry.CONTENT_URI, id);
                break;
            case MOVIE_GENRE:
                id = db.insertWithOnConflict(CineRdContract.MovieGenreEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.MovieGenreEntry.CONTENT_URI, id);
                break;
            case MOVIE_RATING:
                id = db.insertWithOnConflict(CineRdContract.MovieRatingEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.MovieRatingEntry.CONTENT_URI, id);
                break;
            case MOVIE_THEATER_DETAIL:
                id = db.insertWithOnConflict(CineRdContract.MovieTheaterDetailEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.MovieTheaterDetailEntry.CONTENT_URI, id);
                break;
            case RATING:
                id = db.insertWithOnConflict(CineRdContract.RatingEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.RatingEntry.CONTENT_URI, id);
                break;
            case ROOM:
                id = db.insertWithOnConflict(CineRdContract.RoomEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.RoomEntry.CONTENT_URI, id);
                break;
            case SUBTITLE:
                id = db.insertWithOnConflict(CineRdContract.SubtitleEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.SubtitleEntry.CONTENT_URI, id);
                break;
            case THEATER:
                id = db.insertWithOnConflict(CineRdContract.TheaterEntry.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                uriToReturn = ContentUris.withAppendedId(CineRdContract.TheaterEntry.CONTENT_URI, id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriToReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mCineRdDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        String id;
        switch (match) {
            case FORMAT_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(FormatEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case GENRE_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CineRdContract.GenreEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case LANGUAGE_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(LanguageEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case MOVIE_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CineRdContract.MovieEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case MOVIE_GENRE_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(MovieGenreEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case MOVIE_RATING_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CineRdContract.MovieRatingEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case MOVIE_THEATER_DETAIL_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CineRdContract.MovieTheaterDetailEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case RATING_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CineRdContract.RatingEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case ROOM_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CineRdContract.RoomEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case SUBTITLE_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CineRdContract.SubtitleEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case THEATER_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(CineRdContract.TheaterEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case MOVIE_THEATER_DETAIL:
                tasksDeleted = db.delete(CineRdContract.MovieTheaterDetailEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_GENRE:
                tasksDeleted = db.delete(CineRdContract.MovieGenreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_RATING:
                tasksDeleted = db.delete(CineRdContract.MovieRatingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FORMAT:
                tasksDeleted = db.delete(CineRdContract.FormatEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GENRE:
                tasksDeleted = db.delete(CineRdContract.GenreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LANGUAGE:
                tasksDeleted = db.delete(CineRdContract.LanguageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE:
                tasksDeleted = db.delete(CineRdContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case RATING:
                tasksDeleted = db.delete(CineRdContract.RatingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ROOM:
                tasksDeleted = db.delete(CineRdContract.RoomEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SUBTITLE:
                tasksDeleted = db.delete(CineRdContract.SubtitleEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case THEATER:
                tasksDeleted = db.delete(CineRdContract.TheaterEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
