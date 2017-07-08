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

import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.data.CineRdContract;
import com.edwin.android.cinerd.data.CineRdContract.GenreEntry;
import com.edwin.android.cinerd.data.MovieDataPersistence;
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
    @BindView(R.id.recycler_view_movie_poster)
    RecyclerView recyclerViewMoviePoster;
    @BindView(R.id.floating_button_movie_menu)
    FloatingActionButton floatingButtonMovieMenu;
    MovieDataPersistence mMovieDataPersistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mMovieDataPersistence = DaggerDatabaseComponent.builder().build().getMovieDataPersistence();
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
                        mMovieDataPersistence.process(contentResolver, movies.getMovies());

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
}
