
package com.edwin.android.cinerd.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import ir.mirrajabi.searchdialog.core.Searchable;

@Generated("net.hexar.json2pojo")
public class Theater implements Searchable {

    @SerializedName("name")
    private String mName;
    @SerializedName("room")
    private List<Room> mRoom;

    public Theater() {}

    public Theater(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public List<Room> getRoom() {
        return mRoom;
    }

    @Override
    public String toString() {
        return "Theater{" +
                "mName='" + mName + '\'' +
                ", mRoom=" + mRoom +
                '}';
    }

    @Override
    public String getTitle() {
        return mName;
    }
}
