package com.edwin.android.cinerd.entity;

import com.google.gson.annotations.SerializedName;

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
    private Rating rating;
    private String posterUrl;
    private String backdropUrl;
    private String trailerUrl;

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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
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
                ", rating=" + rating +
                ", posterUrl='" + posterUrl + '\'' +
                ", backdropUrl='" + backdropUrl + '\'' +
                ", trailerUrl='" + trailerUrl + '\'' +
                '}';
    }

    @Override
    public String getTitle() {
        return name;
    }
}
