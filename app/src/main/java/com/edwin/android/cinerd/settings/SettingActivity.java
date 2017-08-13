package com.edwin.android.cinerd.settings;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.moviedetail.MovieDetailFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {

    @Inject
    SettingPresenter mPresenter;
    @BindView(R.id.fragment_settings)
    FrameLayout mSettingsFragmentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        SettingFragment fragment = (SettingFragment) getFragmentManager().findFragmentById(R.id.fragment_settings);

        if(fragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = SettingFragment.newInstance();

            fragmentTransaction.add(R.id.fragment_settings, fragment);
            fragmentTransaction.commit();
        }

        DaggerSettingComponent.builder().settingPresenterModule(new SettingPresenterModule
                (fragment)).build().inject(this);

    }
}
