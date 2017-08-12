package com.edwin.android.cinerd.movieposter;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.data.MovieCollector;
import com.edwin.android.cinerd.data.ProcessMovies;
import com.edwin.android.cinerd.entity.json.Movie;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Edwin Ramirez Ventura on 8/12/2017.
 */

public class ManualSyncService extends IntentService {

    public static final String TAG = ManualSyncService.class.getSimpleName();
    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_LIGHT_VERSION = "EXTRA_LIGHT_VERSION";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    @Inject
    ProcessMovies mMovieDataRepository;
    @Inject @Named("MovieCollectorJSON")
    MovieCollector mMovieCollector;

    public ManualSyncService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service started");
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        boolean loadLightVersion = intent.getExtras().getBoolean(EXTRA_LIGHT_VERSION, true);
        Log.d(TAG, "loadLightVersion: " + loadLightVersion);

        DaggerDatabaseComponent.builder().applicationModule(new ApplicationModule
                (getApplicationContext())).build().inject(this);

        receiver.send(STATUS_RUNNING, Bundle.EMPTY);
        List<Movie>  movies = mMovieCollector.getMovies(loadLightVersion);
        mMovieDataRepository.process(movies);
        receiver.send(STATUS_FINISHED, Bundle.EMPTY);
    }
}
