package swap.irfanullah.com.swap.CustomComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


@SuppressLint("AppCompatCustomView")
public class SwapsSquareImageView extends ImageView {
    public SwapsSquareImageView(Context context) {
        super(context);
    }

    public SwapsSquareImageView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public SwapsSquareImageView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(getMeasuredWidth(), getMeasuredWidth());
    }
}
