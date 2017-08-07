package com.edwin.android.cinerd.moviedetail;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;

import javax.inject.Inject;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String BUNDLE_MOVIE_ID = "BUNDLE_MOVIE_ID";
    @Inject
    MovieDetailPresenter moviePosterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        long movieId = getIntent().getExtras().getLong(BUNDLE_MOVIE_ID, 0);

        Log.d(TAG, "movieId: " + movieId);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance(movieId);

        fragmentTransaction.add(R.id.fragment_movie_detail, movieDetailFragment);
        fragmentTransaction.commit();

        Log.d(TAG, "Injection movie poster presenter");
        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().applicationModule
                (new
                ApplicationModule(getApplication())).build();
        DaggerMovieDetailComponent.builder().databaseComponent(databaseComponent)
                .movieDetailPresenterModule(new MovieDetailPresenterModule(movieDetailFragment))
                .build().inject(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
