package common.android.fiot.androidcommon;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by caoxuanphong on    8/4/16.
 * 
 * How to use: set height in xml, width is automatically same height
 */
public class SquareRelativeLayout extends RelativeLayout {


    public SquareRelativeLayout(Context context) {
        super(context);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

}
