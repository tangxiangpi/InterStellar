package org.qiyi.video.mcg.arch.log;

import android.util.Log;

import org.qiyi.video.mcg.arch.InterStellar;

/**
 * Created by wangallen on 2018/4/7.
 */


public class Logger {

    private static final String TAG = InterStellar.class.getSimpleName();

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

}
