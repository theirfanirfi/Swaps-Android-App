package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.TimeDiff;
import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.Models.SwapsTab;
import swap.irfanullah.com.swap.R;

public class SwapsAdapter extends RecyclerView.Adapter<SwapsAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<SwapsTab> swapsTabArrayList;
    private final static String REPRESENTING_LOGGED_USER_IN_TAB = "You";
    public SwapsAdapter(Context context, ArrayList<SwapsTab> swapsTabArrayList) {
        this.context = context;
        this.swapsTabArrayList = swapsTabArrayList;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.swap_custom_row,viewGroup,false);


        return new StatusViewHolder(view, this.context);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
            SwapsTab swap = this.swapsTabArrayList.get(i);
            if(swap.getIS_ME()){
                statusViewHolder.username.setText(REPRESENTING_LOGGED_USER_IN_TAB);
                statusViewHolder.withTextV.setText(swap.getSWAPED_WITH_FULLNAME());
            }else {
                statusViewHolder.username.setText(swap.getPOSTER_FULLNAME());
                statusViewHolder.withTextV.setText(REPRESENTING_LOGGED_USER_IN_TAB);
            }

            statusViewHolder.statusDescription.setText(swap.getSTATUS());
            statusViewHolder.ratingBar.setRating(swap.getAVG_RATTING());
            statusViewHolder.swapTime.setText(TimeDiff.getTimeDifference(swap.getSWAP_DATE()));
    }



    @Override
    public int getItemCount() {
        return this.swapsTabArrayList.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image;
        TextView username, statusDescription, withTextV,swapTime;
        ConstraintLayout layout;
        RatingBar ratingBar;
        public StatusViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            layout = itemView.findViewById(R.id.statusLayout);
            profile_image = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameTextView);
            withTextV = itemView.findViewById(R.id.withTextView);
            statusDescription = itemView.findViewById(R.id.statusTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            swapTime = itemView.findViewById(R.id.statusTimeTextView);
        }
    }

    public void notifySwapsAdapter(ArrayList<SwapsTab> swapsTabs){
        this.swapsTabArrayList = swapsTabs;
        notifyDataSetChanged();
    }
}
