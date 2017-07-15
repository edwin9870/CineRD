
package com.edwin.android.cinerd.entity;

import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Movie {

    @SerializedName("duration")
    private Short mDuration;
    @SerializedName("genre")
    private List<String> mGenre;
    @SerializedName("name")
    private String mName;
    @SerializedName("rating")
    private Rating mRating;
    @SerializedName("release_date")
    private Date mReleaseDate;
    @SerializedName("synopsis")
    private String mSynopsis;
    @SerializedName("theaters")
    private List<Theater> mTheaters;

    public Short getDuration() {
        return mDuration;
    }

    public List<String> getGenre() {
        return mGenre;
    }

    public String getName() {
        return mName;
    }

    public Rating getRating() {
        return mRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public List<Theater> getTheaters() {
        return mTheaters;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mDuration=" + mDuration +
                ", mGenre=" + mGenre +
                ", mName='" + mName + '\'' +
                ", mRating=" + mRating +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mTheaters=" + mTheaters +
                '}';
    }
}
