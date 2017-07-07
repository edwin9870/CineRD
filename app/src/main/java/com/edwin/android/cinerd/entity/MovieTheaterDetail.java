package com.edwin.android.cinerd.entity;

import java.util.Date;

/**
 * Created by Edwin Ramirez Ventura on 7/7/2017.
 */

public class MovieTheaterDetail {

    private long movieId;
    private long theaterId;
    private long roomId;
    private Date availableDate;
    private short subtitleId;
    private short formatId;
    private short languageId;

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(long theaterId) {
        this.theaterId = theaterId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public short getSubtitleId() {
        return subtitleId;
    }

    public void setSubtitleId(short subtitleId) {
        this.subtitleId = subtitleId;
    }

    public short getFormatId() {
        return formatId;
    }

    public void setFormatId(short formatId) {
        this.formatId = formatId;
    }

    public short getLanguageId() {
        return languageId;
    }

    public void setLanguageId(short languageId) {
        this.languageId = languageId;
    }

    @Override
    public String toString() {
        return "MovieTheaterDetail{" +
                "movieId=" + movieId +
                ", theaterId=" + theaterId +
                ", roomId=" + roomId +
                ", availableDate=" + availableDate +
                ", subtitleId=" + subtitleId +
                ", formatId=" + formatId +
                ", languageId=" + languageId +
                '}';
    }
}
