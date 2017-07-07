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

                        persistTheater(contentResolver, movie.getTheaters());
                        persitRating(contentResolver);

                        for(String genreName: movie.getGenre()) {
                            cv = new ContentValues();
                            cv.put(CineRdContract.GenreEntry.COLUMN_NAME_NAME, genreName);
                            contentResolver.insert(CineRdContract.GenreEntry.CONTENT_URI, cv);
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

    private void persitRating(ContentResolver contentResolver) {
        ContentValues cv;
        cv = new ContentValues();
        cv.put(CineRdContract.RatingEntry.COLUMN_NAME_NAME, "RottenTomatoes");
        contentResolver.insert(CineRdContract.RatingEntry.CONTENT_URI, cv);
        cv.put(CineRdContract.RatingEntry.COLUMN_NAME_NAME, "IMDB");
        contentResolver.insert(CineRdContract.RatingEntry.CONTENT_URI, cv);
    }

    private void persistTheater(ContentResolver contentResolver, List<Theater> theaters) {
        ContentValues cv;
        long theaterId;
        for(Theater theater: theaters) {
            cv = new ContentValues();
            cv.put(CineRdContract.TheaterEntry.COLUMN_NAME_NAME, theater.getName());
            theaterId = ContentUris.parseId(contentResolver.insert(CineRdContract.TheaterEntry.CONTENT_URI, cv));

            for(Room room : theater.getRoom()) {
                cv = new ContentValues();
                cv.put(CineRdContract.RoomEntry.COLUMN_NAME_NUMBER, room.getNumber());
                cv.put(CineRdContract.RoomEntry.COLUMN_NAME_THEATER_ID, theaterId);
                contentResolver.insert(CineRdContract.RoomEntry.CONTENT_URI, cv);

                cv = new ContentValues();
                cv.put(CineRdContract.FormatEntry.COLUMN_NAME_NAME, room.getFormat());
                contentResolver.insert(CineRdContract.FormatEntry.CONTENT_URI, cv);
            }
        }
    }
}
