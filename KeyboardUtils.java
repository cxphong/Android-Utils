package common.android.fiot.androidcommon;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by caoxuanphong on 5/2/17.
 */

public class KeyboardUtils {
    public interface KeyboardUtilsListener {
        void isShowing(boolean isShowing);
    }

    public static void show(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hide(Context context) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) context).getCurrentFocus();

        if (v != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void isVisible(final Context context, final View rootView, final KeyboardUtilsListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        final float density = dm.density;

        ViewTreeObserver.OnGlobalLayoutListener list = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                rootView.getWindowVisibleDisplayFrame(r);

                // cache properties for later use
                int rootViewHeight = rootView.getRootView().getHeight();
                int resultBottom = r.bottom;

                // calculate screen height differently for android versions >= 21: Lollipop 5.x, Marshmallow 6.x
                //http://stackoverflow.com/a/29257533/3642890 beware of nexus 5
                int screenHeight;

                if (Build.VERSION.SDK_INT >= 21) {
                    Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    screenHeight = size.y;
                } else {
                    screenHeight = rootViewHeight;
                }

                int heightDiff = screenHeight - resultBottom;

                int pixelHeightDiff = (int) (heightDiff / density);
                if (pixelHeightDiff > 100 ) { // if more than 100 pixels, its probably a keyboard...
                    listener.isShowing(true);
                } else {
                    listener.isShowing(false);
                }

                if (Build.VERSION.SDK_INT < 16) {
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        };

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(list);
    }
}
