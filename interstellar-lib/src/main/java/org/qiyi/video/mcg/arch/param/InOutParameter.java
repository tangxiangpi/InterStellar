package org.qiyi.video.mcg.arch.param;

import android.os.Parcel;

import org.qiyi.video.mcg.arch.log.Logger;

/**
 * Created by wangallen on 2018/4/7.
 */

public class InOutParameter extends AbstractParameter {

    public InOutParameter(Object param, java.lang.reflect.Type genericParaType) {
        this.value = param;
    }

    public InOutParameter(Parcel in) {
        //this.value = in.readValue(getClass().getClassLoader());
        this.value = extendReadValue(in, getClass().getClassLoader());
        Logger.d("InOutParameter-->readValue() done");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (flags == PARCELABLE_WRITE_RETURN_VALUE) {
            extendWriteValue2Parcel(dest, flags);
        } else {
            extendWriteValue(dest, value, flags);
        }
    }

    @Override
    public void readFromParcel(Parcel in) {
        readValueFromParcel(in);
    }

    public static final Creator<InOutParameter> CREATOR = new Creator<InOutParameter>() {
        @Override
        public InOutParameter createFromParcel(Parcel source) {
            return new InOutParameter(source);
        }

        @Override
        public InOutParameter[] newArray(int size) {
            return new InOutParameter[size];
        }
    };
}
