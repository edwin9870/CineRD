package com.edwin.android.cinerd.moviedetail;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.MovieCollectorJSON;
import com.edwin.android.cinerd.entity.Movie;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
    @BindView(R.id.text_movie_name)
    TextView mMovieNameTextView;
    @BindView(R.id.text_movie_genre)
    TextView mMovieGenreTextView;
    @BindView(R.id.text_movie_release_date)
    TextView mMoviereleaseDateTextView;
    @BindView(R.id.text_imdb_value)
    TextView mImdbValueTextView;
    @BindView(R.id.text_rotten_tomatoes_value)
    TextView textRottenTomatoesValue;
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

        mMovieNameTextView.setText(mMovie.getName());
        Log.d(TAG, "mMovie.getName(): " + mMovie.getName());
        String genre = TextUtils.join(", ", mMovie.getGenre());
        mMovieGenreTextView.setText(genre);
        mMovieGenreTextView.append(" | ");
        mMovieGenreTextView.append(mMovie.getDuration().toString());
        mMovieGenreTextView.append(" min");

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        mMoviereleaseDateTextView.setText(df.format(mMovie.getReleaseDate()));

        mImdbValueTextView.setText(mMovie.getRating().getImdb());
        textRottenTomatoesValue.setText(mMovie.getRating().getRottentomatoes());


        return view;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
