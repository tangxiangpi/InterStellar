package org.qiyi.video.mcg.arch.param;

import android.os.Parcel;

import org.qiyi.video.mcg.arch.parcelable.Outable;

/**
 * Created by wangallen on 2018/4/7.
 */

public class InOutParameter extends AbstractParameter implements Outable {

    public InOutParameter(Object param, java.lang.reflect.Type genericParaType) {
        this.value = param;
    }

    public InOutParameter(Parcel in) {
        this.value = in.readValue(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        extendWriteValue(dest, value, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        this.value = in.readValue(getClass().getClassLoader());
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
