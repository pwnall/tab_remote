package com.tabrehab.tabremote;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tabrehab.tabremote.model.RemoteControl;
import com.tabrehab.tabremote.model.RemoteControlList;

import org.json.JSONException;

import java.io.IOException;


public class AddRemoteActivity extends ActionBarActivity {
    private static final String TAG = "AddRemoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remote);
        if (savedInstanceState == null) {
            String createUrl = getIntent().getDataString();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,
                            PlaceholderFragment.newInstance(createUrl))
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private String mCreateUrl;
        private RemoteControlList mControlList;

        public static PlaceholderFragment newInstance(String createUrl) {
            PlaceholderFragment instance = new PlaceholderFragment();
            instance.mCreateUrl = createUrl;
            return instance;
        }
        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mControlList = RemoteControlList.get(getActivity());
            setRetainInstance(true);
            new CreateRemoteTask().execute(mCreateUrl);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_remote, container, false);
            return rootView;
        }

        private class CreateRemoteTask extends AsyncTask<String, Void, RemoteControl> {
            @Override
            protected RemoteControl doInBackground(String... params) {
                String createUrl = params[0];
                RemoteControlFetcher fetcher = new RemoteControlFetcher();
                try {
                    String remoteUrl = fetcher.create(createUrl);
                    RemoteControl remoteControl = fetcher.fetch(remoteUrl);
                    return remoteControl;
                } catch (IOException e) {
                    Log.e(TAG, "Failed to create remote", e);
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to create remote", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(RemoteControl remoteControl) {
                mControlList.getRemotes().add(remoteControl);
                mControlList.save();
            }
        }
    }
}
