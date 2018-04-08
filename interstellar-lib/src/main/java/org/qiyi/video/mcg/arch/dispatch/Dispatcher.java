package org.qiyi.video.mcg.arch.dispatch;

import android.os.RemoteException;
import android.text.TextUtils;

import org.qiyi.video.mcg.arch.bean.BinderBean;
import org.qiyi.video.mcg.arch.log.Logger;

/**
 * Created by wangallen on 2018/3/31.
 */
//TODO 后面考虑持久化保存，然后把Dispatcher放在一个独立进程中，这样万一这个进程挂了，重新拉起还是可以恢复数据
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
    public void registerRemoteService(String serviceInterface, BinderBean transferBinderBean) throws RemoteException {
        if (TextUtils.isEmpty(serviceInterface) || null == transferBinderBean) {
            return;
        }
        Logger.d("Dispatcher-->registerRemoteService,serviceInterface:" + serviceInterface + ",processName:" + transferBinderBean.getProcessName());
        serviceDispatcher.registerRemoteService(serviceInterface, transferBinderBean);
    }

    @Override
    public void unregisterRemoteService(String serviceInterface) throws RemoteException {
        if (TextUtils.isEmpty(serviceInterface)) {
            return;
        }
        Logger.d("Dispatcher-->unregisterRemoteService,serviceInterface:" + serviceInterface);
        serviceDispatcher.unregisterRemoteService(serviceInterface);
    }

    @Override
    public BinderBean getTransferInfo(String serviceInterface) throws RemoteException {
        Logger.d("Dispatcher-->getTransferInfo,serviceInterface:" + serviceInterface);
        if (TextUtils.isEmpty(serviceInterface)) {
            return null;
        }
        return serviceDispatcher.getTransferBean(serviceInterface);
    }
}
