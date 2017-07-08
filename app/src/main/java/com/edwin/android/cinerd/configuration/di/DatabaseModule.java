package com.edwin.android.cinerd.configuration.di;

import com.edwin.android.cinerd.data.MovieDataPersistence;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */

@Module
public class DatabaseModule {

    //@Provides
    static MovieDataPersistence provideMovieDataPersistence() {
        return new MovieDataPersistence();
    }
}
