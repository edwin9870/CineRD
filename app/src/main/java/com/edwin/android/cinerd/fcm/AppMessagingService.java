package com.edwin.android.cinerd.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Edwin Ramirez Ventura on 8/12/2017.
 */

public class AppMessagingService extends FirebaseMessagingService {

    public static final String TAG = AppMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Message received...");
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
