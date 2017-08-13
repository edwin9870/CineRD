package com.edwin.android.cinerd.settings;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Edwin Ramirez Ventura on 8/13/2017.
 */

@Module
public class SettingPresenterModule {
    private final SettingMVP.View mView;

    public SettingPresenterModule(SettingMVP.View mView) {
        this.mView = mView;
    }

    @Provides
    SettingMVP.View provideSettingView(){
        return mView;
    }
}
