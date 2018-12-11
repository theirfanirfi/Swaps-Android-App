package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;

public class SingleStatusAdapter extends RecyclerView.Adapter<SingleStatusAdapter.StatusViewHolder> {

    private ArrayList<User> raters;
    private Context context;
    public SingleStatusAdapter(Context context, ArrayList<User> raters) {
        this.context = context;
        this.raters = raters;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_status_custom_row,viewGroup,false);

        return new StatusViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder statusViewHolder, int i) {
        User user = this.raters.get(i);
    statusViewHolder.fullname.setText(user.getFULL_NAME());
    statusViewHolder.stars.setText(Float.toString(user.getRATING())+ " stars");
    }


    @Override
    public int getItemCount() {
        return this.raters.size();
    }


    public static class StatusViewHolder extends RecyclerView.ViewHolder {
        TextView fullname, stars;
        ImageView profile_image;
        public StatusViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullNameTextView);
            stars = itemView.findViewById(R.id.starsGivenTextView);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
