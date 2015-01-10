package com.tabrehab.tabremote;

import android.util.Log;

import com.tabrehab.tabremote.model.RemoteControl;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteControlFetcher {
    private static final String TAG = "RemoteControlFetcher";

    /**
     * Performs the remote control creation handshake.
     *
     * @param createUrl the tabremote: URL containing the handshake data
     * @return the newly created remote's URL; this can be fetched to obtain
     *   remote control data
     */
    public String create(String createUrl) throws IOException {
        assert createUrl.startsWith("tabremote:");
        URL httpUrl = new URL(createUrl.substring(10));
        HttpURLConnection connection =
                (HttpURLConnection)httpUrl.openConnection();
        try {
            connection.setDoOutput(true);
            connection.getOutputStream().close();
            connection.getInputStream().close();

            int httpResponse = connection.getResponseCode();
            if (httpResponse != HttpURLConnection.HTTP_CREATED) {
                Log.d(TAG, "Got error " + httpResponse + " from " + httpUrl);
                return null;
            }

            String remoteUrl = connection.getHeaderField("Location");
            Log.d(TAG, "Created remote " + remoteUrl);
            return remoteUrl;
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Performs a HTTP GET and returns the result as a String.
     * @param url the URL to be obtained
     * @return the returned result
     * @throws IOException
     */
    private String getString(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection connection =
                (HttpURLConnection)httpUrl.openConnection();
        try {
            ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
            InputStream responseStream = connection.getInputStream();

            int httpResponse = connection.getResponseCode();
            if (httpResponse != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Got error " + httpResponse + " from " + httpUrl);
                return null;
            }

            byte[] buffer = new byte[1024];
            while (true) {
                int bytesRead = responseStream.read(buffer);
                if (bytesRead <= 0)
                    break;
                bufferStream.write(buffer, 0, bytesRead);
            }
            bufferStream.close();
            return bufferStream.toString();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Gets a remote control's information, given the remote.
     *
     * @param remoteUrl the remote's main URL
     * @return remote control information
     * @throws IOException
     * @throws JSONException
     */
    public RemoteControl fetch(String remoteUrl)
            throws IOException, JSONException {
        String jsonResponse = getString(remoteUrl);
        Log.d(TAG, "Got remote data " + jsonResponse);
        return new RemoteControl(jsonResponse);
    }
}
