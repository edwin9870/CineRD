package com.edwin.android.cinerd;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edwin.android.cinerd.data.CineRdContract;
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
                        Set<String> format = new HashSet<>();

                        ContentResolver contentResolver = MainActivity.this.getContentResolver();

                        ContentValues cv = new ContentValues();
                        Movie movie = movies.getMovies().get(0);

                        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_NAME, movie.getName());
                        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_DURATION, movie.getDuration());
                        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate().toString());
                        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_SYNOPSIS, movie.getSynopsis());

                        long movieId = ContentUris.parseId(contentResolver.insert(CineRdContract.MovieEntry.CONTENT_URI, cv));
                        Log.d(TAG, "MovieID generated: " + movieId);

                        persistTheater(movieId, contentResolver, movie.getTheaters());
                        persistRating(movieId, contentResolver, movie.getRating());

                        long genreId;
                        for(String genreName: movie.getGenre()) {
                            cv = new ContentValues();
                            cv.put(CineRdContract.GenreEntry.COLUMN_NAME_NAME, genreName);
                            genreId = ContentUris.parseId(contentResolver.insert(CineRdContract.GenreEntry.CONTENT_URI, cv));

                            cv = new ContentValues();
                            cv.put(CineRdContract.MovieGenreEntry.COLUMN_NAME_GENRE_ID, genreId);
                            cv.put(CineRdContract.MovieGenreEntry.COLUMN_NAME_MOVIE_ID, movieId);
                            contentResolver.insert(CineRdContract.MovieGenreEntry.CONTENT_URI, cv);
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

    private void persistRating(long movieId, ContentResolver contentResolver, Rating rating) {
        ContentValues cv;
        cv = new ContentValues();
        cv.put(CineRdContract.RatingEntry.COLUMN_NAME_NAME, "RottenTomatoes");
        long rottenTomatoesId = ContentUris.parseId(contentResolver.insert(CineRdContract.RatingEntry.CONTENT_URI, cv));
        cv.put(CineRdContract.RatingEntry.COLUMN_NAME_NAME, "IMDB");
        long imdbId = ContentUris.parseId(contentResolver.insert(CineRdContract.RatingEntry.CONTENT_URI, cv));

        cv = new ContentValues();
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_MOVIE_ID, movieId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER, rottenTomatoesId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING, rating.getRottentomatoes());
        contentResolver.insert(CineRdContract.MovieRatingEntry.CONTENT_URI, cv);

        cv = new ContentValues();
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_MOVIE_ID, movieId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING_PROVIDER, imdbId);
        cv.put(CineRdContract.MovieRatingEntry.COLUMN_NAME_RATING, rating.getImdb());
        contentResolver.insert(CineRdContract.MovieRatingEntry.CONTENT_URI, cv);
    }

    private void persistTheater(long movieId, ContentResolver contentResolver, List<Theater> theaters) {
        ContentValues cv;
        long theaterId;
        long roomId;
        long subtitleId;
        long formatId;
        long languageId;
        for(Theater theater: theaters) {
            cv = new ContentValues();
            cv.put(CineRdContract.TheaterEntry.COLUMN_NAME_NAME, theater.getName());
            theaterId = ContentUris.parseId(contentResolver.insert(CineRdContract.TheaterEntry.CONTENT_URI, cv));

            for(Room room : theater.getRoom()) {
                cv = new ContentValues();
                cv.put(CineRdContract.RoomEntry.COLUMN_NAME_NUMBER, room.getNumber());
                cv.put(CineRdContract.RoomEntry.COLUMN_NAME_THEATER_ID, theaterId);
                roomId = ContentUris.parseId(contentResolver.insert(CineRdContract.RoomEntry.CONTENT_URI, cv));

                cv = new ContentValues();
                cv.put(CineRdContract.FormatEntry.COLUMN_NAME_NAME, room.getFormat());
                formatId = ContentUris.parseId(contentResolver.insert(CineRdContract.FormatEntry.CONTENT_URI, cv));

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
}
