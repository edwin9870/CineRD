
package com.edwin.android.cinerd.entity.json;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Rating implements Parcelable {

    @SerializedName("imdb")
    private String mImdb;
    @SerializedName("rottentomatoes")
    private String mRottentomatoes;

    protected Rating(Parcel in) {
        mImdb = in.readString();
        mRottentomatoes = in.readString();
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

    public String getImdb() {
        return mImdb;
    }

    public String getRottentomatoes() {
        return mRottentomatoes;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "mImdb='" + mImdb + '\'' +
                ", mRottentomatoes='" + mRottentomatoes + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mImdb);
        parcel.writeString(mRottentomatoes);
    }
}
