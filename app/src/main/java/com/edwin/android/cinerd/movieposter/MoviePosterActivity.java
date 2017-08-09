package com.edwin.android.cinerd.movieposter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;
import com.edwin.android.cinerd.data.CineRdDbHelper;
import com.edwin.android.cinerd.data.MovieCollectorJSON;
import com.edwin.android.cinerd.data.ProcessMovies;
import com.edwin.android.cinerd.data.adapters.AccountGeneral;
import com.edwin.android.cinerd.entity.json.Movie;
import com.edwin.android.cinerd.util.DatabaseUtil;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviePosterActivity extends AppCompatActivity {

    public static final String TAG = MoviePosterActivity.class.getSimpleName();
    @BindView(R.id.floating_button_movie_menu)
    FloatingActionButton mFloatingButtonMovieMenu;
    @Inject
    MoviePosterPresenter moviePosterPresenter;
    @BindView(R.id.menu_fab_filter)
    FABRevealMenu mFabFilterMenu;
    @BindView(R.id.progress_bar_loading_indicator)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);
        ButterKnife.bind(this);

        try {
            if (mFloatingButtonMovieMenu != null && mFabFilterMenu != null) {
                mFabFilterMenu.bindAncherView(mFloatingButtonMovieMenu);
                mFabFilterMenu.setOnFABMenuSelectedListener(new OnFABMenuSelectedListener() {

                    @Override
                    public void onMenuItemSelected(View view) {
                        int id = (int) view.getTag();
                        switch (id) {
                            case R.id.item_action_movie:
                                moviePosterPresenter.openMovieFilterActivity();
                                break;
                            default:
                                moviePosterPresenter.openTheatersActivity();
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder()
                .applicationModule
                (new ApplicationModule(getApplication())).build();


        if (!DatabaseUtil.existDatabase(this, CineRdDbHelper.DATABASE_NAME)) {
            Log.d(TAG, "Database doesn't exist. Starting to data");
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    MoviePosterActivity.this.mFloatingButtonMovieMenu.setVisibility(View.INVISIBLE);
                    MoviePosterActivity.this.mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids) {

                    MovieCollectorJSON collector = new MovieCollectorJSON(MoviePosterActivity.this);
                    List<Movie> movies = collector.getMovies();
                    Log.d(TAG, "mMovieCollector.getMoviesCollector(): " + movies);
                    ProcessMovies processMovies = new ProcessMovies(MoviePosterActivity.this,
                            collector);
                    processMovies.process(movies);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Log.d(TAG, "Finish manual sync");
                    addFragment(databaseComponent);
                    MoviePosterActivity.this.mProgressBar.setVisibility(View.INVISIBLE);
                    MoviePosterActivity.this.mFloatingButtonMovieMenu.setVisibility(View.VISIBLE);
                }
            }.execute();
        } else {
            AccountGeneral.createSyncAccount(this);
            addFragment(databaseComponent);
        }


    }

    private void addFragment(DatabaseComponent databaseComponent) {
        MoviePosterFragment fragment = (MoviePosterFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_movie_poster);

        Log.d(TAG, "fragment: " + fragment);
        if (fragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = MoviePosterFragment.newInstance();
            fragmentTransaction.add(R.id.fragment_movie_poster, fragment);
            fragmentTransaction.commit();
        }

        Log.d(TAG, "Injection movie poster presenter");
        DaggerMoviePosterComponent.builder().databaseComponent(databaseComponent)
                .moviePosterPresenterModule(new MoviePosterPresenterModule(fragment)).build()
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
