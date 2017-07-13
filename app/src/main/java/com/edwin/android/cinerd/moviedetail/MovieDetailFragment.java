package com.edwin.android.cinerd.moviedetail;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.MovieCollectorJSON;
import com.edwin.android.cinerd.entity.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {


    public static final String TAG = MovieDetailFragment.class.getSimpleName();
    @BindView(R.id.image_movie_backdrop)
    ImageView imageMovieBackdrop;
    private Movie mMovie;
    private Unbinder mUnbinder;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance() {
        MovieDetailFragment fragment = new MovieDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        MovieCollectorJSON movieCollectorJSON = new MovieCollectorJSON(getActivity());
        mMovie = movieCollectorJSON.getMovies().get(0);

        Log.d(TAG, "Movie displayed: " + mMovie.toString());

        Picasso picasso = Picasso.with(getActivity());
        picasso.load(R.drawable.maxmaxbackdrop).fit().into(imageMovieBackdrop);


        return view;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
