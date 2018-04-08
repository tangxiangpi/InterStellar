package org.qiyi.video.mcg.arch.transfer;

import org.qiyi.video.mcg.arch.log.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by wangallen on 2018/4/7.
 */

public class ServiceHandler implements InvocationHandler {

    private StellarApi api;

    public ServiceHandler(String serviceCanonicalName) {
        this.api = StellarApiManager.getInstance().getStellarApi(serviceCanonicalName);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Logger.d("ServiceHandler-->invoke,method name:" + method.getName());
        StellarMethod stellarMethod = new StellarMethod(method, args);
        Reply reply = api.invoke(stellarMethod);
        if (reply != null && reply.getCode() == Reply.SUCCEED) {
            return reply.getResult();
        }
        return null;
    }
}
