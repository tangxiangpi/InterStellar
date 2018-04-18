package org.qiyi.video.mcg.arch.remote;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.qiyi.video.mcg.arch.bean.BinderBean;
import org.qiyi.video.mcg.arch.life.Lifecycle;
import org.qiyi.video.mcg.arch.life.LifecycleListener;
import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.utils.Utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wangallen on 2018/4/17.
 */

public class RemoteManager implements IRemoteManager, LifecycleListener {

    private Lifecycle lifecycle;
    private IRemoteManagerTreeNode treeNode;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Context appContext;

    private Set<String> commuStubServiceNames = new HashSet<>();

    public RemoteManager(Context context, final Lifecycle lifecycle, IRemoteManagerTreeNode treeNode) {
        this.appContext = context;
        this.lifecycle = lifecycle;
        this.treeNode = treeNode;

        if (Utils.isOnBackgroundThread()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    lifecycle.addListener(RemoteManager.this);
                }
            });
        } else {
            lifecycle.addListener(this);
        }

    }

    @Override
    public <T> T getRemoteService(Class<?> serviceInterface) {
        if (null == serviceInterface) {
            return null;
        }
        //TODO 这里除了获取proxy之外，还要获取对方进程的信息，比如BinderBean,目前其实是可以获取到的，关键就看怎么写了。
        T proxy = RemoteServiceManager.getInstance().getServiceProxy(serviceInterface);
        BinderBean binderBean = RemoteServiceManager.getInstance().getServerBinderBean(serviceInterface.getCanonicalName());
        if (binderBean != null && !TextUtils.isEmpty(binderBean.getProcessName())) {
            String stubServiceName = ConnectionManager.getInstance().bindAction(appContext, binderBean.getProcessName());
            commuStubServiceNames.add(stubServiceName);
        }
        return proxy;
    }

    @Override
    public void onStart() {
        Logger.d(this.toString() + "-->onStart()");
    }

    @Override
    public void onStop() {
        Logger.d(this.toString() + "-->onStop()");
    }

    @Override
    public void onDestroy() {
        //TODO unbindAction
        Logger.d(this.toString() + "-->onDestroy()");
        ConnectionManager.getInstance().unbindAction(appContext, commuStubServiceNames);
    }
}

