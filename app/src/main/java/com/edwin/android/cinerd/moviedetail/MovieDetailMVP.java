package com.edwin.android.cinerd.moviedetail;

import android.content.Context;

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
        void openTrailer(String trailerUrl);
        void showMessage(String message);
    }

    interface Presenter {
        void showMovieDetail(Context context, long movieId);
        void showTrailer(Context context, long mMovieId);
    }
}
