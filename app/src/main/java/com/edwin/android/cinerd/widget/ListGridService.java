package com.edwin.android.cinerd.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Edwin Ramirez Ventura on 5/27/2017.
 */
public class ListGridService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MovieRemoteViewFactory(this.getApplicationContext());
    }

}
