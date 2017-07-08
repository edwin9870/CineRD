package com.edwin.android.cinerd;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

                        for(Movie movie : movies.getMovies()) {
                            Log.d(TAG, "Persisting movie: "+ movie);
                            persistMovie(contentResolver, movie);
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

    private void persistMovie(ContentResolver contentResolver, Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_NAME, movie.getName());
        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_DURATION, movie.getDuration());
        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate().toString());
        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_SYNOPSIS, movie.getSynopsis());

        long movieId = ContentUris.parseId(contentResolver.insert(CineRdContract.MovieEntry.CONTENT_URI, cv));
        Log.d(TAG, "MovieID generated: " + movieId);

        persistTheater(movieId, contentResolver, movie.getTheaters());
        persistRating(movieId, contentResolver, movie.getRating());

        long genreId;
        Cursor cursor = null;
        for(String genreName: movie.getGenre()) {
            try {
                cursor = contentResolver.query(GenreEntry.CONTENT_URI, null,
                        GenreEntry.COLUMN_NAME_NAME + " = ?", new String[]{genreName}, null);
                if (cursor != null && cursor.moveToNext()) {
                    genreId = cursor.getLong(cursor.getColumnIndexOrThrow(GenreEntry._ID));
                    Log.d(TAG, "genreId from table: "+ genreId);
                } else {
                    cv = new ContentValues();
                    cv.put(GenreEntry.COLUMN_NAME_NAME, genreName);
                    genreId = ContentUris.parseId(contentResolver.insert(GenreEntry.CONTENT_URI, cv));
                }

                cv = new ContentValues();
                cv.put(CineRdContract.MovieGenreEntry.COLUMN_NAME_GENRE_ID, genreId);
                cv.put(CineRdContract.MovieGenreEntry.COLUMN_NAME_MOVIE_ID, movieId);
                contentResolver.insert(CineRdContract.MovieGenreEntry.CONTENT_URI, cv);
            } finally {
                if(cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    private void persistRating(long movieId, ContentResolver contentResolver, Rating rating) {

        short rottenTomatoesRatingId = getRatingId(contentResolver, ROTTEN_TOMATOES);
        short imdbRattingId = getRatingId(contentResolver, IMDB);

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

    private short getRatingId(ContentResolver contentResolver, String ratingProvider) {
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

    private void persistTheater(long movieId, ContentResolver contentResolver, List<Theater> theaters) {
        ContentValues cv;
        Long theaterId;
        long roomId;
        long subtitleId;
        short formatId;
        long languageId;
        for(Theater theater: theaters) {
            cv = new ContentValues();
            cv.put(CineRdContract.TheaterEntry.COLUMN_NAME_NAME, theater.getName());
            theaterId = ContentUris.parseId(contentResolver.insert(CineRdContract.TheaterEntry.CONTENT_URI, cv));

            for(Room room : theater.getRoom()) {
                roomId = getRoomId(contentResolver, theaterId, room);

                formatId = getFormatId(contentResolver, room);

                cv = new ContentValues();
                cv.put(CineRdContract.LanguageEntry.COLUMN_NAME_NAME, room.getLanguage());
                languageId = ContentUris.parseId(contentResolver.insert(CineRdContract.LanguageEntry.CONTENT_URI, cv));

                if(room.getSubtitle() != null && !room.getSubtitle().isEmpty()) {
                    cv = new ContentValues();
                    cv.put(CineRdContract.SubtitleEntry.COLUMN_NAME_NAME, room.getLanguage());
                    subtitleId = ContentUris.parseId(contentResolver.insert(CineRdContract.SubtitleEntry.CONTENT_URI, cv));
                } else {
                    subtitleId = 0;
                }

                cv = new ContentValues();
                cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_MOVIE_ID, movieId);
                cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_THEATER_ID, theaterId);
                cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_ROOM_ID, roomId);
                cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_AVAILABLE_DATE, room.getDate().toString());
                if(subtitleId > 0) {
                    cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_SUBTITLE_ID, subtitleId);
                }
                cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_FORMAT_ID, formatId);
                cv.put(CineRdContract.MovieTheaterDetailEntry.COLUMN_NAME_LANGUAGE_ID, languageId);
                contentResolver.insert(CineRdContract.MovieTheaterDetailEntry.CONTENT_URI, cv);

            }
        }
    }

    private short getFormatId(ContentResolver contentResolver, Room room) {
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

    private long getRoomId(ContentResolver contentResolver, Long theaterId,
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
