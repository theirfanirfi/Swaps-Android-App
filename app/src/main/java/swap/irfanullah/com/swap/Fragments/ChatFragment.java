package swap.irfanullah.com.swap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Adapters.ParticipantsAdapter;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Participants;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ChatFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView rv;
    private ParticipantsAdapter  participantsAdapter;
    private Context context;
    private ArrayList<Participants> participantsArrayList;
    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        context = getContext();
            initializeObjects(rootView);
            getParticipants();
         return rootView;
    }

    public void initializeObjects(View rootView){
        participantsArrayList = new ArrayList<>();
        rv = rootView.findViewById(R.id.chatRV);
        progressBar = rootView.findViewById(R.id.participantLoadingProgressbar);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        participantsAdapter = new ParticipantsAdapter(context,participantsArrayList);
        rv.setAdapter(participantsAdapter);
    }

    public void getParticipants(){
        RetroLib.geApiService().getParticipants(PrefStorage.getUser(context).getTOKEN()).enqueue(new Callback<Participants>() {
            @Override
            public void onResponse(Call<Participants> call, Response<Participants> response) {
                if(response.isSuccessful()){
                    Participants participants = response.body();
                    if(participants.getAuthenticated()){
                        if(participants.getFound()){
                            updateAdapter(participants.getPARTICIPANTS());
                        }else if(participants.getError()) {
                            RMsg.toastHere(context,participants.getMESSAGE());
                        }else {
                            RMsg.toastHere(context,participants.getMESSAGE());
                        }
                    }else {
                        RMsg.toastHere(getContext(),RMsg.AUTH_ERROR_MESSAGE);
                    }

                }else {
                    RMsg.toastHere(getContext(),RMsg.REQ_ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<Participants> call, Throwable t) {
                RMsg.toastHere(getContext(),t.toString());
            }
        });

        progressBar.setVisibility(View.GONE);
    }

    public void updateAdapter(ArrayList<Participants> participantsArrayList1){
        this.participantsArrayList= participantsArrayList1;
        participantsAdapter.notifyAdapter(participantsArrayList1);
    }

}
