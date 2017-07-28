package com.edwin.android.cinerd.moviefinder;

import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/27/2017.
 */

public interface MovieFinderMVP {

    interface View {
        void setPresenter(Presenter presenter);
        void showMovieFilterDialog(List<Movie> movies);
    }

    interface Presenter {
        void movieNameFilterClicked();
    }
}
