package com.edwin.android.cinerd.data;

import android.content.Context;
import android.util.Log;

import com.edwin.android.cinerd.BuildConfig;
import com.edwin.android.cinerd.data.rest.MovieService;
import com.edwin.android.cinerd.data.rest.MovieServiceAppClient;
import com.edwin.android.cinerd.entity.json.Movie;
import com.edwin.android.cinerd.entity.json.Movies;
import com.edwin.android.cinerd.util.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 7/9/2017.
 */

@Singleton
public class MovieCollectorJSON implements MovieCollector {

    public static final String TAG = MovieCollectorJSON.class.getSimpleName();
    Context mContext;

    @Inject
    public MovieCollectorJSON(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<Movie> getMovies(boolean lightVersion) {
        try {
            MovieService movieService = MovieServiceAppClient.getClient().create(MovieService
                    .class);
            Movies movies;
            Log.d(TAG, "Downloading data. is lightVersion: " + lightVersion);
            if(lightVersion) {
                movies = movieService.getMovie(BuildConfig.MOVIE_JSON_LIGHT_VERSION).execute().body();
            } else {
                movies = movieService.getMovie(BuildConfig.MOVIE_JSON_FULL_VERSION).execute().body();
            }
            Log.d(TAG, "Data downloaded");
            return movies.getMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
