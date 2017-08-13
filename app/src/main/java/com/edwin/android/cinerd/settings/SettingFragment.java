package com.edwin.android.cinerd.settings;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwin.android.cinerd.R;

public class SettingFragment extends Fragment implements SettingMVP.View {

    public static final String TAG = SettingFragment.class.getSimpleName();
    private SettingMVP.Presenter mPresener;

    public SettingFragment() {}

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setPresenter(SettingMVP.Presenter presenter) {
        Log.d(TAG, "Setting presenter");
        mPresener = presenter;
    }
}
