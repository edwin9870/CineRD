package com.edwin.android.cinerd.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;
import com.edwin.android.cinerd.entity.json.Movie;
import com.edwin.android.cinerd.entity.json.Rating;
import com.edwin.android.cinerd.entity.json.Room;
import com.edwin.android.cinerd.entity.json.Theater;
import com.edwin.android.cinerd.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */

@Singleton
public class MovieDataRepository {
    public static final String TAG = MovieDataRepository.class.getSimpleName();
    public static final String ROTTEN_TOMATOES = "RottenTomatoes";
    public static final String IMDB = "IMDB";
    ContentResolver mContentResolver;
    MovieCollector mMovieCollector;

    @Inject
    public MovieDataRepository(ContentResolver contentResolver, MovieCollectorJSON movieCollector) {
        this.mContentResolver = contentResolver;
        this.mMovieCollector = movieCollector;
    }

    public List<Movie> getMovies() {
        return mMovieCollector.getMovies();
    }

    public void process(List<Movie> movies) {
        cleanMovieSchedule();

        for(Movie movie : movies) {
            Log.d(TAG, "Persisting movie: "+ movie);
            processMovie(movie);
            Log.d(TAG, "Movie persisted");
        }
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
                    .COLUMN_NAME_AVAILABLE_DATE + ") < date('" + DateUtil.formatDate
                    (availableDate) + "')", new String[]{String.valueOf(movieId), String.valueOf(theaterId)}, null);

            movieTheaterDetailList = parseMovieTheaterDetail(movieId, movieTheaterDetailCursor);
            movieTheaterDetailCursor.close();

            return movieTheaterDetailList;
        } finally {
            if(movieTheaterDetailCursor != null) {
                movieTheaterDetailCursor.close();
            }
        }
    }

    private List<MovieTheaterDetail> parseMovieTheaterDetail(long movieId, Cursor movieTheaterDetailCursor) {
        List<MovieTheaterDetail> movieTheaterDetailList = new ArrayList<>();
        MovieTheaterDetail movieTheaterDetail;
        while (movieTheaterDetailCursor.moveToNext()) {
            Log.d(TAG, "movieTheaterDetailCursor.getCount(): " + movieTheaterDetailCursor.getCount());
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

            movieTheaterDetailList.add(movieTheaterDetail);
        }

        return movieTheaterDetailList;
    }

    public String getTheaterNameById(int theaterId) {
        Cursor theaterCursor = null;

        try {
            theaterCursor = mContentResolver.query(CineRdContract.TheaterEntry
                            .CONTENT_URI, null,
                    CineRdContract.TheaterEntry._ID + " = ?", new String[]{String.valueOf(theaterId)},

                    null);

            if (theaterCursor.moveToNext()) {
                String theaterName = theaterCursor.getString(theaterCursor.getColumnIndex(CineRdContract.TheaterEntry.COLUMN_NAME_NAME));
                Log.d(TAG, "theaterName: " + theaterName);
                return theaterName;
            }
            return null;
        } finally {
            if(theaterCursor != null) {
                theaterCursor.close();
            }
        }
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


    public long getMovieIdByName(String movieName) {
        long movieId = -1;
        Cursor movieCursor = null;
        try {
            movieCursor = mContentResolver.query(CineRdContract.MovieEntry
                    .CONTENT_URI, new String[]{CineRdContract.MovieEntry._ID}, "UPPER(NAME) = ?", new String[]{movieName}, null);

            if (movieCursor.moveToNext()) {
                movieId = movieCursor.getLong(movieCursor.getColumnIndex(CineRdContract.MovieEntry._ID));
            }

            return movieId;
        }finally {
            if(movieCursor != null) {
                movieCursor.close();
            }
        }
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
                    .COLUMN_NAME_AVAILABLE_DATE + ") < date('" + DateUtil.formatDate
                    (availableDate) + "')", new String[]{String.valueOf(movieId)}, null);

            movieTheaterDetailList = parseMovieTheaterDetail(movieId, movieTheaterDetailCursor);
            movieTheaterDetailCursor.close();

            return movieTheaterDetailList;
        } finally {
            if(movieTheaterDetailCursor != null) {
                movieTheaterDetailCursor.close();
            }
        }
    }

    private int cleanMovieSchedule() {
        int rowsDeleted = mContentResolver.delete(CineRdContract.MovieTheaterDetailEntry.CONTENT_URI,
                null, null);
        Log.d(TAG, "rows deleted: "+ rowsDeleted);
        return rowsDeleted;
    }

    private void processMovie(Movie movie) {
        ContentValues cv = new ContentValues();
        long movieId;

        movieId = persistMovie(movie, cv);
        processMovieDetail(movieId, movie.getTheaters());
        persistMovieRating(movieId, movie.getRating());

        for(String genreName: movie.getGenre()) {
            persistMovieGenre(movieId, genreName);
        }
    }

    private long persistMovie(Movie movie, ContentValues cv) {
        long movieId;Cursor cursor = null;
        try {
            cursor = mContentResolver.query(CineRdContract.MovieEntry.CONTENT_URI, null,
                    CineRdContract.MovieEntry.COLUMN_NAME_NAME + " = ?", new String[]{movie.getName()}, null);

            if (cursor != null && cursor.moveToNext()) {
                movieId = cursor.getLong(cursor.getColumnIndexOrThrow(CineRdContract.MovieEntry
                        ._ID));
                Log.d(TAG, "movieId from table: " + movieId);
            } else {
                cv.put(CineRdContract.MovieEntry.COLUMN_NAME_NAME, movie.getName());
                cv.put(CineRdContract.MovieEntry.COLUMN_NAME_DURATION, movie.getDuration());
                cv.put(CineRdContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, DateUtil.formatDateTime(movie.getReleaseDate()));

                cv.put(CineRdContract.MovieEntry.COLUMN_NAME_SYNOPSIS, movie.getSynopsis());

                movieId = ContentUris.parseId(mContentResolver.insert(CineRdContract.MovieEntry.CONTENT_URI, cv));
                Log.d(TAG, "MovieID generated: " + movieId);
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return movieId;
    }

    private void persistMovieGenre(long movieId, String genreName) {
        long genreId;
        Cursor cursor = null;

        try {
            cursor = mContentResolver.query(CineRdContract.GenreEntry.CONTENT_URI, null,
                    CineRdContract.GenreEntry.COLUMN_NAME_NAME + " = ?", new String[]{genreName}, null);
            genreId = persistGenre(cursor, genreName);

            ContentValues cv = new ContentValues();
            cv.put(CineRdContract.MovieGenreEntry.COLUMN_NAME_GENRE_ID, genreId);
            cv.put(CineRdContract.MovieGenreEntry.COLUMN_NAME_MOVIE_ID, movieId);
            mContentResolver.insert(CineRdContract.MovieGenreEntry.CONTENT_URI, cv);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    private long persistGenre(Cursor cursor, String genreName) {
        long genreId;
        ContentValues cv;
        if (cursor != null && cursor.moveToNext()) {
            genreId = cursor.getLong(cursor.getColumnIndexOrThrow(CineRdContract.GenreEntry._ID));
            Log.d(TAG, "genreId from table: "+ genreId);
        } else {
            cv = new ContentValues();
            cv.put(CineRdContract.GenreEntry.COLUMN_NAME_NAME, genreName);
            genreId = ContentUris.parseId(mContentResolver.insert(CineRdContract.GenreEntry.CONTENT_URI,
                    cv));
        }
        return genreId;
    }

    private void persistMovieRating(long movieId, Rating rating) {

        short rottenTomatoesRatingId = persistRating(MovieDataRepository.ROTTEN_TOMATOES);
        short imdbRattingId = persistRating(MovieDataRepository.IMDB);

        ContentValues cv = new ContentValues();
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_MOVIE_ID, movieId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER, rottenTomatoesRatingId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING, rating.getRottentomatoes());
        mContentResolver.insert(CineRdContract.MovieRatingEntry.CONTENT_URI, cv);

        cv = new ContentValues();
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_MOVIE_ID, movieId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER, imdbRattingId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING, rating.getImdb());
        mContentResolver.insert(CineRdContract.MovieRatingEntry.CONTENT_URI, cv);
    }

    private short persistRating(String ratingProvider) {
        Cursor cursor = null;
        short ratingId;
        try {
            cursor = mContentResolver.query(CineRdContract.RatingEntry.CONTENT_URI, null,
                    CineRdContract.RatingEntry.COLUMN_NAME_NAME + " = ?",
                    new String[]{ratingProvider}, null);
            if (cursor != null && cursor.moveToNext()) {
                ratingId = cursor.getShort(cursor.getColumnIndexOrThrow(CineRdContract.RatingEntry._ID));

                return ratingId;
            } else {
                ContentValues cv = new ContentValues();
                cv.put(CineRdContract.RatingEntry.COLUMN_NAME_NAME, ratingProvider);
                ratingId = (short) ContentUris.parseId(mContentResolver.insert(CineRdContract.RatingEntry.CONTENT_URI, cv));
                return ratingId;
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    private void processMovieDetail(long movieId, List<Theater> theaters) {
        Integer theaterId;
        long roomId;
        short subtitleId;
        short formatId;
        short languageId;
        for(Theater theater: theaters) {
            theaterId = persistMovieTheater(theater);
            for(Room room : theater.getRoom()) {
                roomId = persistRoom(theaterId, room);
                formatId = persistFormat(room);
                languageId = persistLanguage(room);

                if(room.getSubtitle() != null && !room.getSubtitle().isEmpty()) {
                    subtitleId = persistSubtitle(room);
                } else {
                    subtitleId = 0;
                }

                persistRoom(movieId, theaterId, roomId, subtitleId, formatId,
                        languageId, room);

            }
        }
    }

    @NonNull
    private Integer persistMovieTheater(Theater theater) {
        ContentValues cv;
        Integer theaterId;
        cv = new ContentValues();
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(CineRdContract.TheaterEntry.CONTENT_URI, null,
                    CineRdContract.TheaterEntry.COLUMN_NAME_NAME + " = ?", new String[]{theater
                            .getName()}, null);
            if (cursor != null && cursor.moveToNext()) {
                theaterId = cursor.getInt(cursor.getColumnIndexOrThrow(CineRdContract
                        .TheaterEntry._ID));
            } else {
                cv.put(CineRdContract.TheaterEntry.COLUMN_NAME_NAME, theater.getName());
                theaterId = (int) ContentUris.parseId(mContentResolver.insert(CineRdContract
                        .TheaterEntry.CONTENT_URI, cv));
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return theaterId;
    }

    private void persistRoom(long movieId, Integer theaterId, long
            roomId, short subtitleId, short formatId, short languageId, Room room) {
        ContentValues cv;
        cv = new ContentValues();
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_MOVIE_ID, movieId);
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_THEATER_ID, theaterId);
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_ROOM_ID, roomId);

        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_AVAILABLE_DATE, DateUtil.formatDateTime(room.getDate()));
        if(subtitleId > 0) {
            cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_SUBTITLE_ID,
                    subtitleId);
        }
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_FORMAT_ID, formatId);
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_LANGUAGE_ID, languageId);
        mContentResolver.insert(CineRdContract.MovieTheaterDetailEntry.CONTENT_URI, cv);
    }

    private short persistSubtitle(Room room) {
        ContentValues cv;
        short subtitleId;
        cv = new ContentValues();
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(CineRdContract.SubtitleEntry.CONTENT_URI, null,

                    CineRdContract.SubtitleEntry.COLUMN_NAME_NAME + " = ?", new String[]{room.getSubtitle()}, null);
            if (cursor != null && cursor.moveToNext()) {
                subtitleId = cursor.getShort(cursor.getColumnIndexOrThrow(CineRdContract
                        .SubtitleEntry._ID));
            } else {
                cv.put(CineRdContract.SubtitleEntry.COLUMN_NAME_NAME, room.getSubtitle());
                subtitleId = (short) ContentUris.parseId(mContentResolver.insert(CineRdContract.SubtitleEntry.CONTENT_URI, cv));
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return subtitleId;
    }

    private short persistLanguage(Room room) {
        ContentValues cv;
        short languageId;
        cv = new ContentValues();
        Cursor cursor = null;
        cursor = mContentResolver.query(CineRdContract.LanguageEntry.CONTENT_URI, null,
                CineRdContract.LanguageEntry.COLUMN_NAME_NAME + " = ?", new String[]{room.getLanguage()}, null);
        if (cursor != null && cursor.moveToNext()) {
            languageId = cursor.getShort(cursor.getColumnIndexOrThrow(CineRdContract
                    .LanguageEntry._ID));
        } else {
            cv.put(CineRdContract.LanguageEntry.COLUMN_NAME_NAME, room.getLanguage());
            languageId = (short) ContentUris.parseId(mContentResolver.insert
                    (CineRdContract.LanguageEntry.CONTENT_URI, cv));
        }
        return languageId;
    }

    private short persistFormat(Room room) {
        ContentValues cv;
        short formatId;
        cv = new ContentValues();
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(CineRdContract.FormatEntry.CONTENT_URI, null,
                    CineRdContract.FormatEntry.COLUMN_NAME_NAME + " = ?", new String[]{room.getFormat()}, null);

            cv.put(CineRdContract.FormatEntry.COLUMN_NAME_NAME, room.getFormat());
            if (cursor != null && cursor.moveToNext()) {
                formatId = cursor.getShort(cursor.getColumnIndexOrThrow(CineRdContract
                        .FormatEntry._ID));
            } else {
                formatId = (short) ContentUris.parseId(mContentResolver.insert(CineRdContract
                        .FormatEntry.CONTENT_URI, cv));
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return formatId;
    }

    private long persistRoom(Integer theaterId,
                             Room room) {
        long roomId;
        Cursor cursor = null;
        ContentValues cv = new ContentValues();
        try {
            cursor = mContentResolver.query(CineRdContract.RoomEntry.CONTENT_URI, null,
                    CineRdContract.RoomEntry.COLUMN_NAME_NUMBER + " = ? AND " + CineRdContract

                            .RoomEntry.COLUMN_NAME_THEATER_ID + " = ?", new String[]{room
                            .getNumber(), theaterId.toString()}, null);
            if (cursor != null && cursor.moveToNext()) {
                roomId = cursor.getLong(cursor.getColumnIndexOrThrow(CineRdContract.RoomEntry._ID));
            } else {
                cv.put(CineRdContract.RoomEntry.COLUMN_NAME_NUMBER, room.getNumber());
                cv.put(CineRdContract.RoomEntry.COLUMN_NAME_THEATER_ID, theaterId);
                roomId = ContentUris.parseId(mContentResolver.insert(CineRdContract
                        .RoomEntry.CONTENT_URI, cv));
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return roomId;
    }

}
