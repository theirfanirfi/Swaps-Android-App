package swap.irfanullah.com.swap.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import swap.irfanullah.com.swap.R;

public class ChatFragment extends Fragment {

    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statuses, container, false);
         return rootView;
    }
}
