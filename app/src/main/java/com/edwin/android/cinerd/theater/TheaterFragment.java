package com.edwin.android.cinerd.theater;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.movieposter.MoviePosterAdapter;
import com.edwin.android.cinerd.util.SpacesItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TheaterFragment extends Fragment implements TheaterMVP.View, MoviePosterAdapter.MoviePosterListener {


    public static final String TAG = TheaterFragment.class.getSimpleName();
    @BindView(R.id.recycler_view_theater_movie_poster)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private TheaterMVP.Presenter mPresenter;
    private MoviePosterAdapter mAdapter;

    public TheaterFragment() {
    }

    public static TheaterFragment newInstance() {
        TheaterFragment fragment = new TheaterFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theater, container, false);
        unbinder = ButterKnife.bind(this, view);

        mAdapter = new MoviePosterAdapter(getActivity(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen
                .space_between_movie_poster);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(2, spacingInPixels, false));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getMovies();
        return view;
    }

    @Override
    public void setPresenter(TheaterMVP.Presenter presenter) {
        Log.d(TAG, "Setting mPresenter");
        mPresenter = presenter;
    }

    @Override
    public void onReceiveMovies(List<Movie> movies) {
        mAdapter.setMovies(movies);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClickMovie(Movie movie) {

    }
}
