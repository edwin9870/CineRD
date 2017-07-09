package com.edwin.android.cinerd.configuration.di;

import com.edwin.android.cinerd.MainActivity;
import com.edwin.android.cinerd.data.MoviesSyncAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */
@Component(modules = {ApplicationModule.class, DatabaseModule.class})
@Singleton
public interface DatabaseComponent {
    void inject(MainActivity activity);
    void inject(MoviesSyncAdapter activity);
}
