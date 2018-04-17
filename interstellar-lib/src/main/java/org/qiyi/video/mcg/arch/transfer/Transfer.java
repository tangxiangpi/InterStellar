package org.qiyi.video.mcg.arch.transfer;

import android.os.Parcelable;
import android.os.RemoteException;

import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.param.AbstractParameter;
import org.qiyi.video.mcg.arch.remote.RemoteServiceManager;
import org.qiyi.video.mcg.arch.utils.ProcessUtils;
import org.qiyi.video.mcg.arch.utils.ReflectUtils;

import java.lang.reflect.Method;

/**
 * Created by wangallen on 2018/3/31.
 */

public class Transfer extends ITransfer.Stub {

    private static Transfer sInstance;

    public static Transfer getInstance() {
        if (null == sInstance) {
            synchronized (Transfer.class) {
                if (null == sInstance) {
                    sInstance = new Transfer();
                }
            }
        }
        return sInstance;
    }

    private Transfer() {
    }

    @Override
    public Reply invoke(String serviceInterface, StellarMethod method) throws RemoteException {
        Logger.d("Transfer-->invoke,serviceInterface:" + serviceInterface + ",method name:" + method.getMethodName()+",process:"+ ProcessUtils.getProcessName(android.os.Process.myPid()));
        //TODO 这样直接调用RemoteServiceManager是不是不太好？要不要改一下架构，让Transfer处于上层呢?
        Object serivceImpl = RemoteServiceManager.getInstance().getStubService(serviceInterface);
        if (null == serivceImpl) {
            Logger.d("serviceImpl is null");
            return new Reply(Reply.NO_SERVICE, null, null);
        } else {
            Method targetMethod = ReflectUtils.getMethod(serivceImpl.getClass(), method.getMethodName(),
                    getParameterTypes(method.getParameters()));
            if (null == targetMethod) {
                Logger.d("no such method");
                return new Reply(Reply.NO_METHOD, null, null);
            }
            try {
                Object result = targetMethod.invoke(serivceImpl, getParameters(method.getParameters()));
                Logger.d("Transfer-->invoke succeed!");
                //TODO 只是这样还不够，因为还没有处理in,out等! 所以Stub和Proxy需要自己写
                return new Reply(Reply.SUCCEED, null, result);
            } catch (Exception ex) {
                ex.printStackTrace();
                return new Reply(Reply.EXECUTE_ERR, ex.getMessage(), null);
            }
        }
    }
    //TODO 这样是不行的，对于inout的情况，比如类型为Apple[],但是读取出来为什么是Parcelable[]?那说明InOutParameter一定是哪里还有问题
    private Class[] getParameterTypes(AbstractParameter[] parameters) {
        if (null == parameters || parameters.length < 1) {
            return null;
        }
        Class[] paraTypes = new Class[parameters.length]; //打log发现int传递过来之后竟然会变成java.lang.Integer
        for (int i = 0; i < parameters.length; ++i) {
            paraTypes[i] = stripWrapper(parameters[i].getValue().getClass());
        }
        Logger.d("Transfer-->getParameterTypes()");
        return paraTypes;
    }

    /**
     * 将包装类还原
     * @param wrapperClazz
     * @return
     */
    private Class stripWrapper(Class<?>wrapperClazz){
        //如果已经是原始类型，就不用还原了
        if(wrapperClazz.isPrimitive()){
            return wrapperClazz;
        }
        if(wrapperClazz==Integer.class){
            return int.class;
        }
        if(wrapperClazz==Short.class){
            return short.class;
        }
        if(wrapperClazz==Long.class){
            return long.class;
        }
        if(wrapperClazz==Float.class){
            return float.class;
        }
        if(wrapperClazz==Double.class){
            return double.class;
        }
        if(wrapperClazz==Boolean.class){
            return boolean.class;
        }
        if(wrapperClazz==Byte.class){
            return byte.class;
        }
        if(wrapperClazz==Character.class){
            return char.class;
        }
        return wrapperClazz;
    }

    private Object[] getParameters(AbstractParameter[] parameters) {
        if (null == parameters || parameters.length < 1) {
            return null;
        }
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            args[i] = parameters[i].getValue();
        }
        Logger.d("Transfer-->getParameters()");
        return args;
    }

}
