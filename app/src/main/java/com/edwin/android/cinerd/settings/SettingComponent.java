package com.edwin.android.cinerd.settings;

import dagger.Component;

/**
 * Created by Edwin Ramirez Ventura on 8/13/2017.
 */

@Component(modules = {SettingPresenterModule.class})
public interface SettingComponent {
    void inject(SettingActivity activity);
}
