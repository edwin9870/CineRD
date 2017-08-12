package com.edwin.android.cinerd.moviefinder;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;
import com.edwin.android.cinerd.moviedetail.MovieDetailFragment;

import javax.inject.Inject;

public class MovieFinderActivity extends AppCompatActivity {

    @Inject
    MovieFinderPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_finder);

        MovieFinderFragment movieFinderFragment = (MovieFinderFragment) getFragmentManager().findFragmentById(R.id.fragment_movie_finder);

        if(movieFinderFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            movieFinderFragment = MovieFinderFragment.newInstance();

            fragmentTransaction.add(R.id.fragment_movie_finder, movieFinderFragment);
            fragmentTransaction.commit();
        }

        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().applicationModule(new
                ApplicationModule(getApplication())).build();
        DaggerMovieFinderComponent.builder().databaseComponent(databaseComponent)
                .movieFinderPresenterModule(new MovieFinderPresenterModule(movieFinderFragment)).build().inject(this);

    }
}
