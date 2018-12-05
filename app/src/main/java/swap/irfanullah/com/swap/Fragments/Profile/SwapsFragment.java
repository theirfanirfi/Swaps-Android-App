package swap.irfanullah.com.swap.Fragments.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Adapters.SwapsAdapter;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;

public class SwapsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView sRV;
    private SwapsAdapter swapsAdapter;
    private ArrayList<Status> statuses;

    public SwapsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_swap, container, false);
        sRV = rootView.findViewById(R.id.swapRV);
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

        swapsAdapter= new SwapsAdapter(getActivity(),statuses);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(swapsAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Toast.makeText(getContext(),"working",Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(sRV);
         return rootView;
    }

    @Override
    public void onRefresh() {
        swapsAdapter.notifyDataSetChanged();
    }
}
