package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;
import swap.irfanullah.com.swap.SwapWithActivity;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Status> statuses;

    public StatusAdapter(Context context, ArrayList<Status> st) {
        this.context = context;
        this.statuses = st;
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
    if(PrefStorage.getUser(context).getPROFILE_IMAGE() == null) {
        statusViewHolder.profile_image.setImageResource(R.drawable.ic_person);
    }
    else {
        statusViewHolder.profile_image.setImageURI(Uri.parse(PrefStorage.getUser(context).getPROFILE_IMAGE()));

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
        public StatusViewHolder(@NonNull final View itemView, final Context context, final ArrayList<Status> st) {
            super(itemView);

            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            swapWithBtn = itemView.findViewById(R.id.swapPlusIvBtn);
            statusTime = itemView.findViewById(R.id.statusTimeTextView);

            swapWithBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status_id = st.get(getAdapterPosition()).getSTATUS_ID();
                    Intent swapWith = new Intent(context,SwapWithActivity.class);
                    swapWith.putExtra("status_id",status_id);
                    context.startActivity(swapWith);
                }
            });
        }
    }
}
