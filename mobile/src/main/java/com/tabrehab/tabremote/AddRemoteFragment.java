package com.tabrehab.tabremote;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tabrehab.tabremote.model.RemoteControl;
import com.tabrehab.tabremote.model.RemoteControlList;

import org.json.JSONException;

import java.io.IOException;

public class AddRemoteFragment extends Fragment {
    private static final String TAG = "AddRemoteFragment";

    private boolean startedFromIntent;
    private String mCreateUrl;
    private RemoteControlList mControlList;

    public static final String CREATE_URL = "create_url";

    private static final int REQUEST_SCAN_BARCODE = 0;

    public static AddRemoteFragment newInstance(String createUrl) {
        AddRemoteFragment instance = new AddRemoteFragment();
        Bundle arguments = new Bundle();
        if (createUrl != null) {
            arguments.putString(CREATE_URL, createUrl);
        }
        instance.setArguments(arguments);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mControlList = RemoteControlList.get(getActivity());
        mCreateUrl = getArguments().getString(CREATE_URL, null);
        setRetainInstance(true);
        if (mCreateUrl != null) {
            startedFromIntent = true;
            new CreateRemoteTask().execute(mCreateUrl);
        } else {
            startBarcodeIntent();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCreateUrl == null) {
            startBarcodeIntent();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_remote,
                container, false);
        return rootView;
    }

    /**
     * Start the barcode scanner intent.
     *
     * If no barcode scanning activity is available, we point the user to the
     * Google Play page of Google's barcode scanner.
     */
    private void startBarcodeIntent() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

        PackageManager packageManager = getActivity().getPackageManager();
        ComponentName scanActivity = intent.resolveActivity(packageManager);
        if (scanActivity != null) {
            startActivityForResult(intent, REQUEST_SCAN_BARCODE);
            return;
        }

        Uri marketUri = Uri.parse(
                "market://details?id=com.google.zxing.client.android");
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);

        Toast toast = Toast.makeText(getActivity(),
                getString(R.string.app_store_barcode_prompt),
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        startActivity(marketIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                mCreateUrl = data.getStringExtra("SCAN_RESULT");
                new CreateRemoteTask().execute(mCreateUrl);
                return;
            }
            // TODO(pwnall): handle cancel
        }
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

            Activity activity = getActivity();
            Intent parentIntent = NavUtils.getParentActivityIntent(activity);
            if (startedFromIntent == false && parentIntent != null &&
                    !NavUtils.shouldUpRecreateTask(activity, parentIntent)) {
                NavUtils.navigateUpTo(activity, parentIntent);
            } else {
                Intent intent = new Intent(activity,
                        RemoteControlListActivity.class);
                startActivity(intent);
            }

            Toast toast = Toast.makeText(getActivity(),
                    activity.getString(R.string.toast_remote_control_added),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
