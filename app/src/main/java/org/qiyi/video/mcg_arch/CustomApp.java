package org.qiyi.video.mcg_arch;

import android.app.Application;

import org.qiyi.video.mcg.arch.InterStellar;

/**
 * Created by wangallen on 2018/4/7.
 */

public class CustomApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        InterStellar.init(this);
    }
}
