package com.edwin.android.cinerd.movieposter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.edwin.android.cinerd.data.MovieCollectorJSON;
import com.edwin.android.cinerd.data.ProcessMovies;
import com.edwin.android.cinerd.entity.json.Movie;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 8/9/2017.
 */

public class MovieSyncLoader extends AsyncTaskLoader<Void> {

    public static final String TAG = MovieSyncLoader.class.getSimpleName();
    private final Context mContext;
    private boolean loaded;

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
        MovieCollectorJSON collector = new MovieCollectorJSON(mContext);
        List<Movie> movies = collector.getMovies();
        Log.d(TAG, "mMovieCollector.getMoviesCollector(): " + movies);
        ProcessMovies processMovies = new ProcessMovies(mContext, collector);
        processMovies.process(movies);
        return null;
    }

    @Override
    public void deliverResult(Void data) {
        loaded = true;
        super.deliverResult(data);
    }

}
