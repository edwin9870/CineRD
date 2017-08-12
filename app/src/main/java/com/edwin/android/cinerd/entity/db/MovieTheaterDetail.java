package com.edwin.android.cinerd.entity.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Edwin Ramirez Ventura on 7/24/2017.
 */

public class MovieTheaterDetail implements Parcelable {

    private Long movieId;
    private Short roomId;
    private Integer theaterId;
    private Short subtitleId;
    private Date availableDate;
    private Short formatId;
    private Short languageId;


    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Short getRoomId() {
        return roomId;
    }

    public void setRoomId(Short roomId) {
        this.roomId = roomId;
    }

    public Integer getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Integer theaterId) {
        this.theaterId = theaterId;
    }

    public Short getSubtitleId() {
        return subtitleId;
    }

    public void setSubtitleId(Short subtitleId) {
        this.subtitleId = subtitleId;
    }

    public Short getFormatId() {
        return formatId;
    }

    public void setFormatId(Short formatId) {
        this.formatId = formatId;
    }

    public Short getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Short languageId) {
        this.languageId = languageId;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieTheaterDetail that = (MovieTheaterDetail) o;

        if (!movieId.equals(that.movieId)) return false;
        if (!roomId.equals(that.roomId)) return false;
        if (!theaterId.equals(that.theaterId)) return false;
        if (subtitleId != null ? !subtitleId.equals(that.subtitleId) : that.subtitleId != null)
            return false;
        if (!formatId.equals(that.formatId)) return false;
        return languageId.equals(that.languageId);

    }

    @Override
    public int hashCode() {
        int result = movieId.hashCode();
        result = 31 * result + roomId.hashCode();
        result = 31 * result + theaterId.hashCode();
        result = 31 * result + (subtitleId != null ? subtitleId.hashCode() : 0);
        result = 31 * result + formatId.hashCode();
        result = 31 * result + languageId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MovieTheaterDetail{" +
                "movieId=" + movieId +
                ", roomId=" + roomId +
                ", theaterId=" + theaterId +
                ", subtitleId=" + subtitleId +
                ", availableDate=" + availableDate +
                ", formatId=" + formatId +
                ", languageId=" + languageId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.movieId);
        dest.writeValue(this.roomId);
        dest.writeValue(this.theaterId);
        dest.writeValue(this.subtitleId);
        dest.writeLong(this.availableDate != null ? this.availableDate.getTime() : -1);
        dest.writeValue(this.formatId);
        dest.writeValue(this.languageId);
    }

    public MovieTheaterDetail() {
    }

    protected MovieTheaterDetail(Parcel in) {
        this.movieId = (Long) in.readValue(Long.class.getClassLoader());
        this.roomId = (Short) in.readValue(Short.class.getClassLoader());
        this.theaterId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.subtitleId = (Short) in.readValue(Short.class.getClassLoader());
        long tmpAvailableDate = in.readLong();
        this.availableDate = tmpAvailableDate == -1 ? null : new Date(tmpAvailableDate);
        this.formatId = (Short) in.readValue(Short.class.getClassLoader());
        this.languageId = (Short) in.readValue(Short.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieTheaterDetail> CREATOR = new Parcelable
            .Creator<MovieTheaterDetail>() {
        @Override
        public MovieTheaterDetail createFromParcel(Parcel source) {
            return new MovieTheaterDetail(source);
        }

        @Override
        public MovieTheaterDetail[] newArray(int size) {
            return new MovieTheaterDetail[size];
        }
    };
}
