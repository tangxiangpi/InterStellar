package org.qiyi.video.mcg.arch.utils;

import android.os.Looper;

/**
 * Created by wangallen on 2018/4/17.
 */

public final class Utils {

    private Utils() {
    }

    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

}
