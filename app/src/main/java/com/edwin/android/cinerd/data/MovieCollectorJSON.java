package com.edwin.android.cinerd.data;

import android.content.Context;

import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Movies;
import com.edwin.android.cinerd.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by Edwin Ramirez Ventura on 7/9/2017.
 */

@Singleton
public class MovieCollectorJSON implements MovieCollector {

    Context mContext;

    @Inject
    public MovieCollectorJSON(@Named("application context") Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<Movie> getMovies() {
        String jsonData = JsonUtil.loadJSONFromAsset(mContext, "data.json");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        Movies movies = gson.fromJson(jsonData, Movies.class);
        return movies.getMovies();
    }
}
