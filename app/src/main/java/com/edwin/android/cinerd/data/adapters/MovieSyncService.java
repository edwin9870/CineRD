package com.edwin.android.cinerd.data.adapters;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Edwin Ramirez Ventura on 7/9/2017.
 */

public class MovieSyncService extends Service {

    private static MoviesSyncAdapter sMoviesSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sMoviesSyncAdapter == null) {
                sMoviesSyncAdapter = new MoviesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sMoviesSyncAdapter.getSyncAdapterBinder();
    }
}
