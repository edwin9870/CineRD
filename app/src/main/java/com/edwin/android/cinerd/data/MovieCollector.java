package com.edwin.android.cinerd.data;

import com.edwin.android.cinerd.entity.json.Movie;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 7/9/2017.
 */

public interface MovieCollector {

    List<Movie> getMovies(boolean lightVersion);
}
