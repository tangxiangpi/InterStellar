package org.qiyi.video.mcg.arch.life;

/**
 * Created by wangallen on 2018/3/27.
 */

public class ApplicationLifecycle implements Lifecycle{

    @Override
    public void addListener(LifecycleListener listener) {
        listener.onStart();
    }

    @Override
    public void removeListener(LifecycleListener listener) {

    }
}
