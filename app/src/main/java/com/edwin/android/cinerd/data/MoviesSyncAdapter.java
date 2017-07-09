package com.edwin.android.cinerd.data;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 7/6/2017.
 */

public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {
    @Inject
    MovieDataPersistence mMovieDataPersistence;
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

    }
}
