package com.edwin.android.cinerd.configuration.di;

import com.edwin.android.cinerd.MainActivity;
import com.edwin.android.cinerd.data.MovieDataPersistence;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */
@Component(modules = {DatabaseModule.class})
public interface DatabaseComponent {
    void inject(MainActivity activity);
    MovieDataPersistence getMovieDataPersistence();
}
