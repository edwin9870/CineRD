package com.edwin.android.cinerd.theater;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TheaterActivity extends AppCompatActivity {

    public static final String TAG = TheaterActivity.class.getSimpleName();
    @Inject
    TheaterPresenter mPresenter;
    @BindView(R.id.floating_button_theater_menu)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);
        ButterKnife.bind(this);

        TheaterFragment theaterFragment = TheaterFragment.newInstance();

        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().applicationModule
                (new
                ApplicationModule(getApplication())).build();
        DaggerTheaterComponent.builder().databaseComponent(databaseComponent)
                .theaterPresenterModule(new TheaterPresenterModule(theaterFragment)).build()
                .inject(this);

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

    @OnClick(R.id.floating_button_theater_menu)
    public void onViewClicked() {
        mPresenter.onClickFabButton();
    }
}
