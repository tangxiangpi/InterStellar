package org.qiyi.video.mcg.arch.dispatch;

import org.qiyi.video.mcg.arch.bean.BinderBean;

/**
 * Created by wangallen on 2018/3/31.
 */

public interface IServiceDispatcher {

    void registerRemoteService(String serviceInterface, BinderBean transferBinderBean);

    void unregisterRemoteService(String serviceInterface);

    BinderBean getTransferBean(String serviceInterface);

}
