package org.qiyi.video.mcg.arch;

import android.content.Context;

import org.qiyi.video.mcg.arch.local.LocalServiceManager;
import org.qiyi.video.mcg.arch.remote.RemoteServiceManager;

/**
 * Created by wangallen on 2018/3/31.
 */

public class InterStellar {

    private static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static void registerLocalService(Class<?> serviceInterface, Object serviceImpl) {
        if (null == serviceInterface || null == serviceImpl) {
            return;
        }
        LocalServiceManager.getInstance().registerLocalService(serviceInterface, serviceImpl);
    }

    public static <T> T getLocalService(Class<?> serviceInterface) {
        if (null == serviceInterface) {
            return null;
        }
        return LocalServiceManager.getInstance().getLocalService(serviceInterface);
    }

    public static void unregisterLocalService(Class<?> serviceInterface) {
        if (null == serviceInterface) {
            return;
        }
        LocalServiceManager.getInstance().unregisterLocalService(serviceInterface);
    }

    public static void registerRemoteService(Class<?> serviceInterface, Object serviceImpl) {
        if (null == serviceInterface || null == serviceImpl) {
            return;
        }
        RemoteServiceManager.getInstance().registerRemoteService(serviceInterface, serviceImpl);
    }

    public static <T> T getRemoteService(Class<?> serviceInterface) {
        if (null == serviceInterface) {
            return null;
        }
        return RemoteServiceManager.getInstance().getServiceProxy(serviceInterface);
    }

    public static void unregisterRemoteService(Class<?> serviceInterface) {
        if (null == serviceInterface) {
            return;
        }
        RemoteServiceManager.getInstance().unregisterRemoteService(serviceInterface);
    }


}
