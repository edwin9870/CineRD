package com.edwin.android.cinerd.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Edwin Ramirez Ventura on 8/3/2017.
 */

public final class ImageUtil {

    public static final int IMAGE_QUALITY = 100;
    public static final String IMAGES_DIRECTORY_NAME = "images";

    public static Bitmap getBitmapFromURL(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    public static String getFileNameFromURL(URL url) {
        return url.toString().substring(url.toString().lastIndexOf('/') + 1);
    }

    @Nullable
    public static String saveImageFromURL(Context context, URL url) {
        Bitmap image = getBitmapFromURL(url);
        String fileName = getFileNameFromURL(url);
        FileOutputStream outputStream;

        ContextWrapper cw = new ContextWrapper(context);
        final File imagesDirectory = cw.getDir(IMAGES_DIRECTORY_NAME, Context.MODE_PRIVATE);


        try {
            File imageFile = new File(imagesDirectory, fileName);
            outputStream = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream);
            outputStream.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getImageFile(Context context, String fileName) {
        ContextWrapper cw = new ContextWrapper(context);
        final File imagesDirectory = cw.getDir(IMAGES_DIRECTORY_NAME, Context.MODE_PRIVATE);
        return new File(imagesDirectory, fileName);
    }
}
