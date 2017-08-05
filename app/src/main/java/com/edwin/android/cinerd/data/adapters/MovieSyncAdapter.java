package com.edwin.android.cinerd.data.adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.data.MovieCollector;
import com.edwin.android.cinerd.data.repositories.MovieDataRepository;
import com.edwin.android.cinerd.entity.json.Movie;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Edwin Ramirez Ventura on 7/6/2017.
 */

public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = MovieSyncAdapter.class.getSimpleName();
    @Inject
    MovieDataRepository mMovieDataRepository;
    @Inject @Named("MovieCollectorJSON")
    MovieCollector mMovieCollector;

    private Context mContext;


    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.mContext = context;
    }

    public MovieSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient
            contentProviderClient, SyncResult syncResult) {
        Log.d(TAG, "Running movie Sync adapter");
        DaggerDatabaseComponent.builder().applicationModule(new ApplicationModule(mContext
                .getApplicationContext())).build().inject(this);

        List<Movie> movies = mMovieCollector.getMovies();
        Log.d(TAG, "mMovieCollector.getMoviesCollector(): " + movies);
        mMovieDataRepository.process(movies);

    }

    public static void performSync() {
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(AccountGeneral.getAccount(),
                "com.edwin.android.cinerd", b);
    }
}
