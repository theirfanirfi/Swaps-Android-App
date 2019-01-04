package swap.irfanullah.com.swap.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.util.ArrayList;

import swap.irfanullah.com.swap.Libraries.GLib;
import swap.irfanullah.com.swap.Models.Attachments;
import swap.irfanullah.com.swap.R;

public class MediaPager extends PagerAdapter {
    private Context context;
    private ArrayList<Attachments> attachments;

    public MediaPager(Context context,ArrayList<Attachments> attachments) {
        this.context = context;
        this.attachments = attachments;
    }

    @Override
    public int getCount() {
        return this.attachments.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Attachments media = this.attachments.get(position);
        if(media.getATTACHMENT_TYPE() == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_viewer_layout, container, false);
            ImageView iv = view.findViewById(R.id.imageView);
            GLib.downloadImage(context,media.getATTACHMENT_URL()).into(iv);
            container.addView(view);
            return view;
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.video_viewer_layout, container, false);
            VideoView iv = view.findViewById(R.id.videoView);
            container.addView(view);
            return view;
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
