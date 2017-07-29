package com.edwin.android.cinerd.moviefinder;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
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

public class MovieFinderFragment extends Fragment implements MovieFinderMVP.View {

    public static final String TAG = MovieFinderFragment.class.getSimpleName();
    @BindView(R.id.edit_text_movie_name_finder)
    EditText mMovieNameFinderEditText;
    Unbinder unbinder;
    @BindView(R.id.image_movie_finder_calendar)
    ImageView mMovieFinderCalendarImageView;
    private MovieFinderMVP.Presenter mPresenter;

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
                                Toast.makeText(MovieFinderFragment.this.getActivity(), "Movie " +
                                        "name: " + movie.getName() + " selected", Toast
                                        .LENGTH_SHORT).show();
                                mMovieNameFinderEditText.setText(movie.getName());
                                mMovieFinderCalendarImageView.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        });
        searchDialogCompat.getContext().setTheme(R.style.AppTheme_Dialog_Light_DarkText);
        searchDialogCompat.show();
    }

    @Override
    public void showCalendar() {
        Log.d(TAG, "Showing calendar");
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
                    }
                }, year, month, day);
        Date initialDate = new Date();
        Date endDate = DateUtil.addDay(initialDate, getResources().getInteger(R.integer
                .max_movie_calendar_additional_days));
        
        datePickerDialog.getDatePicker().setMinDate(initialDate.getTime());
        datePickerDialog.getDatePicker().setMaxDate(endDate.getTime());
        datePickerDialog.show();
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
                mPresenter.movieNameFilterClicked();
                break;
            case R.id.image_movie_finder_calendar :
                mPresenter.movieFinderCalendarClicked();
                break;
            default:
                throw new UnsupportedOperationException("Not supported view with the id: "+ view.getId());
        }
    }

}