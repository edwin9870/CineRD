package com.edwin.android.cinerd.movieposter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;

/**
 * Created by Edwin Ramirez Ventura on 8/9/2017.
 */

public class MovieSyncLoaderCallback implements LoaderManager.LoaderCallbacks<Void> {

    public static final String TAG = MovieSyncLoaderCallback.class.getSimpleName();
    public static final String BUNDLE_RESET_FRAGMENT = "BUNDLE_RESET_FRAGMENT";
    private final Boolean mLightVersion;
    private final FrameLayout mMoviePosterViewFragment;
    private Context mContext;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFloatingButtonMovieMenu;
    private MoviePosterActivity moviePosterActivity;
    private DatabaseComponent mDatabaseComponent;
    private boolean resetFragment;

    public MovieSyncLoaderCallback(Context mContext, FrameLayout mMoviePosterViewFragment, ProgressBar mProgressBar,
                                   FloatingActionButton mFloatingButtonMovieMenu,
                                   MoviePosterActivity moviePosterActivity,
                                   DatabaseComponent mDatabaseComponent,
                                   Boolean lightVersion) {
        this.mContext = mContext;
        this.mMoviePosterViewFragment = mMoviePosterViewFragment;
        this.mProgressBar = mProgressBar;
        this.mFloatingButtonMovieMenu = mFloatingButtonMovieMenu;
        this.moviePosterActivity = moviePosterActivity;
        this.mDatabaseComponent = mDatabaseComponent;
        mLightVersion = lightVersion;
    }


    @Override
    public Loader<Void> onCreateLoader(int i, Bundle bundle) {
        mFloatingButtonMovieMenu.setVisibility(View.INVISIBLE);
        mMoviePosterViewFragment.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        String toastMessage = mContext.getString(R.string.loading_movies);
        Log.d(TAG, "toastMessage to show: " + toastMessage);
        Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();
        if(bundle != null) {
            resetFragment = bundle.getBoolean(BUNDLE_RESET_FRAGMENT, false);
        } else {
            resetFragment = false;
        }

        if(mLightVersion == null) {
            return new MovieSyncLoader(mContext);
        } else {
            return new MovieSyncLoader(mContext, mLightVersion);
        }
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void aVoid) {
        Log.d(TAG, "Finish manual sync");
        final int WHAT = 1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == WHAT) {

                    if(resetFragment) {
                        moviePosterActivity.resetFragment();
                    }else {
                        moviePosterActivity.addFragment(mDatabaseComponent);
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mFloatingButtonMovieMenu.setVisibility(View.VISIBLE);
                    mMoviePosterViewFragment.setVisibility(View.VISIBLE);


                };
            }
        };
        handler.sendEmptyMessage(WHAT);

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }
}
