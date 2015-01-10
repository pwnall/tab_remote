package com.tabrehab.tabremote.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/** Information about one remote-controlled widget. */
public class RemoteControl {
    /** The commands supported by the controlled widget. */
    private ArrayList<Command> mCommands;
    /** The name of the device holding the controlled widget. */
    private String mDeviceName;
    /** The name of the controlled widget. */
    private String mWidgetName;
    /** The remote control's main URL. */
    private String mUrl;

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
        mUrl = jsonRemote.getString("url");
        mDeviceName = jsonRemote.getString("device_name");
        mWidgetName = jsonRemote.getString("widget_name");
        mCommands = new ArrayList<>();
        JSONArray jsonCommands = jsonRemote.getJSONArray("commands");
        for (int i = 0; i < jsonCommands.length(); ++i) {
            JSONObject jsonCommand = jsonCommands.getJSONObject(i);
            Command command = new Command(jsonCommand);
            mCommands.add(command);
        }
    }

    /** Dumps remote control information to a JSON-compatible object. */
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonRemote = new JSONObject();
        jsonRemote.put("device_name", mDeviceName);
        jsonRemote.put("widget_name", mWidgetName);
        jsonRemote.put("url", mUrl);
        JSONArray jsonCommands = new JSONArray();
        for (Command command : mCommands) {
            jsonCommands.put(command.toJSON());
        }
        jsonRemote.put("commands", jsonCommands);
        return jsonRemote;
    }

    /** A command that can be sent to a remote controlled widget. */
    public class Command {
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
        public Command(JSONObject jsonObject) throws JSONException {
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
    }

    public ArrayList<Command> getCommands() {
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

    @Override
    public String toString() {
        return mWidgetName + " on " + mDeviceName;
    }
}
