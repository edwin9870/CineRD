package com.edwin.android.cinerd.moviefinder;

import com.edwin.android.cinerd.configuration.di.DatabaseComponent;
import com.edwin.android.cinerd.configuration.di.FragmentScoped;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/27/2017.
 */

@FragmentScoped
@Component(dependencies = {DatabaseComponent.class}, modules = {MovieFinderPresenterModule.class})
public interface MovieFinderComponent {
    void inject(MovieFinderActivity activity);
}
