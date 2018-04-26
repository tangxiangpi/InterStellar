package org.qiyi.video.mcg.arch.life;

import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.utils.CollectionUtils;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by wangallen on 2018/3/27.
 */

public class ActivityFragLifecycle implements Lifecycle {

    private final Set<LifecycleListener> lifecycleListeners = Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());

    private boolean isStarted;
    private boolean isDestroyed;

    @Override
    public void addListener(LifecycleListener listener) {
        Logger.d(this.toString() + "-->addListener,isDestroyed:" + isDestroyed + ",isStarted:" + isStarted);
        lifecycleListeners.add(listener);

        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStarted) {
            listener.onStart();
        } else {
            listener.onStop();
        }
    }

    @Override
    public void removeListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    public void onStart() {
        Logger.d(this.toString() + "-->onStart()");
        isStarted = true;
        for (LifecycleListener listener : CollectionUtils.getSnapshot(lifecycleListeners)) {
            listener.onStart();
        }
    }

    public void onStop() {
        Logger.d(this.toString() + "-->onStop()");
        isStarted = false;
        for (LifecycleListener listener : CollectionUtils.getSnapshot(lifecycleListeners)) {
            listener.onStop();
        }
    }

    public void onDestroy() {
        Logger.d(this.toString() + "-->onDestroy()");
        isDestroyed = true;
        for (LifecycleListener listener : CollectionUtils.getSnapshot(lifecycleListeners)) {
            listener.onDestroy();
        }
    }
}
