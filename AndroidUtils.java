package com.example.dinhtho.fileutils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by dinhtho on 20/04/2017.
 */

public class AndroidUtils {

    public static int dpToPixel(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxelToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int screenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void makeScreenAlwaysOn(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    public static int getBattery(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale;
        return (int) (batteryPct * 100);
    }

    public static String getBatteryStatus(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        String strStatus = "";
        switch (status) {
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                strStatus = "BATTERY_STATUS_UNKNOWN";
                break;
            case BatteryManager.BATTERY_STATUS_CHARGING:
                strStatus = "BATTERY_STATUS_CHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                strStatus = "BATTERY_STATUS_DISCHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                strStatus = "BATTERY_STATUS_NOT_CHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                strStatus = "BATTERY_STATUS_FULL";
                break;

        }
        return strStatus;
    }

    public static String phoneOrTablet(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return "TABLET";
        } else {
            return "PHONE";
        }
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getAndroidversion() {
        return Build.VERSION.RELEASE;
    }

    public static String getIPAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }
        return ipAddressString;
    }

    public static String getLanguage() {
        return Locale.getDefault().getDisplayLanguage();
    }

    public static String getUserName(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }

    public static String getPhoneNumber(Context context) {
        TelephonyManager telemamanger = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telemamanger.getLine1Number();
    }

    public static String getCurrentWifi(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;

    }

    public static String getSIMInfo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simCountry = telephonyManager.getSimCountryIso();
        String simOperatorCode = telephonyManager.getSimOperator();
        String simOperatorName = telephonyManager.getSimOperatorName();
        String simSerial = telephonyManager.getSimSerialNumber();
        return "simCountry " + simCountry + ",simOperatorCode " + simOperatorCode + ",simOperatorName " + simOperatorName + ",simSerial " + simSerial;
    }

    public static int getNumberImage(Context context) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        return cursor.getCount();
    }

    public static int getNumberVideo(Context context) {
        String[] projection = {MediaStore.Video.Media._ID};
        Cursor cursor = new CursorLoader(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, // Return all rows
                null, null).loadInBackground();
        return cursor.getCount();
    }

}

