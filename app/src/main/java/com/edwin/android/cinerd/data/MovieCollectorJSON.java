package com.edwin.android.cinerd.data;

import android.content.Context;
import android.util.Log;

import com.edwin.android.cinerd.BuildConfig;
import com.edwin.android.cinerd.entity.json.Movie;
import com.edwin.android.cinerd.entity.json.Movies;
import com.edwin.android.cinerd.util.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
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
            String movieData = NetworkUtil.getMovieData(lightVersion);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
            Movies movies = gson.fromJson(movieData, Movies.class);
            return movies.getMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
