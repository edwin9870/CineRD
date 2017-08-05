package com.edwin.android.cinerd.movieposter;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.moviedetail.MovieDetailActivity;
import com.edwin.android.cinerd.moviefinder.MovieFinderActivity;
import com.edwin.android.cinerd.theater.TheaterActivity;
import com.edwin.android.cinerd.util.SpacesItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MoviePosterFragment extends Fragment implements
        MoviePosterMVP.View,
        MoviePosterAdapter.MoviePosterListener{


    public static final String TAG = MoviePosterFragment.class.getSimpleName();
    @BindView(R.id.recycler_view_movie_poster)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private MoviePosterAdapter mAdapter;
    private MoviePosterMVP.Presenter mPresenter;

    public MoviePosterFragment() {
    }

    public static MoviePosterFragment newInstance() {
        MoviePosterFragment fragment = new MoviePosterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_poster, container, false);
        unbinder = ButterKnife.bind(this, view);

        mAdapter = new MoviePosterAdapter(getActivity(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getResources()
                .getInteger(R.integer.movie_poster_columns));

        mRecyclerView.setLayoutManager(gridLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen
                .space_between_movie_poster);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getInteger(R
                .integer.movie_poster_space_item), spacingInPixels, false));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getMovies();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onReceiveMovies(List<Movie> movies) {
        mAdapter.setMovies(movies);
    }

    @Override
    public void setPresenter(MoviePosterMVP.Presenter presenter) {
        mPresenter = presenter;
        Log.d(TAG, "Setting presenter");
    }


    @Override
    public void onClickMovie(Movie movie) {
        Log.d(TAG, "Movie clicked: " + movie);
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.BUNDLE_MOVIE_ID, movie.getMovieId());
        getActivity().startActivity(intent);
    }

    @Override
    public void onClickMovie() {
        Log.d(TAG, "onClickMovie fired");
        Intent intent = new Intent(getActivity(), MovieFinderActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onClickTheater() {
        Log.d(TAG, "onClickTheater fired");
        Intent intent = new Intent(getActivity(), TheaterActivity.class);
        getActivity().startActivity(intent);
    }
}
