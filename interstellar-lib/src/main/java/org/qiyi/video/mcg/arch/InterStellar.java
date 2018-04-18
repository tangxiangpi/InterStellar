package org.qiyi.video.mcg.arch;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import org.qiyi.video.mcg.arch.local.LocalServiceManager;
import org.qiyi.video.mcg.arch.remote.IRemoteManager;
import org.qiyi.video.mcg.arch.remote.RemoteManagerRetriever;
import org.qiyi.video.mcg.arch.remote.RemoteServiceManager;

/**
 * Created by wangallen on 2018/3/31.
 */

public class InterStellar {

    private static Context appContext;
    private static InterStellar sInstance;

    private RemoteManagerRetriever remoteManagerRetriever;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static InterStellar getInstance(){
        if(null==sInstance){
            synchronized (InterStellar.class){
                if(null==sInstance){
                    sInstance=new InterStellar();
                }
            }
        }
        return sInstance;
    }

    private InterStellar(){
        remoteManagerRetriever=new RemoteManagerRetriever();
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

    /*
    public static <T> T getRemoteService(Class<?> serviceInterface) {
        if (null == serviceInterface) {
            return null;
        }
        return RemoteServiceManager.getInstance().getServiceProxy(serviceInterface);
    }
    */

    public static IRemoteManager with(android.app.Fragment fragment){
         return getRetriever().get(fragment);
    }

    public static IRemoteManager with(Fragment fragment){
        return getRetriever().get(fragment);
    }

    public static IRemoteManager with(FragmentActivity fragmentActivity){
        return getRetriever().get(fragmentActivity);
    }

    public static IRemoteManager with(Activity activity){
        return getRetriever().get(activity);
    }

    public static IRemoteManager with(Context context){
        return getRetriever().get(context);
    }

    public static IRemoteManager with(View view){
        return getRetriever().get(view);
    }

    public static RemoteManagerRetriever getRetriever(){
        return getInstance().remoteManagerRetriever;
    }

    public static void unregisterRemoteService(Class<?> serviceInterface) {
        if (null == serviceInterface) {
            return;
        }
        RemoteServiceManager.getInstance().unregisterRemoteService(serviceInterface);
    }


}
