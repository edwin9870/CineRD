package com.edwin.android.cinerd.moviedetail;

import com.edwin.android.cinerd.configuration.di.DatabaseComponent;
import com.edwin.android.cinerd.configuration.di.FragmentScoped;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/14/2017.
 */

@FragmentScoped
@Component(dependencies = {DatabaseComponent.class}, modules = {MovieDetailPresenterModule.class})
public interface MovieDetailComponent {

    void inject(MovieDetailActivity activity);
}
