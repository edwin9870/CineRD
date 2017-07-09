package com.edwin.android.cinerd.configuration.di;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

import com.edwin.android.cinerd.data.MovieDataPersistence;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */

@Module
public class DatabaseModule {


    @Provides
    @Singleton
    MovieDataPersistence provideMovieDataPersistence(ContentResolver contentResolver) {
        return new MovieDataPersistence(contentResolver);
    }

    @Provides @Singleton
    ContentResolver provideContentResolver(@Named("application context") Context applicationContext) {
        return applicationContext.getContentResolver();
    };
}
