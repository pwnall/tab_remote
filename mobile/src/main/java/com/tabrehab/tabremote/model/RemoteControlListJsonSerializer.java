package com.tabrehab.tabremote.model;

import android.content.Context;

import com.tabrehab.tabremote.model.RemoteControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/** The list of remote controls is stored in a JSON file. */
public class RemoteControlListJsonSerializer {
    private Context mContext;
    private String mFileName;

    public RemoteControlListJsonSerializer(Context context, String fileName) {
        mContext = context;
        mFileName = fileName;
    }

    public ArrayList<RemoteControl> loadRemoteControls()
            throws IOException, JSONException {
        ArrayList<RemoteControl> remoteControls =
                new ArrayList<RemoteControl>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonBuffer = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant.
                jsonBuffer.append(line);
            }
            String jsonString = jsonBuffer.toString();
            JSONArray jsonRemotes =
                    (JSONArray)(new JSONTokener(jsonString).nextValue());
            for (int i = 0; i < jsonRemotes.length(); i++) {
                JSONObject jsonRemote = jsonRemotes.getJSONObject(i);
                remoteControls.add(new RemoteControl(jsonRemote));
            }
        } catch (FileNotFoundException e) {
            // No file, no saved remotes.
        } finally {
            if (reader != null)
                reader.close();
        }
        return remoteControls;
    }

    public void saveRemoteControls(ArrayList<RemoteControl> remoteControls)
            throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (RemoteControl remoteControl : remoteControls) {
            array.put(remoteControl.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out =
                    mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }    
}
