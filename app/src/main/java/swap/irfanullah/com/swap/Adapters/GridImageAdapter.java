package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import swap.irfanullah.com.swap.Models.RMsg;

public class GridImageAdapter extends BaseAdapter {
    private Context context;
    private List<Uri> images;

    public GridImageAdapter(Context context, List<Uri> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
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
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }

        // if(images.size() > 0) {
        imageView.setImageURI(images.get(position));
        // }


        return imageView;
    }

    public void notifyAdapter(List<Uri> uriList) {
        this.images = uriList;
        RMsg.ilogHere(this.images.size());
        notifyDataSetChanged();
    }
}
