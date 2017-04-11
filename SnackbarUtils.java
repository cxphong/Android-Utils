package common.android.fiot.androidcommon;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by caoxuanphong on 4/2/17.
 */

public class SnackbarUtils {

    public static void show(final String message, final View parent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

}
