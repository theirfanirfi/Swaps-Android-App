package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Participants;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantsViewHolder> {

    private Context context;
    private ArrayList<Participants> participantsArrayList;
    public ParticipantsAdapter(Context context,ArrayList<Participants> participantsArrayList) {
        this.context = context;
        this.participantsArrayList = participantsArrayList;
    }

    @NonNull
    @Override
    public ParticipantsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.participants_row,viewGroup,false);

        return new ParticipantsViewHolder(view,context,this.participantsArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantsViewHolder participantsViewHolder, int i) {
        Participants participants = participantsArrayList.get(i);
        if(participants.getAM_I_USER_ONE() == 1){
            participantsViewHolder.chatWithUsername.setText(participants.getUSER_TWO_NAME());
            if(participants.getUSER_TWO_PROFILE_IMAGE() == null){
                participantsViewHolder.profile_image.setImageResource(R.drawable.ic_person);
            }else {
                GLib.downloadImage(context,participants.getUSER_TWO_PROFILE_IMAGE()).into(participantsViewHolder.profile_image);
            }
        }else {
            participantsViewHolder.chatWithUsername.setText(participants.getUSER_ONE_NAME());
            if(participants.getUSER_ONE_PROFILE_IMAGE() == null){
                participantsViewHolder.profile_image.setImageResource(R.drawable.ic_person);
            }else {
                GLib.downloadImage(context,participants.getUSER_ONE_PROFILE_IMAGE()).into(participantsViewHolder.profile_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return participantsArrayList.size();
    }

    public static class ParticipantsViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView chatWithUsername;
        private ImageView profile_image;
        private ConstraintLayout pLayout;
        public ParticipantsViewHolder(@NonNull View itemView,final Context context,final ArrayList<Participants> participantsArrayList) {
            super(itemView);
            this.context = context;
            chatWithUsername = itemView.findViewById(R.id.chatWithUsername);
            profile_image = itemView.findViewById(R.id.profile_image);
            pLayout = itemView.findViewById(R.id.participantLayout);

           gotoChat(participantsArrayList);
        }

        private void gotoChat(final ArrayList<Participants> participantsArrayList){
            pLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    User user = PrefStorage.getUser(context);
                    Participants participants = participantsArrayList.get(position);
                    if(user.getUSER_ID() == participants.getUSER_TWO()) {
                        Log.i(RMsg.LOG_MESSAGE, "You are user two");
                    }else {
                        Log.i(RMsg.LOG_MESSAGE, "You are user one");
                    }
                }
            });
        }
    }

    public void notifyAdapter(ArrayList<Participants> participantsArrayList){
        this.participantsArrayList = participantsArrayList;
        notifyDataSetChanged();
    }
}
