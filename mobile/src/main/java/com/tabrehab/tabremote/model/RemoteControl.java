package com.tabrehab.tabremote.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/** Information about one remote-controlled widget. */
public class RemoteControl {
    /** The commands supported by the controlled widget. */
    private ArrayList<RemoteCommand> mCommands;
    /** The name of the device holding the controlled widget. */
    private String mDeviceName;
    /** The name of the controlled widget. */
    private String mWidgetName;
    /** The remote control's main URL. */
    private String mUrl;
    /** The remote control's database ID. */
    private long mId;

    /** Decodes remote control information from a JSON String.
     *
     * @param json JSON-encoded string containing the remote control information
     * @throws JSONException
     */
    public RemoteControl(String json) throws JSONException {
        this((JSONObject) (new JSONTokener(json).nextValue()));
    }

    /** Loads remote control information from a JSON-parsed object.
     *
     * @param jsonRemote JSON-pasrsed remote control information
     * @throws JSONException
     */
    public RemoteControl(JSONObject jsonRemote) throws JSONException {
        mId = -1;
        mUrl = jsonRemote.getString("url");
        mDeviceName = jsonRemote.getString("device_name");
        mWidgetName = jsonRemote.getString("widget_name");
        mCommands = new ArrayList<>();
        JSONArray jsonCommands = jsonRemote.getJSONArray("commands");
        for (int i = 0; i < jsonCommands.length(); ++i) {
            JSONObject jsonCommand = jsonCommands.getJSONObject(i);
            RemoteCommand command = new RemoteCommand(jsonCommand);
            mCommands.add(command);
        }
    }

    /**
     * Creates remote control information.
     *
     * @param id the database ID for the remote control
     * @param deviceName the name of the device holding the controlled widget
     * @param widgetName the name of the controlled widget
     * @param url the remote control's main URL
     */
    public RemoteControl(long id, String deviceName, String widgetName,
                         String url) {
       mId = id;
       mDeviceName = deviceName;
       mWidgetName = widgetName;
       mUrl = url;
    }

    /** Dumps remote control information to a JSON-compatible object. */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonRemote = new JSONObject();
        jsonRemote.put("device_name", mDeviceName);
        jsonRemote.put("widget_name", mWidgetName);
        jsonRemote.put("url", mUrl);
        JSONArray jsonCommands = new JSONArray();
        for (RemoteCommand command : mCommands) {
            jsonCommands.put(command.toJSON());
        }
        jsonRemote.put("commands", jsonCommands);
        return jsonRemote;
    }

    public ArrayList<RemoteCommand> getCommands() {
        return mCommands;
    }
    public String getDeviceName() {
        return mDeviceName;
    }
    public String getWidgetName() {
        return mWidgetName;
    }
    public String getUrl() {
        return mUrl;
    }
    public long getId() { return mId; }

    @Override
    public String toString() {
        return mWidgetName + " on " + mDeviceName;
    }
}
