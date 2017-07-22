package com.edwin.android.cinerd.moviedetail.viewpager;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwin.android.cinerd.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieSynopsisFragment extends Fragment {
    public static final String TAG = MovieSynopsisFragment.class.getSimpleName();
    private static final String ARGUMENT_MOVIE_SYNOPSIS = "MOVIE_SYNOPSIS";
    @BindView(R.id.text_movie_synopsis)
    TextView mMovieSynopsisTextView;
    Unbinder unbinder;

    private String mSynopsis;

    public MovieSynopsisFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movieSynopsis Parameter 1.
     * @return A new instance of fragment MovieSynopsisFragment.
     */
    public static MovieSynopsisFragment newInstance(String movieSynopsis) {
        MovieSynopsisFragment fragment = new MovieSynopsisFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_MOVIE_SYNOPSIS, movieSynopsis);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSynopsis = getArguments().getString(ARGUMENT_MOVIE_SYNOPSIS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_synopsis, container, false);
        unbinder = ButterKnife.bind(this, view);
        Log.d(TAG, "MovieSynopsis created");
        Log.d(TAG, "Movie synopsis: " + mSynopsis);

        mMovieSynopsisTextView.setText(mSynopsis);

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
