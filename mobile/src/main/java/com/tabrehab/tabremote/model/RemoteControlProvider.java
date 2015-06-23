package com.tabrehab.tabremote.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.sql.SQLException;
import java.util.List;

public class RemoteControlProvider extends ContentProvider {
    /** The provider's authority. */
    public static final String AUTHORITY = "com.tabrehab.tabremote.provider";
    /** URI matcher ID for all the remote controls in the system. */
    public static final int ALL_REMOTES = 1;
    /** URI matcher ID for one remote control in the system. */
    public static final int ONE_REMOTE = 2;
    /** URI matcher ID for all the commands of a remote control. */
    public static final int REMOTE_COMMANDS = 3;

    /** Routing table for the provider's URLS. */
    private static final UriMatcher mUriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        mUriMatcher.addURI(AUTHORITY, "remotes", ALL_REMOTES);
        mUriMatcher.addURI(AUTHORITY, "remotes/#", ALL_REMOTES);
        mUriMatcher.addURI(AUTHORITY, "remotes/#/commands", REMOTE_COMMANDS);
    }

    /** The URI for getting all remote controls. */
    public static Uri allRemotesUri() {
        return Uri.parse("content://" + AUTHORITY + "/remotes");
    }

    /** The URI for getting all the commands of a remote control. */
    public static Uri remoteCommandsUri(long remoteId) {
        return Uri.parse("content://" + AUTHORITY + "/remotes/" + remoteId +
                "/commands");
    }

    /** Column names for the remote controls table. */
    public interface RemoteColumns extends BaseColumns {
        public static final String DEVICE_NAME = "device_name";
        public static final String WIDGET_NAME = "widget_name";
        public static final String URL = "url";
        public static final String[] ALL = {
                _ID, DEVICE_NAME, WIDGET_NAME, URL
        };
    }
    public static final String REMOTE_CONTROLS_TABLE = "remotes";

    /** Column names for the commands table. */
    public interface CommandColumns extends BaseColumns {
        public static final String REMOTE_ID = "remote_id";
        public static final String NAME = "name";
        public static final String LABEL = "label";
        public static final String URL = "url";
        public static final String[] ALL = {
                _ID, REMOTE_ID, NAME, LABEL, URL
        };
    }
    public static final String COMMANDS_TABLE = "commands";

    private TabRemoteDatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new TabRemoteDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mOpenHelper.getReadableDatabase();
        switch (mUriMatcher.match(uri)) {
            case ALL_REMOTES:
                if (projection == null) projection = RemoteColumns.ALL;
                return database.query(REMOTE_CONTROLS_TABLE, projection,
                        selection, selectionArgs, null, null, sortOrder);
            case ONE_REMOTE: {
                String remoteId = uri.getLastPathSegment();
                if (projection == null) projection = RemoteColumns.ALL;
                return database.query(REMOTE_CONTROLS_TABLE, projection,
                        addColumnToSelection(RemoteColumns._ID, selection),
                        addColumnToSelectionArgs(remoteId, selectionArgs),
                        null, null, sortOrder);
            }
            case REMOTE_COMMANDS: {
                List<String> uriPathSegments = uri.getPathSegments();
                String remoteId = uriPathSegments.get(
                        uriPathSegments.size() - 2);
                if (projection == null) projection = RemoteColumns.ALL;
                return database.query(COMMANDS_TABLE, projection,
                        addColumnToSelection(CommandColumns.REMOTE_ID,
                                selection),
                        addColumnToSelectionArgs(remoteId, selectionArgs),
                        null, null, sortOrder);
            }
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    /** Adds a column name to a SQL query. */
    private String addColumnToSelection(String columnName, String selection) {
        if (selection == null)
            return columnName + " = ?";
        return columnName + " = ?" + "(" + selection + ")";
    }
    /** Adds a column value to a list of SQL query arguments. */
    private String[] addColumnToSelectionArgs(String columnValue,
                                              String[] selectionArgs) {
        if (selectionArgs == null)
            return new String[] { columnValue };

        String[] newSelectionArgs = new String[selectionArgs.length + 1];
        newSelectionArgs[0] = columnValue;
        System.arraycopy(selectionArgs, 0, newSelectionArgs, 1,
                selectionArgs.length);
        return newSelectionArgs;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        return doInsert(uri, values, database);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] valuesArray) {
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        int count = 0;
        try {
            database.beginTransaction();
            for (ContentValues values : valuesArray) {
                doInsert(uri, values, database);
                count += 1;
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return count;
    }

    /** Common code between insert and bulkInsert. */
    private Uri doInsert(Uri uri, ContentValues values,
                         SQLiteDatabase database) {
        switch (mUriMatcher.match(uri)) {
            case ALL_REMOTES: {
                long id = database.insert(REMOTE_CONTROLS_TABLE, "", values);
                return ContentUris.withAppendedId(uri, id);
            }
            case REMOTE_COMMANDS: {
                List<String> uriPathSegments = uri.getPathSegments();
                String remoteId = uriPathSegments.get(
                        uriPathSegments.size() - 2);
                values.put(CommandColumns.REMOTE_ID, remoteId);
                long id = database.insert(REMOTE_CONTROLS_TABLE, "", values);
                return ContentUris.withAppendedId(uri, id);
            }
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        switch (mUriMatcher.match(uri)) {
            case ONE_REMOTE: {
                String remoteId = uri.getLastPathSegment();
                return database.delete(REMOTE_CONTROLS_TABLE,
                        addColumnToSelection(RemoteColumns._ID, selection),
                        addColumnToSelectionArgs(remoteId, selectionArgs));
            }
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

}
