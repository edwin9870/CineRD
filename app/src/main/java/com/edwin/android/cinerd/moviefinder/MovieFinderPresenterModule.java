package com.edwin.android.cinerd.moviefinder;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 7/27/2017.
 */

@Module
public class MovieFinderPresenterModule {

    private MovieFinderMVP.View mView;

    public MovieFinderPresenterModule(MovieFinderMVP.View view) {
        this.mView = view;
    }

    @Provides
    MovieFinderMVP.View provideMovieFinderView() {
        return mView;
    }
}
