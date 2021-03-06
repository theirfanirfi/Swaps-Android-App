package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.ChatActivity;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Participants;
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
        private final String LOGGEDIN_USER_INTENT_KEY = "loggedin_user_id";
        private final String TO_CHAT_WITH_USER_INTENT_KEY = "to_chat_with_user_id";
        private final String CHAT_ID_INTENT_KEY = "chat_id";

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
                    int CHAT_ID = participants.getCHAT_ID();
                    Intent chatAct = new Intent(context,ChatActivity.class);

                    if(user.getUSER_ID() == participants.getUSER_TWO()) {
                        int TO_CHAT_WITH_ID = participants.getUSER_ONE();
                        int LOGGEDIN_USER_ID = participants.getUSER_TWO();
                        chatAct.putExtra(LOGGEDIN_USER_INTENT_KEY,LOGGEDIN_USER_ID);
                        chatAct.putExtra(TO_CHAT_WITH_USER_INTENT_KEY,TO_CHAT_WITH_ID);
                        chatAct.putExtra(CHAT_ID_INTENT_KEY,CHAT_ID);
                        context.startActivity(chatAct);
                    }else {
                        int TO_CHAT_WITH_ID = participants.getUSER_TWO();
                        int LOGGEDIN_USER_ID = participants.getUSER_ONE();
                        chatAct.putExtra(LOGGEDIN_USER_INTENT_KEY,LOGGEDIN_USER_ID);
                        chatAct.putExtra(TO_CHAT_WITH_USER_INTENT_KEY,TO_CHAT_WITH_ID);
                        chatAct.putExtra(CHAT_ID_INTENT_KEY,CHAT_ID);
                        context.startActivity(chatAct);
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
