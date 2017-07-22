package com.edwin.android.cinerd.moviedetail;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailMVP.View {


    public static final String TAG = MovieDetailFragment.class.getSimpleName();
    @BindView(R.id.image_movie_backdrop)
    ImageView imageMovieBackdrop;
    @BindView(R.id.text_movie_name)
    TextView mMovieNameTextView;
    @BindView(R.id.text_movie_genre_duration)
    TextView mMovieGenreDurationTextView;
    @BindView(R.id.text_movie_release_date)
    TextView mMoviereleaseDateTextView;
    @BindView(R.id.text_imdb_value)
    TextView mImdbValueTextView;
    @BindView(R.id.text_rotten_tomatoes_value)
    TextView textRottenTomatoesValue;
    @BindView(R.id.pager_tab_content)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    private Movie mMovie;
    private Unbinder mUnbinder;
    private MovieDetailMVP.Presenter mPresenter;

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

        //TODO: Receive movie using the bunde
        MovieCollectorJSON movieCollectorJSON = new MovieCollectorJSON(getActivity());
        mMovie = movieCollectorJSON.getMovies().get(0);

        Log.d(TAG, "Movie displayed: " + mMovie.toString());
        mPresenter.showMovieDetail(mMovie);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        MovieSynopsisFragment movieSynopsisFragment = MovieSynopsisFragment.newInstance(mMovie
                .getSynopsis());

        adapter.addFragment(movieSynopsisFragment, getActivity().getString(R.string.tab_synopsis_name));

        MovieScheduleFragment movieScheduleFragment = MovieScheduleFragment.newInstance(mMovie);
        adapter.addFragment(movieScheduleFragment, getActivity().getString(R.string.tab_schedule_name));

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);


        return view;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void setMovieName(String name) {
        mMovieNameTextView.setText(name);
    }

    @Override
    public void setMovieGenreDuration(List<String> genres, Short duration) {
        String genre = TextUtils.join(", ", genres);
        mMovieGenreDurationTextView.setText(genre);
        mMovieGenreDurationTextView.append(" | ");
        mMovieGenreDurationTextView.append(String.valueOf(duration));
        mMovieGenreDurationTextView.append(" min");

    }

    @Override
    public void setMovieReleaseDate(String releaseDate) {
        mMoviereleaseDateTextView.setText(releaseDate);
    }

    @Override
    public void setRating(String imdb, String rottenTomatoes) {
        mImdbValueTextView.setText(imdb);
        textRottenTomatoesValue.setText(rottenTomatoes);
    }

    @Override
    public void setBackdropImage(int resourceId) {
        Picasso picasso = Picasso.with(getActivity());
        picasso.load(R.drawable.maxmaxbackdrop).fit().into(imageMovieBackdrop);
    }

    @Override
    public void setPresenter(MovieDetailMVP.Presenter presenter) {
        Log.d(TAG, "Setting MovieDetail presenter");
        mPresenter = presenter;
    }
}
