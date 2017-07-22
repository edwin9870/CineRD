package com.edwin.android.cinerd.moviedetail;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Theater;
import com.edwin.android.cinerd.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieScheduleFragment extends Fragment implements MovieScheduleAdapter
        .ScheduleDayClicked {


    public static final Date todayDate = new Date();
    public static final String TAG = MovieScheduleFragment.class.getSimpleName();
    public static final String ARGUMENT_MOVIE = "MOVIE";
    Unbinder unbinder;
    @BindView(R.id.recycler_view_movie_schedule)
    RecyclerView mRecyclerView;
    @BindView(R.id.text_theater_name)
    TextView textTheaterName;
    private MovieScheduleAdapter mAdapter;
    private Movie mMovie;

    public MovieScheduleFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param movie
     * @return a new instance of fragment MovieScheduleFragment
     */
    public static MovieScheduleFragment newInstance(Movie movie) {
        MovieScheduleFragment fragment = new MovieScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mMovie = getArguments().getParcelable(ARGUMENT_MOVIE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_schedule, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAdapter = new MovieScheduleAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);


        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        List<Date> dates = getDates();
        mAdapter.setDates(dates);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private List<Date> getDates() {
        List<Date> dates = new ArrayList<>();
        dates.add(todayDate);
        dates.add(DateUtil.addDay(todayDate, 1));
        dates.add(DateUtil.addDay(todayDate, 2));
        return dates;
    }

    @Override
    public void onClickDay(Date date) {
        Log.d(TAG, "Date clicked: " + date);
        Toast.makeText(getActivity(), "Touched", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.text_theater_name)
    public void onViewClicked() {
        Log.d(TAG, "Theater name clicked");

        String dialogTitle = MovieScheduleFragment.this.getString(R.string
                .movie_theater_search_dialog_title);
        String inputPlaceHolder = MovieScheduleFragment.this.getString(R.string
                .movie_theater_search_dialog_input_place_holder);

        new SimpleSearchDialogCompat(getActivity(), dialogTitle,
                inputPlaceHolder, null, ((ArrayList<Theater>) mMovie.getTheaters()),
                new SearchResultListener<Theater>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat dialog, Theater
                            theater, int position) {
                        Toast.makeText(MovieScheduleFragment.this.getActivity(), theater.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        MovieScheduleFragment.this.textTheaterName.setText(theater.getTitle());
                        dialog.dismiss();

                    }
                }).show();
    }

}
