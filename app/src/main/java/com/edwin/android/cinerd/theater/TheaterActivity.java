package com.edwin.android.cinerd.theater;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;

import javax.inject.Inject;

public class TheaterActivity extends AppCompatActivity {

    public static final String TAG = TheaterActivity.class.getSimpleName();
    public static final String BUNDLE_THEATER_ID = "BUNDLE_THEATER_ID";
    @Inject
    TheaterPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);

        //int theaterId = getIntent().getExtras().getInt(BUNDLE_THEATER_ID, 0);
        TheaterFragment theaterFragment = TheaterFragment.newInstance(14);

        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().applicationModule(new
                ApplicationModule(getApplication())).build();
        DaggerTheaterComponent.builder().databaseComponent(databaseComponent)
                .theaterPresenterModule(new TheaterPresenterModule(theaterFragment)).build().inject(this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_theater, theaterFragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Log.d(TAG, "Navigating up");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
