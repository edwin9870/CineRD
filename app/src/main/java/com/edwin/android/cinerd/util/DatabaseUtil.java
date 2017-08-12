package com.edwin.android.cinerd.util;

import android.content.Context;

import java.io.File;

/**
 * Created by Edwin Ramirez Ventura on 8/9/2017.
 */

public final class DatabaseUtil {

    public static boolean existDatabase(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
