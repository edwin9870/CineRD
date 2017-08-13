package com.edwin.android.cinerd.theater;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.edwin.android.cinerd.data.repositories.TheaterRepository;
import com.edwin.android.cinerd.entity.Theater;

import java.util.Date;
import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 8/13/2017.
 */

public class TheaterListLoader extends AsyncTaskLoader<List<Theater>> {

    public static final String TAG = TheaterListLoader.class.getSimpleName();
    private final Context mContext;
    private final TheaterRepository mTheaterRepository;
    private final Date mMinDate;
    private List<Theater> mTheaters;

    public TheaterListLoader(Context context, TheaterRepository theaterRepository, Date minDate) {
        super(context);
        mContext = context;
        mTheaterRepository = theaterRepository;
        mMinDate = minDate;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "mTheaters value: " + mTheaters);
        if (mTheaters != null) {
            deliverResult(mTheaters);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Theater> loadInBackground() {
        return mTheaterRepository.getAllTheatersByMinDate(mMinDate);
    }

    @Override
    public void deliverResult(List<Theater> theaters) {
        mTheaters = theaters;
        super.deliverResult(theaters);
    }
}
