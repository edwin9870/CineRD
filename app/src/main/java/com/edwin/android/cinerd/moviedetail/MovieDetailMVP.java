package com.edwin.android.cinerd.moviedetail;

import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/14/2017.
 */

public interface MovieDetailMVP {

    interface View {
        void setMovieName(String name);
        void setMovieGenreDuration(List<String> genres, Short duration);
        void setMovieReleaseDate(String releaseDate);
        void setRating(String imdb, String rottenTomatoes);
        void setImage(Movie movie);
        void setPresenter(Presenter presenter);
    }

    interface Presenter {
        void showMovieDetail(long movieId);
        void getMoviesByDayMovieNameTheaterName(int day, String movieName, String theaterName);
    }
}
