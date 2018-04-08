// IServiceRegister.aidl
package org.qiyi.video.mcg.arch.dispatch;
import org.qiyi.video.mcg.arch.bean.BinderBean;
// Declare any non-default types here with import statements

interface IServiceRegister {

    void registerRemoteService(String serviceInterface,in BinderBean transferBinderBean);

    void unregisterRemoteService(String serviceInterface);

    BinderBean getTransferInfo(String serviceInterface);
}
