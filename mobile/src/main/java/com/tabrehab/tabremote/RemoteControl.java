package com.tabrehab.tabremote;

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

    /** Decodes remote control information from a raw HTTP response. */
    public RemoteControl(byte[] raw) {

    }

    public class Command {
        /** The URL to POST to in order to perform the command. */
        private String mUrl;
        /** The command's internal name. */
        private String mName;
        /** The command's label. */
        private String mLabel;

        public Command() {
        }
        public String getmLabel() {
            return mLabel;
        }
        public String getmUrl() {
            return mUrl;
        }
        public String getmName() {
            return mName;
        }
    }

    public ArrayList<Command> getmCommands() {
        return mCommands;
    }
    public String getmDeviceName() {
        return mDeviceName;
    }
    public String getmWidgetName() {
        return mWidgetName;
    }
    public String getmUrl() {
        return mUrl;
    }
}
