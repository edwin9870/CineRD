package com.edwin.android.cinerd.data.rest;

import com.edwin.android.cinerd.entity.json.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Edwin Ramirez Ventura on 8/12/2017.
 */

public interface MovieService {

    @GET("/movie/data/{version}")
    Call<Movies> getMovie(@Path("version") String dataVersion);
}
