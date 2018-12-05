package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;

public class SwapsAdapter extends RecyclerView.Adapter<SwapsAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Status> statuses;

    public SwapsAdapter(Context context, ArrayList<Status> st) {
        this.context = context;
        this.statuses = st;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.swap_custom_row,viewGroup,false);


        return new StatusViewHolder(view, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {

    }



    @Override
    public int getItemCount() {
        return this.statuses.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image;
        TextView username, statusDescription;
        ConstraintLayout layout;
        public StatusViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
        }
    }
}
