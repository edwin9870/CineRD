package com.edwin.android.cinerd;

import android.content.ContentResolver;
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
import com.edwin.android.cinerd.entity.Movies;
import com.edwin.android.cinerd.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

                        ContentResolver contentResolver = MainActivity.this.getContentResolver();

                        ContentValues cv = new ContentValues();
                        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_NAME, movies.getMovies().get(0).getName());
                        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_DURATION, movies.getMovies().get(0).getDuration());
                        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_RELEASE_DATE, movies.getMovies().get(0).getReleaseDate().toString());
                        cv.put(CineRdContract.MovieEntry.COLUMN_NAME_SYNOPSIS, movies.getMovies().get(0).getSynopsis());

                        contentResolver.insert(CineRdContract.MovieEntry.CONTENT_URI, cv);

                        cv = new

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
