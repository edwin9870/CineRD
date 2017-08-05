package com.edwin.android.cinerd.theater;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.entity.Theater;
import com.edwin.android.cinerd.moviedetail.MovieDetailActivity;
import com.edwin.android.cinerd.movieposter.MoviePosterAdapter;
import com.edwin.android.cinerd.util.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

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
        return new TheaterFragment();
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

        mPresenter.showTheatersDialog();
        mPresenter.setActivityTitle(getResources().getString(R.string.theater_activity_title));

        mAdapter = new MoviePosterAdapter(getActivity(), this);
        //TODO: Use a integer resource for column size
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen
                .space_between_movie_poster);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(2, spacingInPixels, false));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

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
    public void setActivityTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showTheatersDialog(List<Theater> theaters) {
        Collections.sort(theaters);
        SimpleSearchDialogCompat searchDialogCompat = new SimpleSearchDialogCompat
                (getActivity(), getString(R.string.theater_finder_dialog_title),
                        getString(R.string.theater_finder_dialog_place_holder), null, new ArrayList<Theater>(theaters),
                        new SearchResultListener<Theater>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog, Theater
                                    theaterSearchable, int position) {
                                Toast.makeText(TheaterFragment.this.getActivity(),
                                        theaterSearchable.getTitle(),
                                        Toast.LENGTH_SHORT).show();

                                Log.d(TAG, "Theater selected: " + theaterSearchable.getTitle());
                                mPresenter.setActivityTitle(theaterSearchable.getTheaterId());
                                mPresenter.getMovies(theaterSearchable.getTheaterId());
                                dialog.dismiss();

                            }
                        });
        searchDialogCompat.getContext().setTheme(R.style.AppTheme_Dialog_Light_DarkText);
        searchDialogCompat.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClickMovie(Movie movie) {
        Log.d(TAG, "Movie clicked: " + movie);
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.BUNDLE_MOVIE_ID, movie.getMovieId());
        getActivity().startActivity(intent);
    }
}
