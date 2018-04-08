package org.qiyi.video.mcg.arch.param;

import android.os.Parcel;

/**
 * Created by wangallen on 2018/4/3.
 */

public class InParameter extends AbstractParameter {

    public InParameter(Object param, java.lang.reflect.Type genericParaType) {
        //super(param,genericParaType);
        this.value = param;
    }

    public InParameter(Parcel in) {
        value = in.readValue(getClass().getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        extendWriteValue(dest,value,flags);
    }

    public static final Creator<InParameter> CREATOR = new Creator<InParameter>() {
        @Override
        public InParameter createFromParcel(Parcel source) {
            return new InParameter(source);
        }

        @Override
        public InParameter[] newArray(int size) {
            return new InParameter[size];
        }
    };

}
