package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.Toast;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Models.Status;
import swap.irfanullah.com.swap.R;

public class SwapWithAdapter extends RecyclerView.Adapter<SwapWithAdapter.StatusViewHolder> {
    private Context context;
    private ArrayList<Status> statuses;

    public SwapWithAdapter(Context context, ArrayList<Status> st) {
        this.context = context;
        this.statuses = st;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.swap_with_custom_row,viewGroup,false);


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
        CheckBox swapedWith;
        public StatusViewHolder(@NonNull View itemView, final Context context) {
            super(itemView);
            swapedWith = itemView.findViewById(R.id.swapedWithcheckBox);

            swapedWith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        Toast.makeText(context,"checked",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    public void FilterRV(ArrayList<Status> statuses)
    {
        this.statuses = statuses;
        notifyDataSetChanged();
    }
}
