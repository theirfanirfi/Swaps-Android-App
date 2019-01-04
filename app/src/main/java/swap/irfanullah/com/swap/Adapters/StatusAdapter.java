package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.SwapsTab;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.NLUserProfile;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.StatusActivity;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.SwapWithActivity;
import swap.irfanullah.com.swap.UserProfile;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Status> statuses;
    private ArrayList<String> attachmentsArrayList;
    private User user;

    public StatusAdapter(Context context, ArrayList<Status> st) {
        this.context = context;
        this.statuses = st;
        user = PrefStorage.getUser(this.context);
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.status_custom_row,viewGroup,false);


        return new StatusViewHolder(view, this.context, this.statuses);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        Status e = statuses.get(i);
    statusViewHolder.statusDescription.setText(e.getSTATUS());
    statusViewHolder.ratingBar.setRating(e.getRATTING());
    statusViewHolder.statusTime.setText(TimeDiff.getTimeDifference(e.getTIME()));

    if(user.getPROFILE_IMAGE() == null) {
        statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
    }
    else {
        //statusViewHolder.profile_image.setImageURI(Uri.parse(PrefStorage.getUser(context).getPROFILE_IMAGE()));
        GLib.downloadImage(context,user.getPROFILE_IMAGE()).into(statusViewHolder.profile_image);
    }
    //RMsg.logHere(Integer.toString(e.getHAS_ATTACHMENTS()));

    if(e.getHAS_ATTACHMENTS() == 1) {
        loadStatusMedia(statusViewHolder,e.getATTACHMENTS());
    }else {
        statusViewHolder.mediaView.setVisibility(View.GONE);
    }


    }



    @Override
    public int getItemCount() {
        return this.statuses.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image, swapWithBtn;
        TextView username, statusDescription;
        ConstraintLayout layout;
        RatingBar ratingBar;
        TextView statusTime;
        RecyclerView mediaView;
        ProgressBar mediaProgressBar;
        public StatusViewHolder(@NonNull final View itemView, final Context context, final ArrayList<Status> st) {
            super(itemView);

            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);

            ratingBar = itemView.findViewById(R.id.ratingBar);
            swapWithBtn = itemView.findViewById(R.id.swapPlusIvBtn);
            statusTime = itemView.findViewById(R.id.statusTimeTextView);
            mediaView = itemView.findViewById(R.id.gridViewStatus);
         //   mediaProgressBar = itemView.findViewById(R.id.mediaProgressBar);

            swapWithBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status_id = st.get(getAdapterPosition()).getSTATUS_ID();
                    Intent swapWith = new Intent(context,SwapWithActivity.class);
                    swapWith.putExtra("status_id",status_id);
                    context.startActivity(swapWith);
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Status status = st.get(position);
                    int status_id = status.getSTATUS_ID();
                    Intent singleStatusAct = new Intent(context,StatusActivity.class);
                    singleStatusAct.putExtra("status_id",status_id);
                    singleStatusAct.putExtra("position",position);
                    singleStatusAct.putExtra("is_accepted",0);
                    singleStatusAct.putExtra("swap_id",0);
                    context.startActivity(singleStatusAct);
                }
            });

            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Status status = st.get(position);
                    if(PrefStorage.isMe(context,status.getUSER_ID())){
                        Intent profileAct = new Intent(context,UserProfile.class);
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }else {
                        Intent profileAct = new Intent(context,NLUserProfile.class);
                        profileAct.putExtra("user_id",status.getUSER_ID());
                        profileAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profileAct);
                    }
                }
            });

            mediaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public void notifyAdapter(ArrayList<Status> statuses){
        this.statuses = statuses;
        notifyDataSetChanged();
    }

    private void loadStatusMedia(final StatusViewHolder viewHolder,  String attachments){
        ArrayList<Attachments> mediaAttachments = new ArrayList<>();
        StatusFragGridAdapter statusFragGridAdapter = new StatusFragGridAdapter(context,mediaAttachments);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,4);
        viewHolder.mediaView.setHasFixedSize(true);
        viewHolder.mediaView.setLayoutManager(layoutManager);
        viewHolder.mediaView.setAdapter(statusFragGridAdapter);
        viewHolder.mediaView.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        JsonElement json = gson.fromJson(attachments,JsonElement.class);
        if(json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Attachments att = gson.fromJson(object, Attachments.class);
            mediaAttachments.add(att);
            statusFragGridAdapter.notifyAdapter(mediaAttachments);
            // GLib.downloadImage(context, att.getATTACHMENT_URL()).into(statusMedia);

            RMsg.logHere("Single: "+att.getATTACHMENT_URL());
        }else if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            //Attachments att = gson.fromJson(object, Attachments.class);
            Type type = new TypeToken<ArrayList<Attachments>>(){}.getType();
            ArrayList<Attachments> arrayList = gson.fromJson(jsonArray,type);
            statusFragGridAdapter.notifyAdapter(arrayList);
            RMsg.logHere("working");
        }
    }
}
