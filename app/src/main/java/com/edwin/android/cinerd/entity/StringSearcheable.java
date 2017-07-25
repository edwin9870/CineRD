package com.edwin.android.cinerd.entity;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by Edwin Ramirez Ventura on 7/24/2017.
 */

public class StringSearcheable implements Searchable {
    private String mTitle;

    public StringSearcheable(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public StringSearcheable setTitle(String title) {
        mTitle = title;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringSearcheable that = (StringSearcheable) o;

        return mTitle != null ? mTitle.equals(that.mTitle) : that.mTitle == null;

    }

    @Override
    public int hashCode() {
        return mTitle != null ? mTitle.hashCode() : 0;
    }
}
