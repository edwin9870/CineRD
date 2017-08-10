package com.edwin.android.cinerd.configuration.di;

import com.edwin.android.cinerd.data.repositories.FormatRepository;
import com.edwin.android.cinerd.data.repositories.GenreRepository;
import com.edwin.android.cinerd.data.ProcessMovies;
import com.edwin.android.cinerd.data.repositories.MovieRepository;
import com.edwin.android.cinerd.data.repositories.MovieTheaterDetailRepository;
import com.edwin.android.cinerd.data.repositories.RatingRepository;
import com.edwin.android.cinerd.data.repositories.TheaterRepository;
import com.edwin.android.cinerd.data.adapters.MovieSyncAdapter;
import com.edwin.android.cinerd.movieposter.MovieSyncLoader;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/8/2017.
 */
@Component(modules = {ApplicationModule.class, DatabaseModule.class, MovieModule.class})
@Singleton
public interface DatabaseComponent {
    void inject(MovieSyncAdapter activity);
    void inject(MovieSyncLoader activity);
    ProcessMovies getMetadataRepository();
    TheaterRepository getTheaterRepository();
    MovieTheaterDetailRepository getMovieTheaterDetailRepository();
    RatingRepository getRatingRepository();
    FormatRepository getFormatRepository();
    GenreRepository getGenreRepository();
    MovieRepository getMovieRepository();
}
