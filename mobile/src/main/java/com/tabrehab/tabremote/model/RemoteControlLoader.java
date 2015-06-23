package com.tabrehab.tabremote.model;

import android.content.Context;
import android.support.v4.content.CursorLoader;

public class RemoteControlLoader extends CursorLoader {
    public RemoteControlLoader(Context context) {
        super(context, RemoteControlProvider.allRemotesUri(), null, null, null,
                RemoteControlProvider.RemoteColumns.DEVICE_NAME);
    }
}