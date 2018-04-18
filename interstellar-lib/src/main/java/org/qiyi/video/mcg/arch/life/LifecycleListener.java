package org.qiyi.video.mcg.arch.life;

/**
 * Created by wangallen on 2018/3/27.
 */

public interface LifecycleListener {

    void onStart();

    void onStop();

    void onDestroy();
}
