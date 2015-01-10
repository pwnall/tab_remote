package com.tabrehab.tabremote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tabrehab.tabremote.model.RemoteControl;
import com.tabrehab.tabremote.model.RemoteControlList;

/**
 * A fragment representing a single RemoteControl detail screen.
 * This fragment is either contained in a {@link RemoteControlListActivity}
 * in two-pane mode (on tablets) or a {@link RemoteControlDetailActivity}
 * on handsets.
 */
public class RemoteControlDetailFragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_remotecontrol_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mRemoteControl != null) {
            ((TextView)rootView.findViewById(R.id.remotecontrol_detail)).setText(mRemoteControl.getWidgetName());
        }

        return rootView;
    }
}
