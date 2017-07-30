package com.edwin.android.cinerd.theater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;

import javax.inject.Inject;

public class TheaterActivity extends AppCompatActivity {

    public static final String TAG = TheaterActivity.class.getSimpleName();
    @Inject
    TheaterPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);

        TheaterFragment theaterFragment = TheaterFragment.newInstance();

        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().applicationModule(new
                ApplicationModule(getApplication())).build();
        DaggerTheaterComponent.builder().databaseComponent(databaseComponent)
                .theaterPresenterModule(new TheaterPresenterModule(theaterFragment)).build().inject(this);

    }
}
