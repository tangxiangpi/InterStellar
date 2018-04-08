package org.qiyi.video.mcg.arch.utils;

import java.lang.reflect.Method;

/**
 * Created by wangallen on 2018/4/7.
 */

public final class ReflectUtils {

    private ReflectUtils(){}

    public static Method getMethod(Class<?>clazz,String methodName,Class<?>...args){
        try{
            Method method=clazz.getMethod(methodName,args);
            method.setAccessible(true);
            return method;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

}
