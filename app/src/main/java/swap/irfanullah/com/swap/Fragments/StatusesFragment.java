package swap.irfanullah.com.swap.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.StatusAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

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
        RetroLib.geApiService().getStatuses(PrefStorage.getUser(getContext()).getTOKEN()).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()) {

                    Status status = response.body();
                    if(status.getAuthenticated()) {
                        if(status.getFound()) {

                            statusAdapter= new StatusAdapter(getActivity(),status.getSTATUSES());
                            sRV.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            sRV.setLayoutManager(layoutManager);
                            sRV.setAdapter(statusAdapter);

                        }
                        else {
                            Toast.makeText(getContext(),status.getMESSAGE(),Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(),status.getMESSAGE(),Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Log.i("STATUES: ","NOT SUCCESSFULL "+response.raw().toString());

                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Toast.makeText(getContext(),"ERROR: "+t.toString(),Toast.LENGTH_LONG).show();
                Log.i("STATUES: ","NOT SUCCESSFULL "+t.toString());

            }
        });

         return rootView;
    }
}
