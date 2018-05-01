package common.android.fiot.androidcommon;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by caoxuanphong on    4/29/16.
 */
public class DateTimeUtils {
    private static final String TAG = "DateTimeUtils";

    public static long currentTimeToEpoch() {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime()) / 1000;

        Log.i(TAG, "currentTimeToEpoch: " + offsetFromUtc +  ", " + System.currentTimeMillis());
        return (System.currentTimeMillis() / 1000 + offsetFromUtc);
    }

    /**
     * Get current timezone of device
     * @return
     */
    public static int getTimezone() {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime());

        return offsetFromUtc/3600000;
    }

    public static String epochToString(long epoch, String format) {
        //Date date = new Date((epoch - offsetFromUtc) * 1000);
        Date date = new Date((epoch) * 1000);
        SimpleDateFormat s = new SimpleDateFormat(format);
        return s.format(date);
    }

    public static Date epochToDate(long epoch) {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime()) / 1000;

        Date date = new Date((epoch - offsetFromUtc) * 1000);
        return date;
    }

    public static boolean isSameDate(long epoch1, long epoch2) {
        Date date1 = epochToDate(epoch1);
        Date date2 = epochToDate(epoch2);

        if (date1.getDate() == date2.getDate() &&
                date1.getMonth() == date2.getMonth() &&
                date1.getYear() == date2.getYear()) {
            return true;
        }

        return false;
    }

    /**
     * Get current time
     * @param format Wanted format
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static Date getDate(int year,
                               int month,
                               int day,
                               int hour,
                               int minute,
                               int second,
                               int millisecond) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);
        return cal.getTime();
    }

}
