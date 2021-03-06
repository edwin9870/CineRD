package com.edwin.android.cinerd.moviedetail;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
import com.edwin.android.cinerd.moviedetail.viewpager.MovieScheduleFragment;
import com.edwin.android.cinerd.moviedetail.viewpager.MovieSynopsisFragment;
import com.edwin.android.cinerd.moviedetail.viewpager.ViewPagerAdapter;
import com.edwin.android.cinerd.movieposter.MoviePosterActivity;
import com.edwin.android.cinerd.util.FirebaseUtil;
import com.edwin.android.cinerd.views.WrapContentViewPager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.edwin.android.cinerd.util.ImageUtil.getImageFile;
import static com.edwin.android.cinerd.util.ResourceUtil.getResourceColor;

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
    WrapContentViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar_detail_movie)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_movie_detail)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.image_movie_detail_poster)
    ImageView mMovieDetailPosterImageView;
    @BindView(R.id.image_button_play_trailer)
    ImageButton mPlayTrailerImageButton;
    @BindView(R.id.app_bar_movie_detail)
    AppBarLayout mAppBar;
    private Unbinder mUnbinder;
    private MovieDetailMVP.Presenter mPresenter;
    private MovieScheduleFragment mScheduleFragment;
    private long mMovieId;
    private ViewPagerAdapter mAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundleFcm = new Bundle();
        bundleFcm.putString(FirebaseUtil.PARAM.ACTIVITY_NAME, MovieDetailActivity.class.getSimpleName());
        mFirebaseAnalytics.logEvent(FirebaseUtil.EVENT.OPEN_ACTIVITY, bundleFcm);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbar.setExpandedTitleColor(getResourceColor(getActivity(), android.R.color.transparent));


        Log.d(TAG, "Movie ID displayed: " + mMovieId);
        mPresenter.showMovieDetail(getActivity(), mMovieId);

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

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.reMeasureCurrentPage(mViewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    mPlayTrailerImageButton.setVisibility(View.INVISIBLE);
                    mMovieDetailPosterImageView.setVisibility(View.INVISIBLE);
                } else {
                    mPlayTrailerImageButton.setVisibility(View.VISIBLE);
                    mMovieDetailPosterImageView.setVisibility(View.VISIBLE);
                }
            }
        });
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
    public void setImage(Movie movie) {
        Picasso picasso = Picasso.with(getActivity());

        if(movie.getBackdropUrl() != null) {
            File backdropImageFile = getImageFile(getActivity(), movie.getBackdropUrl());
            picasso.load(backdropImageFile).fit().into(imageMovieBackdrop);
        }

        if(movie.getPosterUrl() != null) {
            File posterImageFile = getImageFile(getActivity(), movie.getPosterUrl());
            picasso.load(posterImageFile).fit().into(mMovieDetailPosterImageView);
        }
    }

    @Override
    public void setPresenter(MovieDetailMVP.Presenter presenter) {
        Log.d(TAG, "Setting MovieDetail presenter");
        mPresenter = presenter;
    }

    @Override
    public void openTrailer(String trailerUrl) {
        Log.d(TAG, "Movie trailer url: " + trailerUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(trailerUrl));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.image_button_play_trailer)
    public void onViewClicked() {
        Log.d(TAG, "image button play trailer");
        mPresenter.showTrailer(getActivity(), mMovieId);
    }
}
