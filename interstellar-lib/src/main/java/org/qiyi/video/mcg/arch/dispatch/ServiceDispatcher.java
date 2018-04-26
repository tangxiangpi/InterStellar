package org.qiyi.video.mcg.arch.dispatch;

import android.os.IBinder;
import android.os.RemoteException;

import org.qiyi.video.mcg.arch.bean.BinderBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangallen on 2018/3/31.
 */

public class ServiceDispatcher implements IServiceDispatcher {

    private Map<String, String> interfaceCache = new HashMap<>();

    private Map<String, BinderBean> transferBinderCache = new HashMap<>();

    @Override
    public synchronized void registerRemoteService(String serviceInterface, final BinderBean transferBinderBean) {
        interfaceCache.put(serviceInterface, transferBinderBean.getProcessName());
        try {
            transferBinderBean.getBinder().linkToDeath(new IBinder.DeathRecipient() {
                @Override
                public void binderDied() {
                    transferBinderCache.remove(transferBinderBean.getProcessName());
                }
            }, 0);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } finally {
            transferBinderCache.put(transferBinderBean.getProcessName(), transferBinderBean);
        }

    }

    @Override
    public synchronized void unregisterRemoteService(String serviceInterface) {
        interfaceCache.remove(serviceInterface);
    }

    @Override
    public synchronized BinderBean getTransferBean(String serviceInterface) {
        return transferBinderCache.get(interfaceCache.get(serviceInterface));
    }
}
