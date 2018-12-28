package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import swap.irfanullah.com.swap.R;

public class StatusFragGridAdapter extends BaseAdapter {
    private Context context;

    public StatusFragGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
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
        iv.setImageResource(R.drawable.person);
        return view;
    }
}
