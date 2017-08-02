package com.edwin.android.cinerd.moviefinder;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;
import com.edwin.android.cinerd.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class MovieFinderFragment extends Fragment implements MovieFinderMVP.View,
        MovieFinderTimeAdapter.MovieFinderTimeLister {

    public static final String TAG = MovieFinderFragment.class.getSimpleName();
    @BindView(R.id.edit_text_movie_name_finder)
    TextView mMovieNameFinderTextView;
    Unbinder unbinder;
    @BindView(R.id.image_movie_finder_calendar)
    ImageView mMovieFinderCalendarImageView;
    @BindView(R.id.text_date_filter)
    TextView mTextDateFilter;
    @BindView(R.id.recycler_view_available_hour)
    RecyclerView mAvailableHourRecyclerView;
    @BindView(R.id.recycler_view_available_movie_theaters)
    RecyclerView mAvailableMovieTheatersRecyclerView;
    @BindView(R.id.toolbar_detail_movie)
    Toolbar mToolbar;
    private MovieFinderMVP.Presenter mPresenter;
    private MovieFinderTimeAdapter mMovieFinderTimeAdapter;
    private MovieFinderTheaterAdapter mMovieFinderTheaterAdapter;

    public MovieFinderFragment() {

    }

    public static MovieFinderFragment newInstance() {
        MovieFinderFragment fragment = new MovieFinderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_finder, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public void setPresenter(MovieFinderMVP.Presenter presenter) {
        Log.d(TAG, "Setting fragment");
        mPresenter = presenter;
    }

    @Override
    public void showMovieFilterDialog(final List<Movie> movies) {
        String title = getString(R.string.movie_filter_search_dialog_title);
        String placeHolder = getString(R.string.movie_filter_search_dialog_input_place_holder);
        SimpleSearchDialogCompat searchDialogCompat = new SimpleSearchDialogCompat
                (getActivity(), title,
                        placeHolder, null, new ArrayList<Movie>(movies),
                        new SearchResultListener<Movie>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog, Movie
                                    movie, int position) {
                                MovieFinderFragment.this.clearForm();
                                Toast.makeText(MovieFinderFragment.this.getActivity(), "Movie " +
                                        "name: " + movie.getName() + " selected", Toast
                                        .LENGTH_SHORT).show();
                                mMovieNameFinderTextView.setText(movie.getName());
                                mMovieFinderCalendarImageView.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        });
        searchDialogCompat.getContext().setTheme(R.style.AppTheme_Dialog_Light_DarkText);
        searchDialogCompat.show();
    }

    //TODO: If maxAdditionalDays is greater than -1, don't open Date Dialog
    @Override
    public void showCalendar(int maxAdditionalDays) {
        Log.d(TAG, "Showing calendar");
        Log.d(TAG, "maxAdditionalDays: " + maxAdditionalDays);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Log.d(TAG, dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        Calendar instance = Calendar.getInstance();
                        instance.set(year, monthOfYear, dayOfMonth);
                        Date dateClicked = instance.getTime();
                        mPresenter.showCalendarDate(getActivity(), mMovieNameFinderTextView
                                .getText().toString(), dateClicked);
                    }
                }, year, month, day);
        Date initialDate = new Date();
        Date endDate = DateUtil.addDay(initialDate, maxAdditionalDays);
        Log.d(TAG, "initialDate: " + initialDate);
        Log.d(TAG, "endDate.getTime(): " + new Date(endDate.getTime()));
        datePickerDialog.getDatePicker().setMinDate(initialDate.getTime());
        datePickerDialog.getDatePicker().setMaxDate(endDate.getTime());
        datePickerDialog.show();
    }

    @Override
    public void showDateSelected(String message) {
        mTextDateFilter.setText(message);
    }

    @Override
    public void showAvailableMovieTime(List<MovieTheaterDetail> movieTheaterDetails) {
        mMovieFinderTimeAdapter = new MovieFinderTimeAdapter(this, getActivity());
        List<MovieTheaterDetail> uniqueMovieTheaterDetails = new ArrayList<>();
        List<Long> existenceTime = new ArrayList<>();
        for (MovieTheaterDetail movieTheaterDetail : movieTheaterDetails) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(movieTheaterDetail.getAvailableDate());
            int hour = instance.get(Calendar.HOUR);
            int minute = instance.get(Calendar.MINUTE);
            long time = hour + minute;
            if (!existenceTime.contains(time)) {
                existenceTime.add(time);
                uniqueMovieTheaterDetails.add(movieTheaterDetail);
            }
        }

        mMovieFinderTimeAdapter.setMovieTheaterDetails(new ArrayList<>(uniqueMovieTheaterDetails));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mAvailableHourRecyclerView.setLayoutManager(linearLayoutManager);
        mAvailableHourRecyclerView.setAdapter(mMovieFinderTimeAdapter);
    }

    @Override
    public void showTheaters(List<String> theatersName) {
        mMovieFinderTheaterAdapter = new MovieFinderTheaterAdapter();
        mMovieFinderTheaterAdapter.setTheatersName(theatersName);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mAvailableMovieTheatersRecyclerView.setLayoutManager(linearLayoutManager);
        mAvailableMovieTheatersRecyclerView.setAdapter(mMovieFinderTheaterAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.edit_text_movie_name_finder, R.id.image_movie_finder_calendar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_text_movie_name_finder:
                mPresenter.movieNameFilterClicked(getActivity());
                break;
            case R.id.image_movie_finder_calendar:
                mPresenter.movieFinderCalendarClicked(getActivity(), mMovieNameFinderTextView
                        .getText().toString());
                break;
            default:
                throw new UnsupportedOperationException("Not supported view with the id: " + view
                        .getId());
        }
    }

    @Override
    public void onClick(MovieTheaterDetail movieTheaterDetail) {
        Log.d(TAG, "Time clicked");
        mPresenter.showMovieTheaterByDate(movieTheaterDetail.getMovieId(),
                movieTheaterDetail.getAvailableDate());
    }


    private void clearForm() {
        mMovieFinderCalendarImageView.setVisibility(View.INVISIBLE);
        mTextDateFilter.setText("");
        if (mMovieFinderTimeAdapter != null) {
            mMovieFinderTimeAdapter.setMovieTheaterDetails(null);
        }
        if (mMovieFinderTheaterAdapter != null) {
            mMovieFinderTheaterAdapter.setTheatersName(null);
        }

    }
}
