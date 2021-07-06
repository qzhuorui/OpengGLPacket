package com.choryan.opengglpacket.util;

import android.util.Log;

/**
 * @author: ChoRyan Quan
 * @date: 2021/7/6
 */
public class LogUtil {

    private static final String TAG = "LogUtil";

    public static void print(String msg) {
        Log.d(TAG, "print: " + msg);
    }
}
