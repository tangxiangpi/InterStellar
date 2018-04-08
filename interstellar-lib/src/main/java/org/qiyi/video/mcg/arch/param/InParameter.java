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
        /*
        if (flags != PARCELABLE_WRITE_RETURN_VALUE) {
            //dest.writeInt(type);  //对于InParameter来说，type和baseType都是多余的
            //dest.writeInt(baseType);
            //TODO 能否直接用writeValue()和readValue()搞定呢?
            //TODO 值得尝试一下!
            //TODO 考虑到parcelableFlags,写一个扩展的writeValue()方法即可!
            dest.writeValue(value);
        }
        */
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
