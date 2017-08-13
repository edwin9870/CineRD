package com.edwin.android.cinerd.settings;

import javax.inject.Inject;

/**
 * Created by Edwin Ramirez Ventura on 8/13/2017.
 */

public class SettingPresenter implements SettingMVP.Presenter {

    private final SettingMVP.View mView;

    @Inject
    public SettingPresenter(SettingMVP.View view) {
        mView = view;
    }

    @Inject
    public void setupPresenter() {
        mView.setPresenter(this);
    }
}
