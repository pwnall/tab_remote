package com.tabrehab.tabremote;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

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

            setRetainInstance(true);
            new CreateRemoteTask().execute(mCreateUrl);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_remote, container, false);
            return rootView;
        }

        private class CreateRemoteTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... url) {
                try {
                    String result = new RemoteControlFetcher().create(url[0]);
                } catch (IOException e) {
                    Log.e(TAG, "Failed to create remote", e);
                }
                return null;
            }
        }
    }
}
