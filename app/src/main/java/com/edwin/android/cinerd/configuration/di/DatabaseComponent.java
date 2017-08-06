package com.edwin.android.cinerd.configuration.di;

import com.edwin.android.cinerd.data.repositories.FormatRepository;
import com.edwin.android.cinerd.data.repositories.MovieDataRepository;
import com.edwin.android.cinerd.data.repositories.MovieTheaterDetailRepository;
import com.edwin.android.cinerd.data.repositories.RatingRepository;
import com.edwin.android.cinerd.data.repositories.TheaterRepository;
import com.edwin.android.cinerd.data.adapters.MovieSyncAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */
@Component(modules = {ApplicationModule.class, DatabaseModule.class, MovieModule.class})
@Singleton
public interface DatabaseComponent {
    void inject(MovieSyncAdapter activity);
    MovieDataRepository getMetadataRepository();
    TheaterRepository getTheaterRepository();
    MovieTheaterDetailRepository getMovieTheaterDetailRepository();
    RatingRepository getRatingRepository();
    FormatRepository getFormatRepository();
}
