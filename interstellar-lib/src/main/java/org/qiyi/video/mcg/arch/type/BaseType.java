package org.qiyi.video.mcg.arch.type;

/**
 * 不管是Array还是
 * Created by wangallen on 2018/4/2.
 */
//TODO 其实AIDL是可以支持包装类的，看一下Parcel中的writeValue(Object)就知道了，不过目前尚不确定是否所有版本都支持
public interface BaseType {

    //未知或者说不care,比如为List时
    int UNKNOWN=0;

    int INT = 1;
    int SHORT = 2;
    int LONG = 3;
    int FLOAT = 4;
    int DOUBLE = 5;
    int BYTE = 6;
    int BOOLEAN = 7;
    int CHAR = 8;
    int CHARSEQUENCE=9;
    int STRING = 10;
    int PARCELABLE=11;

    //TODO 这种类型比较特殊，但是其实AIDL确实是支持的
    int IBINDER=12;
    int BUNDLE=13;

}
