
package com.edwin.android.cinerd.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Movies {

    @SerializedName("movies")
    private List<Movie> mMovies;

    public List<Movie> getMovies() {
        return mMovies;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "mMovies=" + mMovies +
                '}';
    }
}
