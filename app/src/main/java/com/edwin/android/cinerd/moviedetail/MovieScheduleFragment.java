package com.edwin.android.cinerd.moviedetail;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwin.android.cinerd.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieScheduleFragment extends Fragment {


    @BindView(R.id.recycler_view_movie_schedule)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private MovieScheduleAdapter mAdapter;

    public MovieScheduleFragment() {
    }

    public static MovieScheduleFragment newInstance() {
        MovieScheduleFragment fragment = new MovieScheduleFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_schedule, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAdapter = new MovieScheduleAdapter();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);


        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        List<Date> dates = new ArrayList<>();
        dates.add(new Date());
        dates.add(new Date());
        dates.add(new Date());
        mAdapter.setDates(dates);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
