package com.edwin.android.cinerd.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.edwin.android.cinerd.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingFragment extends PreferenceFragment implements SettingMVP.View,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = SettingFragment.class.getSimpleName();
    public static final String NEW_MOVIES_NOTIFICATION_KEY = "new_movies_notification_key";
    private SettingMVP.Presenter mPresener;


    public SettingFragment() {
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.movie_poster_settings);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void setPresenter(SettingMVP.Presenter presenter) {
        Log.d(TAG, "Setting presenter");
        mPresener = presenter;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case NEW_MOVIES_NOTIFICATION_KEY:
                boolean isNotificationActivated = sharedPreferences.getBoolean
                        (NEW_MOVIES_NOTIFICATION_KEY, getResources().getBoolean(R.bool
                                .default_receive_new_movies_notification));
                Log.d(TAG, "Key notification changed to: " + isNotificationActivated);
                if (isNotificationActivated) {
                    FirebaseMessaging.getInstance().subscribeToTopic(NEW_MOVIES_NOTIFICATION_KEY);
                    Log.d(TAG, "Subscribed to " + NEW_MOVIES_NOTIFICATION_KEY + " topic");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(NEW_MOVIES_NOTIFICATION_KEY);
                    Log.d(TAG, "Remove subscription to " + NEW_MOVIES_NOTIFICATION_KEY + " topic");
                }
        }
    }
}
