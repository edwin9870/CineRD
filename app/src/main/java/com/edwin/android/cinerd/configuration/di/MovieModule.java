package com.edwin.android.cinerd.configuration.di;

import android.content.Context;

import com.edwin.android.cinerd.data.MovieCollector;
import com.edwin.android.cinerd.data.MovieCollectorJSON;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 7/9/2017.
 */

@Module
public class MovieModule {

    @Provides
    @Singleton
    @Named("MovieCollectorJSON")
    MovieCollector provideMovieCollectorJson(Context applicationContext) {
        return new MovieCollectorJSON(applicationContext);
    }
}
