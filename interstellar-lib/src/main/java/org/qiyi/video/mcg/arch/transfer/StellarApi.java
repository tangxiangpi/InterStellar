package org.qiyi.video.mcg.arch.transfer;

import android.os.RemoteException;

import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.remote.RemoteServiceManager;


/**
 * Created by wangallen on 2018/4/7.
 */

public class StellarApi implements IApi {

    private String serviceCanonicalName;

    /**
     * @param serviceCanonicalName 服务接口名称
     */
    public StellarApi(String serviceCanonicalName) {
        this.serviceCanonicalName = serviceCanonicalName;
    }

    public String getServiceCanonicalName() {
        return serviceCanonicalName;
    }

    @Override
    public Reply invoke(StellarMethod method) throws RemoteException {
        if (null == method) {
            return null;
        }
        Logger.i("StellarApi-->invoke,method:" + method.getMethodName());
        ITransfer transfer = RemoteServiceManager.getInstance().getRemoteTransfer(serviceCanonicalName);
        if (null == transfer) {
            return new Reply(Reply.NO_SERVICE, null, null);
        }
        return transfer.invoke(serviceCanonicalName, method);
    }
}
