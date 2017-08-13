package com.edwin.android.cinerd.theater;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.repositories.TheaterRepository;
import com.edwin.android.cinerd.entity.Theater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

/**
 * Created by Edwin Ramirez Ventura on 8/13/2017.
 */

public class TheaterListLoaderCallback implements LoaderManager.LoaderCallbacks<List<Theater>> {

    public static final String TAG = TheaterListLoaderCallback.class.getSimpleName();
    private final TheaterRepository mTheaterRepository;
    private final Date mMinDate;
    private final TheaterMVP.View mView;
    private final TheaterMVP.Presenter mPresenter;
    private Context mContext;

    public TheaterListLoaderCallback(Context mContext,
                                     TheaterMVP.View view,
                                     TheaterMVP.Presenter presenter,
                                     TheaterRepository theaterRepository,
                                     Date minDate) {
        this.mContext = mContext;
        mView = view;
        mPresenter = presenter;
        mTheaterRepository = theaterRepository;
        mMinDate = minDate;
    }

    @Override
    public Loader<List<Theater>> onCreateLoader(int i, Bundle bundle) {
        return new TheaterListLoader(mContext, mTheaterRepository, mMinDate);
    }

    @Override
    public void onLoadFinished(Loader<List<Theater>> loader, List<Theater> theaters) {
        Collections.sort(theaters);
        Log.d(TAG, "Start to showing list of theater dialogs");
        SimpleSearchDialogCompat searchDialogCompat = new SimpleSearchDialogCompat
                (mContext, mContext.getString(R.string.theater_finder_dialog_title),
                        mContext.getString(R.string.theater_finder_dialog_place_holder), null, new ArrayList<Theater>(theaters),
                        new SearchResultListener<Theater>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog, Theater
                                    theaterSearchable, int position) {
                                Toast.makeText(mContext,
                                        theaterSearchable.getTitle(),
                                        Toast.LENGTH_SHORT).show();

                                Log.d(TAG, "Theater selected: " + theaterSearchable.getTitle());
                                int theaterId = theaterSearchable.getTheaterId();
                                mView.setTheaterId(theaterId);
                                mPresenter.setActivityTitle(theaterId);
                                mPresenter.getMovies(theaterId);
                                dialog.dismiss();

                            }
                        });
        searchDialogCompat.getContext().setTheme(R.style.AppTheme_Dialog_Light_DarkText);
        searchDialogCompat.show();
    }

    @Override
    public void onLoaderReset(Loader<List<Theater>> loader) {

    }
}
