package com.edwin.android.cinerd.theater;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwin.android.cinerd.R;


public class TheaterFragment extends Fragment implements  TheaterMVP.View{


    public static final String TAG = TheaterFragment.class.getSimpleName();
    private TheaterMVP.Presenter mPresenter;

    public TheaterFragment() {
    }

    public static TheaterFragment newInstance() {
        TheaterFragment fragment = new TheaterFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_theater, container, false);
    }

    @Override
    public void setPresenter(TheaterMVP.Presenter presenter) {
        Log.d(TAG, "Setting mPresenter");
        mPresenter = presenter;
    }
}
