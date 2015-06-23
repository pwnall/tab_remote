package com.tabrehab.tabremote.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.tabrehab.tabremote.model.RemoteControlProvider.*;

class TabRemoteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RemoteControls";
    private static final Integer DATABASE_VERSION = 1;

    public TabRemoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String remotesSql = "CREATE TABLE " + REMOTE_CONTROLS_TABLE + " ("
                + RemoteColumns.DEVICE_NAME + " TEXT NOT NULL, "
                + RemoteColumns.WIDGET_NAME + " TEXT NOT NULL, "
                + RemoteColumns.URL + " TEXT NOT NULL, "
                + RemoteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT);";
        final String commandsSql = "CREATE TABLE " + COMMANDS_TABLE + " ("
                + CommandColumns.REMOTE_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + CommandColumns.REMOTE_ID + ") REFERENCES"
                    + REMOTE_CONTROLS_TABLE + "(" + RemoteColumns._ID + ") "
                    + " ON DELETE CASCADE, "
                + CommandColumns.NAME + " TEXT NOT NULL, "
                + CommandColumns.LABEL + " TEXT NOT NULL, "
                + CommandColumns.URL + " TEXT NOT NULL;"
                + CommandColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT);";
        db.execSQL(remotesSql + commandsSql);

        final String indexRemoteUrlsSql = "CREATE INDEX _remote_urls_idx ON "
                + REMOTE_CONTROLS_TABLE + " ("
                + RemoteColumns.URL + " ASC "
                + ");";
        final String indexRemoteNamesSql = "CREATE INDEX _remote_names_idx ON "
                + REMOTE_CONTROLS_TABLE + " ("
                + RemoteColumns.DEVICE_NAME + " ASC "
                + ", " + RemoteColumns.WIDGET_NAME + " ASC "
                + ");";
        final String indexCommandNamesSql =
                "CREATE INDEX _command_names_idx ON "
                + COMMANDS_TABLE + " ("
                + CommandColumns.REMOTE_ID + " ASC "
                + ", " + CommandColumns.NAME + " ASC "
                + ");";
        db.execSQL(indexRemoteUrlsSql + indexRemoteNamesSql +
                indexCommandNamesSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
