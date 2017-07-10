package com.edwin.android.cinerd.configuration.di;

import com.edwin.android.cinerd.movieposter.MoviePosterActivity;
import com.edwin.android.cinerd.data.adapters.MovieSyncAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */
@Component(modules = {ApplicationModule.class, DatabaseModule.class, MovieModule.class})
@Singleton
public interface DatabaseComponent {
    void inject(MoviePosterActivity activity);
    void inject(MovieSyncAdapter activity);
}
