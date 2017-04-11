package com.viralandroid.androidtabsatbottom.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;

import common.android.fiot.androidcommon.R;


/**
 * Created by caoxuanphong on    8/19/16.
 */
public class DialogUtils {
    /**
     * Have click OK Listener
     * @param context
     * @param message
     * @param clickOKListener
     */
    public static void showMessage(final Context context,
                                   final String message,
                                   final DialogInterface.OnClickListener clickOKListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    new AlertDialog.Builder(context)
                            .setMessage(message)
                            .setPositiveButton("OK", clickOKListener)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Have click OK Listener
     * @param context
     * @param tittle
     * @param message
     * @param clickOKListener
     */
    public static void showMessage(final Context context,
                                   final String tittle,
                                   final String message,
                                   final DialogInterface.OnClickListener clickOKListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    new AlertDialog.Builder(context)
                            .setTitle(tittle)
                            .setMessage(message)
                            .setPositiveButton("OK", clickOKListener)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Have click OK Listener
     * @param context
     * @param tittle
     * @param message
     * @param clickOKListener
     */
    public static void showMessage(final Context context,
                                   final String tittle,
                                   final String message,
                                   final DialogInterface.OnClickListener clickOKListener,
                                   final DialogInterface.OnClickListener clickCancelListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    new AlertDialog.Builder(context)
                            .setTitle(tittle)
                            .setMessage(message)
                            .setPositiveButton("OK", clickOKListener)
                            .setNegativeButton("Cancel", clickCancelListener)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    /**
     * Have click OK Listener
     * @param context
     * @param message
     * @param clickOKListener
     */
    public static void showMessage(final Context context,
                                   final String message,
                                   final DialogInterface.OnClickListener clickOKListener,
                                   final DialogInterface.OnClickListener clickNoListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("YES", clickOKListener)
                .setNegativeButton("NO", clickNoListener)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
            }
        });
    }
    
    /**
     * Have OnDismissListener
     * @param context
     * @param message
     * @param dismissListener
     */
    public static void showMessage(final Context context,
                                   final String message,
                                   final DialogInterface.OnDismissListener dismissListener) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(context)
                        .setMessage(message)
                        .setPositiveButton("OK", null)
                        .setOnDismissListener(dismissListener)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
}
