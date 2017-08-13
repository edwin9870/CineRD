package com.edwin.android.cinerd.theater;

import com.edwin.android.cinerd.data.repositories.TheaterRepository;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Theater;

import java.util.Date;
import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/30/2017.
 */

public interface TheaterMVP {

    interface View {
        void setPresenter(Presenter presenter);
        void onReceiveMovies(List<Movie> movies);
        void setActivityTitle(String title);
        void setTheaterId(int theaterId);
        void showTheatersDialog(TheaterRepository theaterRepository, Date minDate);
    }

    interface Presenter {
        void getMovies(int theaterId);
        void setActivityTitle(int mTheaterId);
        void setActivityTitle(String title);
        void showTheatersDialog();
        void onClickFabButton();

    }
}
