package com.edwin.android.cinerd.moviedetail.viewpager;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.MovieCollectorJSON;
import com.edwin.android.cinerd.data.MovieDataRepository;
import com.edwin.android.cinerd.entity.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieSynopsisFragment extends Fragment {
    public static final String TAG = MovieSynopsisFragment.class.getSimpleName();
    private static final String ARGUMENT_MOVIE_ID = "MOVIE_SYNOPSIS";
    @BindView(R.id.text_movie_synopsis)
    TextView mMovieSynopsisTextView;
    Unbinder unbinder;

    private long mMovieId;

    public MovieSynopsisFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movieId Parameter 1.
     * @return A new instance of fragment MovieSynopsisFragment.
     */
    public static MovieSynopsisFragment newInstance(Long movieId) {
        MovieSynopsisFragment fragment = new MovieSynopsisFragment();
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieId = getArguments().getLong(ARGUMENT_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_synopsis, container, false);
        unbinder = ButterKnife.bind(this, view);
        Log.d(TAG, "MovieSynopsis created");
        Log.d(TAG, "Movie synopsis: " + mMovieId);

        MovieDataRepository movieDataRepository = new MovieDataRepository(getActivity()
                .getContentResolver(), new
                MovieCollectorJSON
                (getActivity()));

        Movie movie = movieDataRepository.getMovieById(mMovieId);
        mMovieSynopsisTextView.setText(movie.getSynopsis());

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
