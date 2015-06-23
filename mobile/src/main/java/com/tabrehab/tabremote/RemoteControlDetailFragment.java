package com.tabrehab.tabremote;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tabrehab.tabremote.model.RemoteCommand;
import com.tabrehab.tabremote.model.RemoteControl;
import com.tabrehab.tabremote.model.RemoteControlList;

import java.io.IOException;

/**
 * A fragment representing a single RemoteControl detail screen.
 * This fragment is either contained in a {@link RemoteControlListActivity}
 * in two-pane mode (on tablets) or a {@link RemoteControlDetailActivity}
 * on handsets.
 */
public class RemoteControlDetailFragment extends Fragment {
    private static final String TAG = "RemoteControlDetailFragment";

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private RemoteControlList mRemoteControlList;
    private RemoteControl mRemoteControl;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RemoteControlDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mRemoteControlList = RemoteControlList.get(getActivity());
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            int remoteIndex = Integer.parseInt(
                    getArguments().getString(ARG_ITEM_ID));
            mRemoteControl = mRemoteControlList.getRemotes().get(remoteIndex);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.remote_control_detail_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_remotecontrol_detail,
                container, false);


        TextView titleView = (TextView)rootView.findViewById(R.id.remotecontrol_detail_title);
        ListView listView = (ListView)rootView.findViewById(R.id.remotecontrol_detail_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Dispatch the command.
                RemoteCommand command = mRemoteControl.getCommands().get(position);
                String commandUrl = command.getUrl();
                new SendCommandTask().execute(commandUrl);
            }
        });
        if (mRemoteControl != null) {
            getActivity().setTitle(mRemoteControl.getWidgetName());
            titleView.setText("on " + mRemoteControl.getDeviceName());
            listView.setAdapter(new ArrayAdapter<RemoteCommand>(
                    getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    android.R.id.text1,
                    mRemoteControl.getCommands()));
        }

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_remove_remote:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SendCommandTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String commandUrl = params[0];
            RemoteControlFetcher fetcher = new RemoteControlFetcher();
            try {
                fetcher.sendCommand(commandUrl);
                return null;
            } catch (IOException e) {
                Log.e(TAG, "Failed to send command", e);
            }
            return null;
        }
    }
}
