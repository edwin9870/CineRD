package com.edwin.android.cinerd.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Edwin Ramirez Ventura on 7/3/2017.
 */

public class CineRdContract {

    public static final String AUTHORITY = "com.edwin.android.cinerd";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIE = "MOVIE";
    public static final String PATH_FORMAT = "FORMAT";
    public static final String PATH_GENRE = "GENRE";
    public static final String PATH_LANGUAGE = "LANGUAGE";
    public static final String PATH_MOVIE_GENRE = "MOVIE_GENRE";
    public static final String PATH_MOVIE_RATING = "MOVIE_RATING";
    public static final String PATH_MOVIE_THEATER_DETAIL = "MOVIE_THEATER_DETAIL";
    public static final String PATH_RATING = "RATING";
    public static final String PATH_ROOM = "ROOM";
    public static final String PATH_SUBTITLE = "SUBTITLE";
    public static final String PATH_THEATER = "THEATER";

    public static class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "MOVIE";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_DURATION = "DURATION";
        public static final String COLUMN_NAME_RELEASE_DATE = "RELEASE_DATE";
        public static final String COLUMN_NAME_SYNOPSIS = "SYNOPSIS";
    }

    public static class FormatEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FORMAT).build();

        public static final String TABLE_NAME = "FORMAT";
        public static final String COLUMN_NAME_NAME = "NAME";
    }

    public static class GenreEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

        public static final String TABLE_NAME = "GENRE";
        public static final String COLUMN_NAME_NAME = "NAME";
    }

    public static class LanguageEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LANGUAGE).build();

        public static final String TABLE_NAME = "LANGUAGE";
        public static final String COLUMN_NAME_NAME = "NAME";
    }

    public static class MovieGenreEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_GENRE).build();

        public static final String TABLE_NAME = "MOVIE_GENRE";
        public static final String COLUMN_NAME_MOVIE_ID = "MOVIE_ID";
        public static final String COLUMN_NAME_GENRE_ID = "GENRE_ID";
    }

    public static class MovieRatingEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_RATING).build();

        public static final String TABLE_NAME = "MOVIE_RATING";
        public static final String COLUMN_NAME_MOVIE_ID = "MOVIE_ID";
        public static final String COLUMN_NAME_RATING_PROVIDER = "RATING_PROVIDER";
        public static final String COLUMN_NAME_RATING = "RATING";
    }

    public static class MovieTheaterDetailEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_THEATER_DETAIL).build();

        public static final String TABLE_NAME = "MOVIE_THEATER_DETAIL";
        public static final String COLUMN_NAME_MOVIE_ID = "MOVIE_ID";
        public static final String COLUMN_NAME_THEATER_ID = "THEATER_ID";
        public static final String COLUMN_NAME_ROOM_ID = "ROOM_ID";
        public static final String COLUMN_NAME_AVAILABLE_DATE = "AVAILABLE_DATE";
        public static final String COLUMN_NAME_SUBTITLE_ID = "SUBTITLE_ID";
        public static final String COLUMN_NAME_FORMAT_ID = "FORMAT_ID";
        public static final String COLUMN_NAME_LANGUAGE_ID = "LANGUAGE_ID";
    }

    public static class RatingEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RATING).build();

        public static final String TABLE_NAME = "RATING";
        public static final String COLUMN_NAME_NAME = "NAME";
    }

    public static class RoomEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROOM).build();

        public static final String TABLE_NAME = "ROOM";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_NUMBER = "NUMBER";
        public static final String COLUMN_NAME_THEATER_ID = "THEATER_ID";
    }

    public static class SubtitleEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBTITLE).build();

        public static final String TABLE_NAME = "SUBTITLE";
        public static final String COLUMN_NAME_NAME = "NAME";
    }

    public static class TheaterEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_THEATER).build();

        public static final String TABLE_NAME = "THEATER";
        public static final String COLUMN_NAME_NAME = "NAME";
    }
}
