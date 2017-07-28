package com.edwin.android.cinerd.moviefinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;

import javax.inject.Inject;

public class MovieFinderActivity extends AppCompatActivity {

    @Inject
    MovieFinderPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_finder);

        MovieFinderFragment movieFinderFragment = MovieFinderFragment.newInstance();

        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().applicationModule(new
                ApplicationModule(getApplication())).build();
        DaggerMovieFinderComponent.builder().databaseComponent(databaseComponent)
                .movieFinderPresenterModule(new MovieFinderPresenterModule(movieFinderFragment)).build().inject(this);
    }
}
