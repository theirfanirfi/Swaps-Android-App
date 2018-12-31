package swap.irfanullah.com.swap.CustomComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.VideoView;


@SuppressLint("AppCompatCustomView")
public class SwapsSquareVideoViewForComposing extends VideoView {
    public SwapsSquareVideoViewForComposing(Context context) {
        super(context);
    }

    public SwapsSquareVideoViewForComposing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwapsSquareVideoViewForComposing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       super.onMeasure(getMeasuredWidth(), getMeasuredWidth()/2);

//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int size;
//        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ^ MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
//            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY)
//                size = width;
//            else
//                size = height-50;
//        }
//        else
//            size = Math.min(width, height);
//        setMeasuredDimension(size, size);
    }
}
