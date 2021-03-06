package com.edwin.android.cinerd.moviefinder;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;
import com.edwin.android.cinerd.util.DateUtil;
import com.edwin.android.cinerd.util.FirebaseUtil;
import com.edwin.android.cinerd.util.SpacesItemDecoration;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    public static final String BUNDLE_MOVIE_NAME_FINDER_ID = "BUNDLE_MOVIE_NAME_FINDER_ID";
    public static final String BUNDLE_DATE_FILTER_TEXT_ID = "BUNDLE_DATE_FILTER_TEXT_ID";
    public static final String BUNDLE_SCHEDULE_TIME_RECYCLER_VIEW_ID = "BUNDLE_SCHEDULE_TIME_RECYCLER_VIEW_ID";

    public static final String BUNDLE_THEATERS_NAME_RECYCLER_VIEW_ID = "BUNDLE_THEATERS_NAME_RECYCLER_VIEW_ID";


    @BindView(R.id.edit_text_movie_name_finder)
    TextView mMovieNameFinderTextView;
    Unbinder unbinder;
    @BindView(R.id.text_date_filter)
    TextView mTextDateFilter;
    @BindView(R.id.recycler_view_available_hour)
    RecyclerView mAvailableHourRecyclerView;
    @BindView(R.id.recycler_view_available_movie_theaters)
    RecyclerView mAvailableMovieTheatersRecyclerView;
    @BindView(R.id.toolbar_detail_movie)
    Toolbar mToolbar;
    @BindView(R.id.card_view_calendar)
    CardView mCalendarCardView;
    @BindView(R.id.card_view_schedule)
    CardView mScheduleCardView;
    @BindView(R.id.card_view_movie_theater)
    CardView mMovieTheaterCardView;
    private MovieFinderMVP.Presenter mPresenter;
    private MovieFinderTimeAdapter mMovieFinderTimeAdapter;
    private MovieFinderTheaterAdapter mMovieFinderTheaterAdapter;
    private List<MovieTheaterDetail> mUniqueMovieTheaterDetails;
    private List<String> mTheatersNameToShow;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundleFcm = new Bundle();
        bundleFcm.putString(FirebaseUtil.PARAM.ACTIVITY_NAME, MovieFinderActivity.class.getSimpleName());
        mFirebaseAnalytics.logEvent(FirebaseUtil.EVENT.OPEN_ACTIVITY, bundleFcm);

        mMovieFinderTimeAdapter = new MovieFinderTimeAdapter(this, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.movie_finder_time_columns));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space_between_movie_poster);
        mAvailableHourRecyclerView.addItemDecoration(new SpacesItemDecoration(
                getResources().getInteger(R.integer.movie_finder_time_columns),
                spacingInPixels,
                false));

        mAvailableHourRecyclerView.setLayoutManager(gridLayoutManager);
        mAvailableHourRecyclerView.setAdapter(mMovieFinderTimeAdapter);

        mMovieFinderTheaterAdapter = new MovieFinderTheaterAdapter();
        LinearLayoutManager linearLayoutManagerTheater = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mAvailableMovieTheatersRecyclerView.setLayoutManager(linearLayoutManagerTheater);
        mAvailableMovieTheatersRecyclerView.setAdapter(mMovieFinderTheaterAdapter);

        return view;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            String movieNameFinder = savedInstanceState.getString(BUNDLE_MOVIE_NAME_FINDER_ID);
            if(movieNameFinder != null) {
                mMovieNameFinderTextView.setText(movieNameFinder);
                mCalendarCardView.setVisibility(View.VISIBLE);
            }

            String dateFilter = savedInstanceState.getString(BUNDLE_DATE_FILTER_TEXT_ID);
            if(dateFilter != null) {
                mTextDateFilter.setText(dateFilter);
            }

            mUniqueMovieTheaterDetails = savedInstanceState.getParcelableArrayList(BUNDLE_SCHEDULE_TIME_RECYCLER_VIEW_ID);
            if(mUniqueMovieTheaterDetails != null) {
                Log.d(TAG, "Restoring Recycler View movie time");
                mMovieFinderTimeAdapter.setMovieTheaterDetails(mUniqueMovieTheaterDetails);
                mScheduleCardView.setVisibility(View.VISIBLE);
            }

            mTheatersNameToShow = savedInstanceState.getStringArrayList(BUNDLE_THEATERS_NAME_RECYCLER_VIEW_ID);

            if(mTheatersNameToShow != null) {
                showTheaters(mTheatersNameToShow);
            }


        }
        super.onViewStateRestored(savedInstanceState);
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
        Collections.sort(movies);
        SimpleSearchDialogCompat searchDialogCompat = new SimpleSearchDialogCompat
                (getActivity(), title,
                        placeHolder, null, new ArrayList<Movie>(movies),
                        new SearchResultListener<Movie>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog, Movie
                                    movie, int position) {
                                MovieFinderFragment.this.clearForm();
                                mMovieNameFinderTextView.setText(movie.getName());
                                mCalendarCardView.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        });
        searchDialogCompat.getContext().setTheme(R.style.AppTheme_Dialog_Light_DarkText);
        searchDialogCompat.show();
    }

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
                        mScheduleCardView.setVisibility(View.VISIBLE);
                        mMovieTheaterCardView.setVisibility(View.INVISIBLE);
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

        mUniqueMovieTheaterDetails = new ArrayList<>();
        List<Long> existenceTime = new ArrayList<>();
        for (MovieTheaterDetail movieTheaterDetail : movieTheaterDetails) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(movieTheaterDetail.getAvailableDate());
            int hour = instance.get(Calendar.HOUR);
            int minute = instance.get(Calendar.MINUTE);
            long time = hour + minute;
            if (!existenceTime.contains(time)) {
                existenceTime.add(time);
                mUniqueMovieTheaterDetails.add(movieTheaterDetail);
            }
        }

        mMovieFinderTimeAdapter.setMovieTheaterDetails(new ArrayList<>(mUniqueMovieTheaterDetails));
    }

    @Override
    public void showTheaters(List<String> theatersName) {
        mTheatersNameToShow = theatersName;
        mMovieFinderTheaterAdapter.setTheatersName(mTheatersNameToShow);
        mMovieTheaterCardView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.edit_text_movie_name_finder, R.id.text_date_filter, R.id
            .view_line_text_date_filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_text_movie_name_finder:
                mPresenter.movieNameFilterClicked(getActivity());
                break;
            case R.id.text_date_filter:
                mPresenter.movieFinderCalendarClicked(getActivity(), mMovieNameFinderTextView
                        .getText().toString());
                break;
            case R.id.view_line_text_date_filter:
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mMovieNameFinderTextView.getText().toString().length() > 0) {
            outState.putString(BUNDLE_MOVIE_NAME_FINDER_ID, mMovieNameFinderTextView.getText().toString());
        }

        if(mTextDateFilter.getText().toString().length() > 0) {
            outState.putString(BUNDLE_DATE_FILTER_TEXT_ID, mTextDateFilter.getText().toString());
            outState.putParcelableArrayList(BUNDLE_SCHEDULE_TIME_RECYCLER_VIEW_ID, new ArrayList<>(mUniqueMovieTheaterDetails));
        }

        if(mTheatersNameToShow != null && mTheatersNameToShow.size() > 0) {
            outState.putStringArrayList(BUNDLE_THEATERS_NAME_RECYCLER_VIEW_ID, new ArrayList<>(mTheatersNameToShow));
        }

        super.onSaveInstanceState(outState);
    }

    private void clearForm() {
        mScheduleCardView.setVisibility(View.INVISIBLE);
        mMovieTheaterCardView.setVisibility(View.INVISIBLE);
        mTextDateFilter.setText("");
        if (mMovieFinderTimeAdapter != null) {
            mMovieFinderTimeAdapter.setMovieTheaterDetails(null);
        }
        if (mMovieFinderTheaterAdapter != null) {
            mMovieFinderTheaterAdapter.setTheatersName(null);
        }

    }
}
