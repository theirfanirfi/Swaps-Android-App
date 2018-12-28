package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.R;
public class StatusFragGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> _attachmentsArrayList;
    public StatusFragGridAdapter(Context context,ArrayList<String> attachmentsArrayList) {
        this.context = context;
        this._attachmentsArrayList = attachmentsArrayList;
    }

    @Override
    public int getCount() {
        RMsg.logHere("notified: size: "+Integer.toString(_attachmentsArrayList.size()));

        return _attachmentsArrayList.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.status_frag_grid_custom_row,parent,false);
        ImageView iv = view.findViewById(R.id.customSquareImage);
        try {
            GLib.downloadImage(context, _attachmentsArrayList.get(position)).into(iv);
        }catch (Exception e){
            RMsg.logHere("Exception: "+e.getMessage());

        }
        RMsg.logHere("notified: url: "+_attachmentsArrayList.get(position));
        return view;
    }

    public void notifyAdapter(ArrayList<String> at){
        this._attachmentsArrayList = at;
        notifyDataSetChanged();
        RMsg.logHere("notified");
    }
}
