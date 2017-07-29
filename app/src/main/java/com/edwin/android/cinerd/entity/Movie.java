package com.edwin.android.cinerd.entity;

import java.util.Date;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by Edwin Ramirez Ventura on 7/26/2017.
 */

public class Movie implements Searchable {

    private long movieId;
    private String name;
    private short duration;
    private Date releaseDate;
    private String synopsis;

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getDuration() {
        return duration;
    }

    public void setDuration(short duration) {
        this.duration = duration;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return movieId == movie.movieId;

    }

    @Override
    public int hashCode() {
        return (int) (movieId ^ (movieId >>> 32));
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                ", releaseDate=" + releaseDate +
                ", synopsis='" + synopsis + '\'' +
                '}';
    }

    @Override
    public String getTitle() {
        return name;
    }
}