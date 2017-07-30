package com.edwin.android.cinerd.moviefinder;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Log;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.MovieDataRepository;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;
import com.edwin.android.cinerd.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 7/27/2017.
 */

public class MovieFinderPresenter implements MovieFinderMVP.Presenter {


    public static final String TAG = MovieFinderPresenter.class.getSimpleName();
    private final MovieFinderMVP.View mView;
    private final MovieDataRepository mRepository;

    @Inject
    public MovieFinderPresenter(MovieFinderMVP.View view, MovieDataRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Inject
    public void setupListener() {
        mView.setPresenter(this);
    }

    @Override
    public void movieNameFilterClicked(final Context context) {
        new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                List<Movie> movies = MovieFinderPresenter.this.mRepository.getMovies();
                List<Movie> moviesToReturn = new ArrayList<Movie>();
                final int maxDayCount = context.getResources().getInteger(R.integer.max_movie_calendar_additional_days);
                for(Movie movie: movies) {
                    if(getMaxAvailableDate(movie.getMovieId(), maxDayCount, new Date()) >= 0) {
                        moviesToReturn.add(movie);
                    }
                }
                return moviesToReturn;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                mView.showMovieFilterDialog(movies);
            }
        }.execute();
    }

    @Override
    public void movieFinderCalendarClicked(Context context, final String movieName) {
        final int maxDays = context.getResources().getInteger(R.integer
                .max_movie_calendar_additional_days);
        final long movieId = mRepository.getMovieIdByName(movieName);
        new AsyncTask<Void, Void, Integer>(){

            @Override
            protected Integer doInBackground(Void... voids) {
                return getMaxAvailableDate(movieId, maxDays, new Date());
            }

            @Override
            protected void onPostExecute(Integer maxAdditionalDays) {
                super.onPostExecute(maxAdditionalDays);
                mView.showCalendar(maxAdditionalDays);
            }
        }.execute();


    }

    @Override
    public void showCalendarDate(Context context, String movieName, Date date) {
        String dateToShow = DateFormat.format(context.getString(R.string.date_calendar), date).toString();
        mView.showDateSelected(dateToShow);
        long movieId = mRepository.getMovieIdByName(movieName);
        List<MovieTheaterDetail> movieTheaterDetails = mRepository
                .getMoviesTheaterDetailByMovieIdAvailableDate(movieId, date);
        mView.showAvailableMovieTime(movieTheaterDetails);
    }

    @Override
    public void showMovieTheaterByDate(long movieId, Date date) {
        List<MovieTheaterDetail> movieTheaters = mRepository
                .getMoviesTheaterDetailByMovieIdAvailableDate(movieId, date);
        Calendar instance = Calendar.getInstance();

        Set<String> theatersName = new HashSet<>();
        instance.setTime(date);
        int hour = instance.get(Calendar.HOUR);
        int minute = instance.get(Calendar.MINUTE);
        int dayOfTheYear = instance.get(Calendar.DAY_OF_YEAR);

        String theaterName;
        for (MovieTheaterDetail movieTheaterDetail : movieTheaters) {
            instance.setTime(movieTheaterDetail.getAvailableDate());
            instance.setTime(date);
            if (instance.get(Calendar.HOUR) == hour &&
                    instance.get(Calendar.MINUTE) == minute &&
                    instance.get(Calendar.DAY_OF_YEAR) == dayOfTheYear) {
                theaterName = mRepository.getTheaterNameById(movieTheaterDetail
                        .getTheaterId());
                theatersName.add(theaterName);

            }
        }
        Log.d(TAG, "theatersName: " + theatersName);
        mView.showTheaters(new ArrayList<>(theatersName));
    }

    /**
     *
     *
     * @param movieId
     * @param maxDays
     * @param fromDate
     * @return -1 If it doesn't found a movie with the specific date or greater, otherwise return 0 or greater
     */
    private int getMaxAvailableDate(long movieId, int maxDays, Date fromDate) {
        int maxDayInList = -1;
        Log.d(TAG, "movieId: "+ movieId);
        for (int i = 0; i < maxDays; i++){
            List<MovieTheaterDetail> moviesTheaterDetailByMovieIdAvailableDate =
                    mRepository.getMoviesTheaterDetailByMovieIdAvailableDate(movieId,
                            DateUtil.addDay(fromDate, i));
            Log.d(TAG, "getMaxAvailableDate. fromDate: " + fromDate + ", i: " + i + ", result: "
                    + DateUtil.addDay(fromDate, i) + ". moviesTheaterDetailByMovieIdAvailableDate" +
                    ".size(): " + moviesTheaterDetailByMovieIdAvailableDate.size());
            if(moviesTheaterDetailByMovieIdAvailableDate.size() > 0) {
                Log.d(TAG, "moviesTheaterDetailByMovieIdAvailableDate.size > 1");
                maxDayInList = i;
            }
        }
        return maxDayInList;
    }
}
