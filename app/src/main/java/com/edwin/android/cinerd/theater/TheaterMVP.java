package com.edwin.android.cinerd.theater;

import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/30/2017.
 */

public interface TheaterMVP {

    interface View {
        void setPresenter(Presenter presenter);
        void onReceiveMovies(List<Movie> movies);
    }

    interface Presenter {
        void getMovies();
    }
}
