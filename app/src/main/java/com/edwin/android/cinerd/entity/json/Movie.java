
package com.edwin.android.cinerd.entity.json;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Movie implements Parcelable {

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
    @SerializedName("poster_url")
    private String mPosterUrl;
    @SerializedName("backdrop_url")
    private String mBackdropUrl;
    @SerializedName("trailer_url")
    private String mTrailerUrl;


    protected Movie(Parcel in) {
        mGenre = in.createStringArrayList();
        mName = in.readString();
        mRating = in.readParcelable(Rating.class.getClassLoader());
        mSynopsis = in.readString();
        mTheaters = in.createTypedArrayList(Theater.CREATOR);
        mPosterUrl = in.readString();
        mBackdropUrl = in.readString();
        mTrailerUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(mGenre);
        dest.writeString(mName);
        dest.writeParcelable(mRating, flags);
        dest.writeString(mSynopsis);
        dest.writeTypedList(mTheaters);
        dest.writeString(mPosterUrl);
        dest.writeString(mBackdropUrl);
        dest.writeString(mTrailerUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getBackdropUrl() {
        return mBackdropUrl;
    }

    public String getmTrailerUrl() {
        return mTrailerUrl;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mDuration=" + mDuration +
                ", mGenre=" + mGenre +
                ", mName='" + mName + '\'' +
                ", mRating=" + mRating +
                ", mReleaseDate=" + mReleaseDate +
                ", mSynopsis='" + mSynopsis + '\'' +
                ", mTheaters=" + mTheaters +
                ", mPosterUrl='" + mPosterUrl + '\'' +
                ", mBackdropUrl='" + mBackdropUrl + '\'' +
                ", mTrailerUrl='" + mTrailerUrl + '\'' +
                '}';
    }
}
