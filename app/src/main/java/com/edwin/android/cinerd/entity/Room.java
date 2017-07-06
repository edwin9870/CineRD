
package com.edwin.android.cinerd.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Room {

    @SerializedName("date")
    private String mDate;
    @SerializedName("format")
    private String mFormat;
    @SerializedName("language")
    private String mLanguage;
    @SerializedName("number")
    private String mNumber;
    @SerializedName("subtitle")
    private String mSubtitle;

    public String getDate() {
        return mDate;
    }

    public String getFormat() {
        return mFormat;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getNumber() {
        return mNumber;
    }

    public String getSubtitle() {
        return mSubtitle;
    }

    @Override
    public String toString() {
        return "Room{" +
                "mDate='" + mDate + '\'' +
                ", mFormat='" + mFormat + '\'' +
                ", mLanguage='" + mLanguage + '\'' +
                ", mNumber='" + mNumber + '\'' +
                ", mSubtitle='" + mSubtitle + '\'' +
                '}';
    }
}
