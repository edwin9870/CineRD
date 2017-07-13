package com.edwin.android.cinerd.movieposter;

import com.edwin.android.cinerd.configuration.di.DatabaseComponent;
import com.edwin.android.cinerd.util.FragmentScoped;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/12/2017.
 */

@FragmentScoped
@Component(dependencies = {DatabaseComponent.class}, modules = {MoviePosterPresenterModule.class})
public interface MoviePosterComponent {
    void inject(MoviePosterActivity activity);
}
