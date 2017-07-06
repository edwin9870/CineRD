
package com.edwin.android.cinerd.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
public class Theater {

    @SerializedName("name")
    private String mName;
    @SerializedName("room")
    private List<Room> mRoom;

    public String getName() {
        return mName;
    }

    public List<Room> getRoom() {
        return mRoom;
    }

}
