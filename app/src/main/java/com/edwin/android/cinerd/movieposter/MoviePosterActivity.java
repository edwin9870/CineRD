package com.edwin.android.cinerd.movieposter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;
import com.edwin.android.cinerd.data.CineRdDbHelper;
import com.edwin.android.cinerd.data.adapters.AccountGeneral;
import com.edwin.android.cinerd.util.DatabaseUtil;
import com.edwin.android.cinerd.util.NetworkUtil;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviePosterActivity extends AppCompatActivity implements ManualSyncServiceReceiver.Receiver {

    public static final String TAG = MoviePosterActivity.class.getSimpleName();
    public static final String BUNDLE_STATUS_RECEIVER = "BUNDLE_STATUS_RECEIVER";
    public static final String BUNDLE_RECEIVER = "BUNDLE_RECEIVER";
    @BindView(R.id.floating_button_movie_menu)
    FloatingActionButton mFloatingButtonMovieMenu;
    @Inject
    MoviePosterPresenter moviePosterPresenter;
    @BindView(R.id.menu_fab_filter)
    FABRevealMenu mFabFilterMenu;
    @BindView(R.id.progress_bar_loading_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.fragment_movie_poster)
    FrameLayout mMoviePosterViewFragment;
    private DatabaseComponent mDatabaseComponent;
    private int mResultCode;
    private ManualSyncServiceReceiver mResultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);
        ButterKnife.bind(this);

        if(savedInstanceState == null) {
            mResultCode = ManualSyncService.STATUS_FINISHED;
        } else {
            mResultCode = savedInstanceState.getInt(BUNDLE_STATUS_RECEIVER);
            Log.d(TAG, "mResultlcode: " + mResultCode);
            mResultReceiver = savedInstanceState.getParcelable(BUNDLE_RECEIVER);
        }

        if(mResultReceiver != null) {
            Log.d(TAG, "setting result receiver");
            mResultReceiver.setReceiver(this);
            if(mResultCode == ManualSyncService.STATUS_RUNNING) {
                hideContent();
            }
        }

        Log.d(TAG, "Executing onCreate");
        try {
            if (mFloatingButtonMovieMenu != null && mFabFilterMenu != null) {
                mFabFilterMenu.bindAncherView(mFloatingButtonMovieMenu);
                mFabFilterMenu.setOnFABMenuSelectedListener(new OnFABMenuSelectedListener() {

                    @Override
                    public void onMenuItemSelected(View view) {
                        int id = (int) view.getTag();
                        switch (id) {
                            case R.id.item_action_movie:
                                moviePosterPresenter.openMovieFilterActivity();
                                break;
                            default:
                                moviePosterPresenter.openTheatersActivity();
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDatabaseComponent = DaggerDatabaseComponent.builder()
                .applicationModule
                        (new ApplicationModule(getApplication())).build();

        if (!DatabaseUtil.existDatabase(this, CineRdDbHelper.DATABASE_NAME)) {
            if (!NetworkUtil.isOnline(this)) {
                Toast.makeText(this, R.string.no_internet_connection_to_load_movie_information,
                        Toast.LENGTH_LONG).show();
                return;
            }

            Log.d(TAG, "Database doesn't exist. Starting to data");
            hideContent();
            mResultReceiver = new ManualSyncServiceReceiver(new Handler());
            mResultReceiver.setReceiver(this);
            Intent intent = new Intent(Intent.ACTION_SYNC,
                                        null,
                                        this,
                                        ManualSyncService.class);
            intent.putExtra(ManualSyncService.EXTRA_RECEIVER, mResultReceiver);
            intent.putExtra(ManualSyncService.EXTRA_LIGHT_VERSION, true);
            startService(intent);
        } else {
            addFragment(mDatabaseComponent);
        }

        AccountGeneral.createSyncAccount(this);
    }

    private void hideContent() {
        mFloatingButtonMovieMenu.setVisibility(View.INVISIBLE);
        mMoviePosterViewFragment.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        String toastMessage = this.getString(R.string.loading_movies);
        Log.d(TAG, "toastMessage to show: " + toastMessage);
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_poster, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_refresh_action:
                Log.d(TAG, "refresh menu clicked");

                if (!NetworkUtil.isOnline(this)) {
                    Toast.makeText(this, R.string.no_internet_connection_to_update_movie_information,
                            Toast.LENGTH_LONG).show();
                    break;
                }

                if(mResultCode == ManualSyncService.STATUS_RUNNING) {
                    Log.d(TAG, "Service is previously running");
                    break;
                }

                hideContent();
                mResultReceiver = new ManualSyncServiceReceiver(new Handler());
                mResultReceiver.setReceiver(this);
                Intent intent = new Intent(Intent.ACTION_SYNC,
                        null,
                        this,
                        ManualSyncService.class);
                intent.putExtra(ManualSyncService.EXTRA_RECEIVER, mResultReceiver);
                intent.putExtra(ManualSyncService.EXTRA_LIGHT_VERSION, false);
                startService(intent);
                Log.d(TAG, "Refresh button clicked, start manual sync");
                break;
            case R.id.item_settings_action:
                    moviePosterPresenter.settingMenuClicked();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Log.d(TAG, "resultCode: " + resultCode);
        mResultCode = resultCode;
        switch (mResultCode) {
            case ManualSyncService.STATUS_RUNNING:
                Log.d(TAG, "Service is running");
                break;
            case ManualSyncService.STATUS_FINISHED:
                Log.d(TAG, "Service finish to running");
                this.addFragment(mDatabaseComponent);
                mProgressBar.setVisibility(View.INVISIBLE);
                mFloatingButtonMovieMenu.setVisibility(View.VISIBLE);
                mMoviePosterViewFragment.setVisibility(View.VISIBLE);
                break;
            case ManualSyncService.STATUS_ERROR:
                Log.d(TAG, "Error running the service");
                break;
            default:
                Log.d(TAG, "Unknown result code");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATUS_RECEIVER, mResultCode);
        outState.putParcelable(BUNDLE_RECEIVER, mResultReceiver);
        super.onSaveInstanceState(outState);
    }

    public void addFragment(DatabaseComponent databaseComponent) {
        MoviePosterFragment fragment = (MoviePosterFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_movie_poster);

        Log.d(TAG, "Adding: " + fragment);
        if (fragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = MoviePosterFragment.newInstance();
            fragmentTransaction.add(R.id.fragment_movie_poster, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }

        Log.d(TAG, "Injection movie poster presenter");
        DaggerMoviePosterComponent.builder().databaseComponent(databaseComponent)
                .moviePosterPresenterModule(new MoviePosterPresenterModule(fragment)).build()
                .inject(this);
    }

}
