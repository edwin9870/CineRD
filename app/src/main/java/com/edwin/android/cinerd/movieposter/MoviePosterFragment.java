package com.edwin.android.cinerd.movieposter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwin.android.cinerd.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MoviePosterFragment extends Fragment {


    @BindView(R.id.recycler_view_movie_poster)
    RecyclerView mRecyclerViewMoviePoster;
    Unbinder unbinder;
    private MoviePosterAdapter mAdapter;

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

        mAdapter = new MoviePosterAdapter(getActivity());
        mRecyclerViewMoviePoster.setAdapter(mAdapter);
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
}
