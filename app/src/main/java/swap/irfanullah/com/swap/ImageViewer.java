package swap.irfanullah.com.swap;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import swap.irfanullah.com.swap.Adapters.MediaPager;

public class ImageViewer extends AppCompatActivity {

    private ViewPager viewPager;
    private MediaPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
    }
}
