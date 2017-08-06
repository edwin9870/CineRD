package com.edwin.android.cinerd.configuration.di;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */
@Module
public class ApplicationModule {
    private Context mApplicationContext;

    public ApplicationModule(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    @Provides
    @Singleton
    Context providesApplication() {
        return mApplicationContext;
    }
}
