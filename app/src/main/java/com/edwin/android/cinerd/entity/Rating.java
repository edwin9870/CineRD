
package com.edwin.android.cinerd.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Rating {

    @SerializedName("imdb")
    private String mImdb;
    @SerializedName("rottentomatoes")
    private String mRottentomatoes;

    public String getImdb() {
        return mImdb;
    }

    public String getRottentomatoes() {
        return mRottentomatoes;
    }

}
