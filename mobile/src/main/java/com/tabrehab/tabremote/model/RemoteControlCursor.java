package com.tabrehab.tabremote.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.tabrehab.tabremote.model.RemoteControlProvider.RemoteColumns;

public class RemoteControlCursor extends CursorWrapper {
    public RemoteControlCursor(Cursor cursor) {
        super(cursor);
    }

    public RemoteControl remoteControl() {
        if (isBeforeFirst() || isAfterLast())
            return null;

        long id = getLong(getColumnIndex(RemoteColumns._ID));
        String deviceName = getString(
                getColumnIndex(RemoteColumns.DEVICE_NAME));
        String widgetName = getString(
                getColumnIndex(RemoteColumns.WIDGET_NAME));
        String url = getString(getColumnIndex(RemoteColumns.URL));
        return new RemoteControl(id, deviceName, widgetName, url);
    }
}