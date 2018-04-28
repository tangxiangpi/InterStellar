package org.qiyi.video.mcg.arch.dispatch;

import android.os.RemoteException;
import android.text.TextUtils;

import org.qiyi.video.mcg.arch.bean.BinderBean;
import org.qiyi.video.mcg.arch.log.Logger;

/**
 * Created by wangallen on 2018/3/31.
 */
public class Dispatcher extends IServiceRegister.Stub {

    public static Dispatcher sInstance;

    public static Dispatcher getInstance() {
        if (null == sInstance) {
            synchronized (Dispatcher.class) {
                if (null == sInstance) {
                    sInstance = new Dispatcher();
                }
            }
        }
        return sInstance;
    }

    private IServiceDispatcher serviceDispatcher;

    private Dispatcher() {
        serviceDispatcher = new ServiceDispatcher();
    }

    @Override
    public synchronized void registerRemoteService(String serviceInterface, BinderBean transferBinderBean) throws RemoteException {
        if (TextUtils.isEmpty(serviceInterface) || null == transferBinderBean) {
            return;
        }
        Logger.d("Dispatcher-->registerRemoteService,serviceInterface:" + serviceInterface + ",processName:" + transferBinderBean.getProcessName());
        serviceDispatcher.registerRemoteService(serviceInterface, transferBinderBean);
    }

    @Override
    public synchronized void unregisterRemoteService(String serviceInterface) throws RemoteException {
        if (TextUtils.isEmpty(serviceInterface)) {
            return;
        }
        Logger.d("Dispatcher-->unregisterRemoteService,serviceInterface:" + serviceInterface);
        serviceDispatcher.unregisterRemoteService(serviceInterface);
    }

    @Override
    public synchronized BinderBean getTransferInfo(String serviceInterface) throws RemoteException {
        Logger.d("Dispatcher-->getTransferInfo,serviceInterface:" + serviceInterface);
        if (TextUtils.isEmpty(serviceInterface)) {
            return null;
        }
        return serviceDispatcher.getTransferBean(serviceInterface);
    }
}
