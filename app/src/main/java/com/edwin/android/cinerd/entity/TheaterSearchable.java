package com.edwin.android.cinerd.entity;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by Edwin Ramirez Ventura on 7/24/2017.
 */

public class TheaterSearchable implements Searchable {
    private String mTitle;
    private long mTheaterId;

    public TheaterSearchable(String title, long theaterId) {
        mTitle = title;
        mTheaterId = theaterId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public TheaterSearchable setTitle(String title) {
        mTitle = title;
        return this;
    }

    public long getTheaterId() {
        return mTheaterId;
    }

    public void setTheaterId(long theaterId) {
        this.mTheaterId = theaterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TheaterSearchable that = (TheaterSearchable) o;

        return mTitle != null ? mTitle.equals(that.mTitle) : that.mTitle == null;

    }

    @Override
    public int hashCode() {
        return mTitle != null ? mTitle.hashCode() : 0;
    }
}
