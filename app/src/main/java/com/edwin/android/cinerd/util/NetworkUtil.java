package com.edwin.android.cinerd.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.edwin.android.cinerd.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Edwin Ramirez Ventura on 8/12/2017.
 */

public final class NetworkUtil {

    public static final String TAG = NetworkUtil.class.getSimpleName();

    public final static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getMovieData(boolean lightVersion) throws IOException {
        URL url;
        if (lightVersion) {
            url = new URL(BuildConfig.HOST_URL + "/" + BuildConfig.MOVIE_JSON_LIGHT_VERSION);
        } else {
            url = new URL(BuildConfig.HOST_URL + "/" + BuildConfig.MOVIE_JSON_FULL_VERSION);
        }

        Log.d(TAG, "Downloading data from url: " + url);
        String jsonData = getResponseFromHttpUrl(url);
        Log.d(TAG, "Data downloaded");
        return jsonData;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
