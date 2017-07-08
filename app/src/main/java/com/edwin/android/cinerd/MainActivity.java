package com.edwin.android.cinerd;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edwin.android.cinerd.data.CineRdContract;
import com.edwin.android.cinerd.data.CineRdContract.GenreEntry;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Movies;
import com.edwin.android.cinerd.entity.Rating;
import com.edwin.android.cinerd.entity.Room;
import com.edwin.android.cinerd.entity.Theater;
import com.edwin.android.cinerd.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String ROTTEN_TOMATOES = "RottenTomatoes";
    public static final String IMDB = "IMDB";
    @BindView(R.id.recycler_view_movie_poster)
    RecyclerView recyclerViewMoviePoster;
    @BindView(R.id.floating_button_movie_menu)
    FloatingActionButton floatingButtonMovieMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        floatingButtonMovieMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        String jsonFromAsset = JsonUtil.loadJSONFromAsset(MainActivity.this, "data.json");
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();;

                        Movies movies = gson.fromJson(jsonFromAsset, Movies.class);
                        Log.d(TAG, "Json: "+jsonFromAsset);
                        Log.d(TAG, "Movies: "+movies);

                        ContentResolver contentResolver = MainActivity.this.getContentResolver();
                        cleanMovieSchedule(contentResolver);

                        for(Movie movie : movies.getMovies()) {
                            Log.d(TAG, "Persisting movie: "+ movie);
                            processMovie(contentResolver, movie);
                            Log.d(TAG, "Movie persisted");
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Sync completed", Toast.LENGTH_SHORT).show();
                    }
                }.execute();
            }
        });
    }

    private int cleanMovieSchedule(ContentResolver contentResolver) {
        int rowsDeleted = contentResolver.delete(CineRdContract.MovieTheaterDetailEntry.CONTENT_URI,
                null, null);
        Log.d(TAG, "rows deleted: "+ rowsDeleted);
        return rowsDeleted;
    }

    private void processMovie(ContentResolver contentResolver, Movie movie) {
        ContentValues cv = new ContentValues();
        long movieId;

        movieId = persistMovie(contentResolver, movie, cv);

        processMovieDetail(movieId, contentResolver, movie.getTheaters());
        persistRating(movieId, contentResolver, movie.getRating());

        for(String genreName: movie.getGenre()) {
            persistMovieGenre(contentResolver, movieId, genreName);
        }
    }

    private long persistMovie(ContentResolver contentResolver, Movie movie, ContentValues cv) {
        long movieId;Cursor cursor = null;
        try {
            cursor = contentResolver.query(CineRdContract.MovieEntry.CONTENT_URI, null,
                    CineRdContract.MovieEntry.COLUMN_NAME_NAME + " = ?", new String[]{movie.getName()}, null);

            if (cursor != null && cursor.moveToNext()) {
                movieId = cursor.getLong(cursor.getColumnIndexOrThrow(CineRdContract.MovieEntry
                        ._ID));
                Log.d(TAG, "movieId from table: " + movieId);
            } else {
                cv.put(CineRdContract.MovieEntry.COLUMN_NAME_NAME, movie.getName());
                cv.put(CineRdContract.MovieEntry.COLUMN_NAME_DURATION, movie.getDuration());
                cv.put(CineRdContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate().toString());

                cv.put(CineRdContract.MovieEntry.COLUMN_NAME_SYNOPSIS, movie.getSynopsis());

                movieId = ContentUris.parseId(contentResolver.insert(CineRdContract.MovieEntry.CONTENT_URI, cv));
                Log.d(TAG, "MovieID generated: " + movieId);
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return movieId;
    }

    private void persistMovieGenre(ContentResolver contentResolver, long movieId, String genreName) {
        long genreId;
        Cursor cursor = null;

        try {
            cursor = contentResolver.query(GenreEntry.CONTENT_URI, null,
                    GenreEntry.COLUMN_NAME_NAME + " = ?", new String[]{genreName}, null);
            genreId = persistGenre(contentResolver, cursor, genreName);

            ContentValues cv = new ContentValues();
            cv.put(CineRdContract.MovieGenreEntry.COLUMN_NAME_GENRE_ID, genreId);
            cv.put(CineRdContract.MovieGenreEntry.COLUMN_NAME_MOVIE_ID, movieId);
            contentResolver.insert(CineRdContract.MovieGenreEntry.CONTENT_URI, cv);
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    private long persistGenre(ContentResolver contentResolver, Cursor cursor, String genreName) {
        long genreId;
        ContentValues cv;
        if (cursor != null && cursor.moveToNext()) {
            genreId = cursor.getLong(cursor.getColumnIndexOrThrow(GenreEntry._ID));
            Log.d(TAG, "genreId from table: "+ genreId);
        } else {
            cv = new ContentValues();
            cv.put(GenreEntry.COLUMN_NAME_NAME, genreName);
            genreId = ContentUris.parseId(contentResolver.insert(GenreEntry.CONTENT_URI,
                    cv));
        }
        return genreId;
    }

    private void persistRating(long movieId, ContentResolver contentResolver, Rating rating) {

        short rottenTomatoesRatingId = persistRating(contentResolver, ROTTEN_TOMATOES);
        short imdbRattingId = persistRating(contentResolver, IMDB);

        ContentValues cv = new ContentValues();
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_MOVIE_ID, movieId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER, rottenTomatoesRatingId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING, rating.getRottentomatoes());
        contentResolver.insert(CineRdContract.MovieRatingEntry.CONTENT_URI, cv);

        cv = new ContentValues();
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_MOVIE_ID, movieId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER, imdbRattingId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING, rating.getImdb());
        contentResolver.insert(CineRdContract.MovieRatingEntry.CONTENT_URI, cv);
    }

    private short persistRating(ContentResolver contentResolver, String ratingProvider) {
        Cursor cursor = null;
        short ratingId;
        try {
            cursor = contentResolver.query(CineRdContract.RatingEntry.CONTENT_URI, null,
                    CineRdContract.RatingEntry.COLUMN_NAME_NAME + " = ?",
                    new String[]{ratingProvider}, null);
            if (cursor != null && cursor.moveToNext()) {
                ratingId = cursor.getShort(cursor.getColumnIndexOrThrow(CineRdContract.RatingEntry._ID));

                return ratingId;
            } else {
                ContentValues cv = new ContentValues();
                cv.put(CineRdContract.RatingEntry.COLUMN_NAME_NAME, ratingProvider);
                ratingId = (short) ContentUris.parseId(contentResolver.insert(CineRdContract.RatingEntry.CONTENT_URI, cv));
                return ratingId;
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    private void processMovieDetail(long movieId, ContentResolver contentResolver, List<Theater> theaters) {
        Integer theaterId;
        long roomId;
        short subtitleId;
        short formatId;
        short languageId;
        for(Theater theater: theaters) {
            theaterId = getMovieTheater(contentResolver, theater);
            for(Room room : theater.getRoom()) {
                roomId = persistRoom(contentResolver, theaterId, room);
                formatId = persistFormat(contentResolver, room);
                languageId = persistLanguage(contentResolver, room);

                if(room.getSubtitle() != null && !room.getSubtitle().isEmpty()) {
                    subtitleId = persistSubtitle(contentResolver, room);
                } else {
                    subtitleId = 0;
                }

                persistRoom(movieId, contentResolver, theaterId, roomId, subtitleId, formatId,
                        languageId, room);

            }
        }
    }

    @NonNull
    private Integer getMovieTheater(ContentResolver contentResolver, Theater theater) {
        ContentValues cv;
        Integer theaterId;
        cv = new ContentValues();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(CineRdContract.TheaterEntry.CONTENT_URI, null,
                    CineRdContract.TheaterEntry.COLUMN_NAME_NAME + " = ?", new String[]{theater
                            .getName()}, null);
            if (cursor != null && cursor.moveToNext()) {
                theaterId = cursor.getInt(cursor.getColumnIndexOrThrow(CineRdContract
                        .TheaterEntry._ID));
            } else {
                cv.put(CineRdContract.TheaterEntry.COLUMN_NAME_NAME, theater.getName());
                theaterId = (int) ContentUris.parseId(contentResolver.insert(CineRdContract
                        .TheaterEntry.CONTENT_URI, cv));
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return theaterId;
    }

    private void persistRoom(long movieId, ContentResolver contentResolver, Integer theaterId, long
            roomId, short subtitleId, short formatId, short languageId, Room room) {
        ContentValues cv;
        cv = new ContentValues();
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_MOVIE_ID, movieId);
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_THEATER_ID, theaterId);
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_ROOM_ID, roomId);
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_AVAILABLE_DATE, room.getDate()
        .toString());
        if(subtitleId > 0) {
            cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_SUBTITLE_ID,
                    subtitleId);
        }
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_FORMAT_ID, formatId);
        cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_LANGUAGE_ID, languageId);
        contentResolver.insert(CineRdContract.MovieTheaterDetailEntry.CONTENT_URI, cv);
    }

    private short persistSubtitle(ContentResolver contentResolver, Room room) {
        ContentValues cv;
        short subtitleId;
        cv = new ContentValues();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(CineRdContract.SubtitleEntry.CONTENT_URI, null,

                    CineRdContract.SubtitleEntry.COLUMN_NAME_NAME + " = ?", new String[]{room.getSubtitle()}, null);
            if (cursor != null && cursor.moveToNext()) {
                subtitleId = cursor.getShort(cursor.getColumnIndexOrThrow(CineRdContract
                        .SubtitleEntry._ID));
            } else {
                cv.put(CineRdContract.SubtitleEntry.COLUMN_NAME_NAME, room.getSubtitle());
                subtitleId = (short) ContentUris.parseId(contentResolver.insert(CineRdContract.SubtitleEntry.CONTENT_URI, cv));
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return subtitleId;
    }

    private short persistLanguage(ContentResolver contentResolver, Room room) {
        ContentValues cv;
        short languageId;
        cv = new ContentValues();
        Cursor cursor = null;
        cursor = contentResolver.query(CineRdContract.LanguageEntry.CONTENT_URI, null,
                CineRdContract.LanguageEntry.COLUMN_NAME_NAME + " = ?", new String[]{room.getLanguage()}, null);
        if (cursor != null && cursor.moveToNext()) {
            languageId = cursor.getShort(cursor.getColumnIndexOrThrow(CineRdContract
                    .LanguageEntry._ID));
        } else {
            cv.put(CineRdContract.LanguageEntry.COLUMN_NAME_NAME, room.getLanguage());
            languageId = (short) ContentUris.parseId(contentResolver.insert
                    (CineRdContract.LanguageEntry.CONTENT_URI, cv));
        }
        return languageId;
    }

    private short persistFormat(ContentResolver contentResolver, Room room) {
        ContentValues cv;
        short formatId;
        cv = new ContentValues();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(CineRdContract.FormatEntry.CONTENT_URI, null,
                    CineRdContract.FormatEntry.COLUMN_NAME_NAME + " = ?", new String[]{room.getFormat()}, null);

            cv.put(CineRdContract.FormatEntry.COLUMN_NAME_NAME, room.getFormat());
            if (cursor != null && cursor.moveToNext()) {
                formatId = cursor.getShort(cursor.getColumnIndexOrThrow(CineRdContract
                        .FormatEntry._ID));
            } else {
                formatId = (short) ContentUris.parseId(contentResolver.insert(CineRdContract
                        .FormatEntry.CONTENT_URI, cv));
            }
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return formatId;
    }

    private long persistRoom(ContentResolver contentResolver, Integer theaterId,
                             Room room) {
        long roomId;
        Cursor cursor = null;
        ContentValues cv = new ContentValues();
        try {
            cursor = contentResolver.query(CineRdContract.RoomEntry.CONTENT_URI, null,
                    CineRdContract.RoomEntry.COLUMN_NAME_NUMBER + " = ? AND " + CineRdContract

                            .RoomEntry.COLUMN_NAME_THEATER_ID + " = ?", new String[]{room
                            .getNumber(), theaterId.toString()}, null);
            if (cursor != null && cursor.moveToNext()) {
                roomId = cursor.getLong(cursor.getColumnIndexOrThrow(CineRdContract.RoomEntry._ID));
            } else {
                cv.put(CineRdContract.RoomEntry.COLUMN_NAME_NUMBER, room.getNumber());
                cv.put(CineRdContract.RoomEntry.COLUMN_NAME_THEATER_ID, theaterId);
                roomId = ContentUris.parseId(contentResolver.insert(CineRdContract
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
