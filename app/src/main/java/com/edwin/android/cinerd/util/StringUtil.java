package com.edwin.android.cinerd.util;

/**
 * Created by Edwin Ramirez Ventura on 8/6/2017.
 */

public final class StringUtil {

    public static final String capitalizeFirstWord(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
