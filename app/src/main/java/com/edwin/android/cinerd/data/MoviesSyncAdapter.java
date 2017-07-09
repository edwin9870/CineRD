package com.edwin.android.cinerd.data;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Edwin Ramirez Ventura on 7/6/2017.
 */

public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = MoviesSyncAdapter.class.getSimpleName();
    @Inject
    MovieDataPersistence mMovieDataPersistence;
    @Inject @Named("MovieCollectorJSON")
    MovieCollector mMovieCollector;

    private Context mContext;


    public MoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.mContext = context;
    }

    public MoviesSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient
            contentProviderClient, SyncResult syncResult) {
        DaggerDatabaseComponent.builder().applicationModule(new ApplicationModule(mContext
                .getApplicationContext())).build().inject(this);

        Log.d(TAG, "mMovieCollector.getMovies(): " + mMovieCollector.getMovies());

    }
}
