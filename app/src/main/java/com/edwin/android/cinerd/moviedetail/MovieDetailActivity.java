package com.edwin.android.cinerd.moviedetail;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.movieposter.MoviePosterFragment;

import static android.R.attr.fragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieDetailFragment movieDetailFragment = MovieDetailFragment.newInstance();

        fragmentTransaction.add(R.id.fragment_movie_detail, movieDetailFragment);
        fragmentTransaction.commit();
    }
}
