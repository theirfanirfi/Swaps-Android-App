package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.R;


//public class StatusFragGridAdapter extends ArrayAdapter {
//    private Context context;
//    private ArrayList<Attachments> _attachmentsArrayList;
//
//    public StatusFragGridAdapter(Context context,ArrayList<Attachments> _attachmentsArrayList) {
//        super(context, R.layout.status_frag_grid_custom_row);
//        this._attachmentsArrayList = _attachmentsArrayList;
//        this.context = context;
//    }
//
//
//    @Override
//    public int getCount() {
//       // RMsg.logHere("notified: size: "+Integer.toString(_attachmentsArrayList.size()));
//
//        return _attachmentsArrayList.size();
//
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = LayoutInflater.from(context).inflate(R.layout.status_frag_grid_custom_row,parent,false);
//        ImageView iv = view.findViewById(R.id.customSquareImage);
//        Attachments attachments = _attachmentsArrayList.get(position);
//        try {
//            GLib.downloadImage(context, attachments.getATTACHMENT_URL()).into(iv);
//        }catch (Exception e){
//            RMsg.logHere("Exception: "+e.getMessage());
//        }
//
//       RMsg.logHere("notified: url: "+attachments.getATTACHMENT_URL());
//        return view;
//    }
//
//    public void notifyAdapter(ArrayList<Attachments> at){
//        this._attachmentsArrayList = at;
//        notifyDataSetChanged();
//        //RMsg.logHere("notified");
//    }
//}

public class StatusFragGridAdapter extends RecyclerView.Adapter<StatusFragGridAdapter.MediaViewHolder>{

    private Context context;
    private ArrayList<Attachments> attachmentsArrayList;

    public StatusFragGridAdapter(Context context, ArrayList<Attachments> attachmentsArrayList) {
        this.context = context;
        this.attachmentsArrayList = attachmentsArrayList;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_frag_grid_custom_row,viewGroup,false);

        return new MediaViewHolder(view,context,attachmentsArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder mediaViewHolder, int i) {
        Attachments att = attachmentsArrayList.get(i);
        GLib.downloadImage(context,att.getATTACHMENT_URL()).into(mediaViewHolder.iv);
    }

    @Override
    public int getItemCount() {
        return this.attachmentsArrayList.size();
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ArrayList<Attachments> attachments;
        ImageView iv;
        public MediaViewHolder(@NonNull View itemView,Context context,ArrayList<Attachments> attachments) {
            super(itemView);
            this.context = context;
            this.attachments = attachments;
            iv = itemView.findViewById(R.id.customSquareImage);
        }
    }

        public void notifyAdapter(ArrayList<Attachments> at){
        this.attachmentsArrayList = at;
        notifyDataSetChanged();
        //RMsg.logHere("notified");
    }
}
