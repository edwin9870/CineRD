
package com.edwin.android.cinerd.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Movies implements Parcelable {

    @SerializedName("movies")
    private List<Movie> mMovies;

    protected Movies(Parcel in) {
        mMovies = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public List<Movie> getMovies() {
        return mMovies;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "mMovies=" + mMovies +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mMovies);
    }
}
