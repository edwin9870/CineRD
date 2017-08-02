package com.edwin.android.cinerd.movieposter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.configuration.di.DatabaseComponent;
import com.edwin.android.cinerd.data.adapters.AccountGeneral;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.view.FABRevealMenu;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.edwin.android.cinerd.util.ActivityUtil.parents;

public class MoviePosterActivity extends AppCompatActivity {

    public static final String TAG = MoviePosterActivity.class.getSimpleName();
    @BindView(R.id.floating_button_movie_menu)
    FloatingActionButton floatingButtonMovieMenu;

    @Inject
    MoviePosterPresenter moviePosterPresenter;
    @BindView(R.id.menu_fab_filter)
    FABRevealMenu mFabFilterMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_poster);
        ButterKnife.bind(this);

        try {
            if (floatingButtonMovieMenu != null && mFabFilterMenu != null) {
                mFabFilterMenu.bindAncherView(floatingButtonMovieMenu);
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

        AccountGeneral.createSyncAccount(this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MoviePosterFragment fragment = MoviePosterFragment.newInstance();

        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().applicationModule
                (new
                ApplicationModule(getApplication())).build();
        DaggerMoviePosterComponent.builder().databaseComponent(databaseComponent)
                .moviePosterPresenterModule(new MoviePosterPresenterModule(fragment)).build()
                .inject(this);


        fragmentTransaction.add(R.id.fragment_movie_poster, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        parents.push(getClass());
    }
}
