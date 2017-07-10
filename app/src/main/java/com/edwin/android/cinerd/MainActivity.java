package com.edwin.android.cinerd;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.data.adapters.AccountGeneral;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view_movie_poster)
    RecyclerView recyclerViewMoviePoster;
    @BindView(R.id.floating_button_movie_menu)
    FloatingActionButton floatingButtonMovieMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerDatabaseComponent.builder().applicationModule(new ApplicationModule(getApplication
                ())).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AccountGeneral.createSyncAccount(this);
    }


}
