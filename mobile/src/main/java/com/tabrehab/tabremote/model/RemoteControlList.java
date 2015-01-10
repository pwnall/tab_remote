package com.tabrehab.tabremote.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class RemoteControlList {
    private static final String TAG = "RemoteControlList";
    private static final String FILENAME = "remotes.json";

    private Context mAppContext;
    private ArrayList<RemoteControl> mRemotes;
    private RemoteControlListJsonSerializer mSerializer;

    public ArrayList<RemoteControl> getRemotes() {
        return mRemotes;
    }

    /** Saves all the remote control information. */
    public boolean save() {
        try {
            mSerializer.saveRemoteControls(mRemotes);
            Log.d(TAG, "Remotes saved to JSON file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving remotes: ", e);
            return false;
        }
    }

    /** Singleton instance. */
    private static RemoteControlList sRemoteControlList;
    /** Get the singleton instance. */
    public static RemoteControlList get(Context context) {
        if (sRemoteControlList == null) {
            sRemoteControlList =
                    new RemoteControlList(context.getApplicationContext());
        }
        return sRemoteControlList;
    }

    /** Singleton instance constructor. */
    private RemoteControlList(Context appContext) {
        mAppContext = appContext;
        mSerializer = new RemoteControlListJsonSerializer(appContext, FILENAME);
        try {
            mRemotes = mSerializer.loadRemoteControls();
        } catch (Exception e) {
            mRemotes = new ArrayList<RemoteControl>();
            Log.e(TAG, "Error loading remote controls", e);
        }
    }
}