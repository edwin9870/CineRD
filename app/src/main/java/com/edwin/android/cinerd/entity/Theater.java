package com.edwin.android.cinerd.entity;

import android.support.annotation.NonNull;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by Edwin Ramirez Ventura on 7/24/2017.
 */

public class Theater implements Searchable, Comparable<Theater> {
    private String mTitle;
    private int mTheaterId;

    public Theater(String title, int theaterId) {
        mTitle = title;
        mTheaterId = theaterId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public Theater setTitle(String title) {
        mTitle = title;
        return this;
    }

    public int getTheaterId() {
        return mTheaterId;
    }

    public void setTheaterId(int theaterId) {
        this.mTheaterId = theaterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Theater that = (Theater) o;

        return mTitle != null ? mTitle.equals(that.mTitle) : that.mTitle == null;

    }

    @Override
    public int hashCode() {
        return mTitle != null ? mTitle.hashCode() : 0;
    }

    @Override
    public int compareTo(@NonNull Theater theater) {
        return this.mTitle.compareTo(theater.getTitle());
    }
}
