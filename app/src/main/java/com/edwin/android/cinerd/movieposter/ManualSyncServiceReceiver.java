package com.edwin.android.cinerd.movieposter;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Edwin Ramirez Ventura on 8/12/2017.
 */

public class ManualSyncServiceReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public ManualSyncServiceReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        this.mReceiver = receiver;
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }


    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }
}
