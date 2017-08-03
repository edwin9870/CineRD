package com.edwin.android.cinerd.moviedetail;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.moviedetail.viewpager.MovieScheduleFragment;
import com.edwin.android.cinerd.moviedetail.viewpager.MovieSynopsisFragment;
import com.edwin.android.cinerd.moviedetail.viewpager.ViewPagerAdapter;
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
    public static final String ARGUMENT_MOVIE_ID = "ARGUMENT_MOVIE_ID";
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
    @BindView(R.id.toolbar_detail_movie)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_movie_detail)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.image_movie_detail_poster)
    ImageView mMovieDetailPosterImageView;
    private Unbinder mUnbinder;
    private MovieDetailMVP.Presenter mPresenter;
    private MovieScheduleFragment mScheduleFragment;
    private long mMovieId;
    private ViewPagerAdapter mAdapter;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(long movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ARGUMENT_MOVIE_ID, movieId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieId = getArguments().getLong(ARGUMENT_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbar.setExpandedTitleColor(getHexColor(android.R.color.transparent));

        Log.d(TAG, "Movie ID displayed: " + mMovieId);
        mPresenter.showMovieDetail(mMovieId);

        mAdapter = new ViewPagerAdapter(getFragmentManager());

        MovieSynopsisFragment movieSynopsisFragment = MovieSynopsisFragment.newInstance(mMovieId);
        mAdapter.addFragment(movieSynopsisFragment, getActivity().getString(R.string
                .tab_synopsis_name));

        mScheduleFragment = MovieScheduleFragment.newInstance(mMovieId, getTag());
        Log.d(TAG, "mScheduleFragment: " + mScheduleFragment.getTag());
        mAdapter.addFragment(mScheduleFragment, getActivity().getString(R.string
                .tab_schedule_name));

        mViewPager.setAdapter(mAdapter);
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
        //TODO: Add failude image background
        picasso.load(R.drawable.maxmaxbackdrop).fit().into(imageMovieBackdrop);
        picasso.load(R.drawable.maxmaxposter).fit().into(mMovieDetailPosterImageView);
    }

    @Override
    public void setPresenter(MovieDetailMVP.Presenter presenter) {
        Log.d(TAG, "Setting MovieDetail presenter");
        mPresenter = presenter;
    }


    private int getHexColor(int colorCode) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getResources().getColor(colorCode, getActivity().getTheme());
        } else {
            color = getResources().getColor(colorCode);
        }
        return color;
    }

}
