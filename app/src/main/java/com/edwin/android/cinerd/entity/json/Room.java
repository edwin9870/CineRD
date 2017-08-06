
package com.edwin.android.cinerd.entity.json;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Generated("net.hexar.json2pojo")
public class Room implements Parcelable {

    @SerializedName("date")
    private Date mDate;
    @SerializedName("format")
    private String mFormat;
    @SerializedName("language")
    private String mLanguage;
    @SerializedName("number")
    private String mNumber;
    @SerializedName("subtitle")
    private String mSubtitle;

    public Room() {}

    protected Room(Parcel in) {
        mFormat = in.readString();
        mLanguage = in.readString();
        mNumber = in.readString();
        mSubtitle = in.readString();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public Date getDate() {
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

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public void setmFormat(String mFormat) {
        this.mFormat = mFormat;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }

    public void setmSubtitle(String mSubtitle) {
        this.mSubtitle = mSubtitle;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFormat);
        parcel.writeString(mLanguage);
        parcel.writeString(mNumber);
        parcel.writeString(mSubtitle);
    }
}
