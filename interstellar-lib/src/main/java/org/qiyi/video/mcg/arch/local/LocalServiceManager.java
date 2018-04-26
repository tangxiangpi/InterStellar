package org.qiyi.video.mcg.arch.local;

/**
 * Created by wangallen on 2018/3/31.
 */

public class LocalServiceManager {

    private static LocalServiceManager sInstance;

    public static LocalServiceManager getInstance() {
        if (null == sInstance) {
            synchronized (LocalServiceManager.class) {
                if (null == sInstance) {
                    sInstance = new LocalServiceManager();
                }
            }
        }
        return sInstance;
    }

    private LocalServiceManager() {
    }

    public void registerLocalService(Class<?> serviceInterface, Object serviceImpl) {

    }

    public <T> T getLocalService(Class<?> serviceInterface) {

        return null;
    }

    public void unregisterLocalService(Class<?> serviceInterface) {

    }

}
