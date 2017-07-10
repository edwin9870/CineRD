package com.edwin.android.cinerd;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.data.MovieCollector;
import com.edwin.android.cinerd.data.MovieDataPersistence;
import com.edwin.android.cinerd.data.adapters.AccountGeneral;
import com.edwin.android.cinerd.data.adapters.MoviesSyncAdapter;
import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view_movie_poster)
    RecyclerView recyclerViewMoviePoster;
    @BindView(R.id.floating_button_movie_menu)
    FloatingActionButton floatingButtonMovieMenu;
    @Inject
    MovieDataPersistence mMovieDataPersistence;
    @Inject @Named("MovieCollectorJSON")
    MovieCollector mMovieCollector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerDatabaseComponent.builder().applicationModule(new ApplicationModule(getApplication
                ())).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AccountGeneral.createSyncAccount(this);
        //MoviesSyncAdapter.performSync();

        /*floatingButtonMovieMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Button clicked", Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        List<Movie> movies = mMovieCollector.getMovies();
                        Log.d(TAG, "Movies: " + movies);

                        mMovieDataPersistence.process(movies);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Sync completed", Toast.LENGTH_SHORT)
                                .show();
                    }
                }.execute();*/
            }



}
