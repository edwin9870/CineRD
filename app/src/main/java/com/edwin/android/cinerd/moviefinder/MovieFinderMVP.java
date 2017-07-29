package com.edwin.android.cinerd.moviefinder;

import android.content.Context;

import com.edwin.android.cinerd.entity.Movie;

import java.util.Date;
import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/27/2017.
 */

public interface MovieFinderMVP {

    interface View {
        void setPresenter(Presenter presenter);
        void showMovieFilterDialog(List<Movie> movies);
        void showCalendar(int maxAdditionalDays);
        void showDateSelected(String message);
    }

    interface Presenter {
        void movieNameFilterClicked(final Context context);
        void movieFinderCalendarClicked(Context context, String movieName);
        void showCalendarDate(Context context, Date date);
    }
}
