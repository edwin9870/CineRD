package com.edwin.android.cinerd.moviefinder;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;

import java.util.ArrayList;
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
    @BindView(R.id.text_movie_name_finder)
    TextView mMovieNameFinderTextView;
    Unbinder unbinder;
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
                                        "name: " + movie.getName()+" selected", Toast.LENGTH_SHORT).show();
                                mMovieNameFinderTextView.setText(movie.getName());
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

    @OnClick(R.id.text_movie_name_finder)
    public void onViewClicked() {
        mPresenter.movieNameFilterClicked();
    }
}
