package common.android.fiot.androidcommon;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by caoxuanphong on 3/30/17.
 */

public class BroadcastUtils {

    public static void sendBroadcast(Context context, String action, HashMap<String,String> params) {
        Intent intent = new Intent(action);

        if (params != null) {
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                intent.putExtra(pair.getKey().toString(), pair.getValue().toString());
                it.remove();
            }
        }

        context.sendBroadcast(intent);
    }
}
