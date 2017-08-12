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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;

/**
 * Created by Edwin Ramirez Ventura on 8/9/2017.
 */

public class MovieSyncLoaderCallback implements LoaderManager.LoaderCallbacks<Void> {

    public static final String TAG = MovieSyncLoaderCallback.class.getSimpleName();
    private Context mContext;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFloatingButtonMovieMenu;
    private MoviePosterActivity moviePosterActivity;
    private DatabaseComponent mDatabaseComponent;

    public MovieSyncLoaderCallback(Context mContext, ProgressBar mProgressBar,
                                   FloatingActionButton mFloatingButtonMovieMenu, MoviePosterActivity moviePosterActivity, DatabaseComponent mDatabaseComponent) {
        this.mContext = mContext;
        this.mProgressBar = mProgressBar;
        this.mFloatingButtonMovieMenu = mFloatingButtonMovieMenu;
        this.moviePosterActivity = moviePosterActivity;
        this.mDatabaseComponent = mDatabaseComponent;
    }


    @Override
    public Loader<Void> onCreateLoader(int i, Bundle bundle) {
        mFloatingButtonMovieMenu.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        String toastMessage = mContext.getString(R.string.loading_movies);
        Log.d(TAG, "toastMessage to show: " + toastMessage);
        Toast.makeText(mContext, toastMessage, Toast.LENGTH_LONG).show();

        return new MovieSyncLoader(mContext);
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void aVoid) {
        Log.d(TAG, "Finish manual sync");
        final int WHAT = 1;
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == WHAT) {
                    moviePosterActivity.addFragment(mDatabaseComponent);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mFloatingButtonMovieMenu.setVisibility(View.VISIBLE);
                };
            }
        };
        handler.sendEmptyMessage(WHAT);
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }
}
