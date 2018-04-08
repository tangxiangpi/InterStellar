package org.qiyi.video.mcg.arch.remote;

import android.content.Context;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;

import org.qiyi.video.mcg.arch.InterStellar;
import org.qiyi.video.mcg.arch.bean.BinderBean;
import org.qiyi.video.mcg.arch.dispatch.DispatcherCursor;
import org.qiyi.video.mcg.arch.dispatch.DispatcherProvider;
import org.qiyi.video.mcg.arch.dispatch.IServiceRegister;
import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.transfer.ITransfer;
import org.qiyi.video.mcg.arch.transfer.ServiceHandler;
import org.qiyi.video.mcg.arch.transfer.Transfer;
import org.qiyi.video.mcg.arch.utils.IOUtils;
import org.qiyi.video.mcg.arch.utils.ProcessUtils;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangallen on 2018/3/31.
 */

public class RemoteServiceManager {

    private static RemoteServiceManager sInstance;

    public static RemoteServiceManager getInstance() {
        if (null == sInstance) {
            synchronized (RemoteServiceManager.class) {
                if (null == sInstance) {
                    sInstance = new RemoteServiceManager();
                }
            }
        }
        return sInstance;
    }

    private Map<String, Object> serviceCache = new HashMap<>();

    private BinderBean binderBean;

    private IServiceRegister serviceRegister;

    private RemoteServiceManager() {
    }

    public synchronized void registerRemoteService(Class<?> serviceInterface, Object serviceImpl) {
        Logger.d("RemoteServiceManager-->registerRemoteService");
        serviceCache.put(serviceInterface.getCanonicalName(), serviceImpl);
        //TODO 向Dispatcher注册接口和进程信息
        checkServiceRegister();
        //TODO 要采用其他策略来获取了
        if(null==serviceRegister){
            return;
        }
        if (binderBean == null) {
            binderBean = new BinderBean(Transfer.getInstance().asBinder(),
                    ProcessUtils.getProcessName(InterStellar.getAppContext()));
        }
        try {
            serviceRegister.registerRemoteService(serviceInterface.getCanonicalName(), binderBean);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void checkServiceRegister() {
        if (serviceRegister == null) {
            IBinder binder = getIBinderFromProvider(InterStellar.getAppContext());
            serviceRegister = IServiceRegister.Stub.asInterface(binder);
        }
    }

    private IBinder getIBinderFromProvider(Context context) {
        Logger.d("RemoteServiceManager-->getIBinderFromProvider()");
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DispatcherProvider.URI,
                    DispatcherProvider.PROJECTION_MAIN,
                    null, null, null);
            if (cursor == null) {
                return null;
            }
            return DispatcherCursor.stripBinder(cursor);
        } finally {
            IOUtils.closeQuietly(cursor);
        }
    }
    //TODO 出现java.lang.IllegalArgumentException: interface org.qiyi.video.mcg_arch.service.IAppleService is not visible from class loader
    public synchronized <T> T getServiceProxy(Class<?> serviceInterface) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{serviceInterface},
                new ServiceHandler(serviceInterface.getCanonicalName()));
    }

    public synchronized Object getStubService(Class<?> serviceInterface) {
        return serviceCache.get(serviceInterface.getCanonicalName());
    }

    //TODO 这个改成叫getStubService()会更不容易引起歧义
    public synchronized Object getStubService(String serviceCanonicalName) {
        return serviceCache.get(serviceCanonicalName);
    }

    private Map<String, BinderBean> remoteTransferCache = new HashMap<>();

    public synchronized ITransfer getRemoteTransfer(final String serviceCanonicalName) {
        if (remoteTransferCache.get(serviceCanonicalName) != null) {
            return ITransfer.Stub.asInterface(remoteTransferCache.get(serviceCanonicalName).getBinder());
        }
        checkServiceRegister();
        try {
            BinderBean binderBean = serviceRegister.getTransferInfo(serviceCanonicalName);
            binderBean.getBinder().linkToDeath(new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    //TODO 要考虑并发的问题
                    removeFromCache(serviceCanonicalName);
                }
            }, 0);
            remoteTransferCache.put(serviceCanonicalName, binderBean);
            return ITransfer.Stub.asInterface(binderBean.getBinder());
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private synchronized void removeFromCache(String serviceCanonicalName) {
        remoteTransferCache.remove(serviceCanonicalName);
    }

    public synchronized void unregisterRemoteService(Class<?> serviceInterface) {
        serviceCache.remove(serviceInterface.getCanonicalName());
        //TODO 向Dispatcher注销，或者就发一个广播事件好了
        try {
            serviceRegister.unregisterRemoteService(serviceInterface.getCanonicalName());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

}
