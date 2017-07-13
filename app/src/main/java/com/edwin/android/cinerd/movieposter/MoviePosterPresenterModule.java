package com.edwin.android.cinerd.movieposter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 7/12/2017.
 */

@Module
public class MoviePosterPresenterModule {

    private final MoviePosterMVP.View mView;

    public MoviePosterPresenterModule(MoviePosterMVP.View mView) {
        this.mView = mView;
    }

    @Provides
    MoviePosterMVP.View provideMoviePosterView() {
        return mView;
    }
}
