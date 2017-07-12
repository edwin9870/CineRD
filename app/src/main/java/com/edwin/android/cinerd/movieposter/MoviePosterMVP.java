package com.edwin.android.cinerd.movieposter;

import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/10/2017.
 */

public interface MoviePosterMVP {

    interface View {
        void showMovies(List<Movie> movies);
    }

    interface Presenter {
        void getMovies();
    }
}
