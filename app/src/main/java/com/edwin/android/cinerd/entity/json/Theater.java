
package com.edwin.android.cinerd.entity.json;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import ir.mirrajabi.searchdialog.core.Searchable;

@Generated("net.hexar.json2pojo")
public class Theater implements Searchable, Parcelable {

    @SerializedName("name")
    private String mName;
    @SerializedName("room")
    private List<Room> mRoom;

    public Theater() {}

    public Theater(String mName) {
        this.mName = mName;
    }

    protected Theater(Parcel in) {
        mName = in.readString();
        mRoom = in.createTypedArrayList(Room.CREATOR);
    }

    public static final Creator<Theater> CREATOR = new Creator<Theater>() {
        @Override
        public Theater createFromParcel(Parcel in) {
            return new Theater(in);
        }

        @Override
        public Theater[] newArray(int size) {
            return new Theater[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeTypedList(mRoom);
    }
}
