package swap.irfanullah.com.swap.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Adapters.StatusAdapter;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;

public class StatusesFragment extends Fragment {

    private RecyclerView sRV;
    private StatusAdapter statusAdapter;
    private ArrayList<Status> statuses;

    public StatusesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statuses, container, false);
        sRV = rootView.findViewById(R.id.statusesRV);
        statuses = new ArrayList<>();
        Status st1 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st1);

        Status st2 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st2);
        Status st3 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st3);

        Status st4 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st4);

        Status st5 = new Status("Irfan",1,1,"This is my new statuse","2h","profile");

        statuses.add(st5);

        statusAdapter= new StatusAdapter(getActivity(),statuses);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(statusAdapter);

         return rootView;
    }
}
