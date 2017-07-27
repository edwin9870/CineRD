package com.edwin.android.cinerd.movieposter;

import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/10/2017.
 */

public interface MoviePosterMVP {

    interface View {
        void onReceiveMovies(List<Movie> movies);
        void setPresenter(Presenter presenter);
        void showMovieAndTheaterDialog();
        void onClickMovie();
        void onClickTheater();
    }

    interface Presenter {
        void getMovies();
        void fabButtonAction();
    }
}
