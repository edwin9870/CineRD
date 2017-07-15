package com.edwin.android.cinerd.moviedetail;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;

import javax.inject.Inject;

public class MovieDetailActivity extends AppCompatActivity {

    @Inject
    MovieDetailPresenter moviePosterPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_movie_detail, movieDetailFragment);
        fragmentTransaction.commit();

        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().applicationModule(new
                ApplicationModule(getApplication())).build();
        DaggerMovieDetailComponent.builder().databaseComponent(databaseComponent)
                .movieDetailPresenterModule(new MovieDetailPresenterModule(movieDetailFragment)).build().inject(this);
    }
}
