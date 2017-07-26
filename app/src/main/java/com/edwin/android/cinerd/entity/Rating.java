package com.edwin.android.cinerd.entity;

/**
 * Created by Edwin Ramirez Ventura on 7/26/2017.
 */

public class Rating {
    private long movieId;
    private String rottenTomatoes;
    private String imdb;

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getRottenTomatoes() {
        return rottenTomatoes;
    }

    public void setRottenTomatoes(String rottenTomatoes) {
        this.rottenTomatoes = rottenTomatoes;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "movieId=" + movieId +
                ", rottenTomatoes='" + rottenTomatoes + '\'' +
                ", imdb='" + imdb + '\'' +
                '}';
    }
}
