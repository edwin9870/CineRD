package com.edwin.android.cinerd.movieposter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.data.MovieCollector;
import com.edwin.android.cinerd.data.MovieCollectorJSON;
import com.edwin.android.cinerd.data.ProcessMovies;
import com.edwin.android.cinerd.entity.json.Movie;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Edwin Ramirez Ventura on 8/9/2017.
 */

public class MovieSyncLoader extends AsyncTaskLoader<Void> {

    public static final String TAG = MovieSyncLoader.class.getSimpleName();
    private final Context mContext;
    private boolean loaded;
    @Inject
    ProcessMovies mMovieDataRepository;
    @Inject @Named("MovieCollectorJSON")
    MovieCollector mMovieCollector;

    public MovieSyncLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if(loaded) {
            deliverResult(null);
        } else {
            forceLoad();
        }
    }

    @Override
    public Void loadInBackground() {
        Log.d(TAG, "Starting to load data");
        DaggerDatabaseComponent.builder().applicationModule(new ApplicationModule(mContext
                .getApplicationContext())).build().inject(this);
        List<Movie> movies = mMovieCollector.getMovies();
        Log.d(TAG, "mMovieCollector.getMoviesCollector(): " + movies);
        mMovieDataRepository.process(movies);
        return null;
    }

    @Override
    public void deliverResult(Void data) {
        loaded = true;
        super.deliverResult(data);
    }

}
