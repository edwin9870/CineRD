package com.edwin.android.cinerd.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 8/12/2017.
 */

public class ActivityUtil {

    public static boolean isAppRunning(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(context.getApplicationContext().getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
