package com.edwin.android.cinerd.theater;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 7/30/2017.
 */

@Module
public class TheaterPresenterModule {

    private TheaterMVP.View mView;

    public TheaterPresenterModule(TheaterMVP.View view) {
        this.mView = view;
    }

    @Provides
    TheaterMVP.View provideTheaterView() {
        return mView;
    }
}
