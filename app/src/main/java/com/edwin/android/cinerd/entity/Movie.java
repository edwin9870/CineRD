
package com.edwin.android.cinerd.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Movie {

    @SerializedName("duration")
    private Long mDuration;
    @SerializedName("genre")
    private List<String> mGenre;
    @SerializedName("name")
    private String mName;
    @SerializedName("rating")
    private Rating mRating;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("synopsis")
    private String mSynopsis;
    @SerializedName("theaters")
    private List<Theater> mTheaters;

    public Long getDuration() {
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

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public List<Theater> getTheaters() {
        return mTheaters;
    }

}
