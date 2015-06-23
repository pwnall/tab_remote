package com.tabrehab.tabremote.model;

import org.json.JSONException;
import org.json.JSONObject;

/** A command that can be sent to a remote controlled widget. */
public class RemoteCommand {
    /** The URL to POST to in order to perform the command. */
    private String mUrl;
    /** The command's internal name. */
    private String mName;
    /** The command's label. */
    private String mLabel;

    /**
     * Creates a command from a JSON-encoded object.
     * @param jsonObject
     */
    public RemoteCommand(JSONObject jsonObject) throws JSONException {
        mName = jsonObject.getString("name");
        mLabel = jsonObject.getString("label");
        mUrl = jsonObject.getString("url");
    }

    /** Dumps a command to a JSON-compatible object. */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonCommand = new JSONObject();
        jsonCommand.put("name", mName);
        jsonCommand.put("label", mLabel);
        jsonCommand.put("url", mUrl);
        return jsonCommand;
    }
    public String getLabel() {
        return mLabel;
    }
    public String getUrl() {
        return mUrl;
    }
    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mLabel;
    }
}
