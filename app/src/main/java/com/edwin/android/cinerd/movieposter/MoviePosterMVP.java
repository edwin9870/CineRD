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
        void onClickMovie();
        void onClickTheater();
        void openSettingActivity();
    }

    interface Presenter {
        void getMovies();
        void openMovieFilterActivity();
        void openTheatersActivity();
        void settingMenuClicked();
    }
}
