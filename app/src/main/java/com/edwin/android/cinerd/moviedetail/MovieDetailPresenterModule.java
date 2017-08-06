package com.edwin.android.cinerd.moviedetail;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 7/14/2017.
 */

@Module
public class MovieDetailPresenterModule {

    private final MovieDetailMVP.View mView;

    public MovieDetailPresenterModule(MovieDetailMVP.View mView) {
        this.mView = mView;
    }

    @Provides
    MovieDetailMVP.View provideMovieDetailView() {
        return mView;
    }
}
