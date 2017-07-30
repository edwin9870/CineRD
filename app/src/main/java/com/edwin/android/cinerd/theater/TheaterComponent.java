package com.edwin.android.cinerd.theater;

import com.edwin.android.cinerd.configuration.di.DatabaseComponent;
import com.edwin.android.cinerd.configuration.di.FragmentScoped;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 7/30/2017.
 */

@FragmentScoped
@Component(dependencies = {DatabaseComponent.class}, modules = {TheaterPresenterModule.class})
public interface TheaterComponent {
    void inject(TheaterActivity activity);
}
