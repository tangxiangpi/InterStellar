package org.qiyi.video.mcg.arch.param;

import android.os.Parcel;

import org.qiyi.video.mcg.arch.log.Logger;

/**
 * Created by wangallen on 2018/4/4.
 */

public class OutParameter extends AbstractParameter {

    public OutParameter(Object param, java.lang.reflect.Type genericParaType) {
        this.value = param;
    }

    public OutParameter(Parcel in) {
        //只需要创建新对象即可
        this.value = extendReadType(getClass().getClassLoader(), in);
        Logger.d("OutParameter,value.getClass():" + value.getClass().getCanonicalName());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //要有返回值时，flags就是PARCELABLE_WRITE_RETURN_VALUE,一般发生在server序列化到client端时。
        if (flags == PARCELABLE_WRITE_RETURN_VALUE) {
            Logger.d("OutParameter-->writeToParcel()");
            extendWriteValue2Parcel(dest, flags);
        } else {
            extendWriteType(value, dest, flags);
        }
    }

    //KP 对应的是writeToParcel()方法中flags==PARCELABLE_WRITE_RETURN_VALUE这种情形
    @Override
    public void readFromParcel(Parcel in) {
        Logger.d("OutParameter-->readFromParcel()");
        readValueFromParcel(in);
    }

    public static final Creator<OutParameter> CREATOR = new Creator<OutParameter>() {
        @Override
        public OutParameter createFromParcel(Parcel source) {
            return new OutParameter(source);
        }

        @Override
        public OutParameter[] newArray(int size) {
            return new OutParameter[size];
        }
    };

}
