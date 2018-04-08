package org.qiyi.video.mcg.arch.param.out;

import android.os.Parcel;
import android.os.Parcelable;

import org.qiyi.video.mcg.arch.exception.StellarException;
import org.qiyi.video.mcg.arch.parcelable.Outable;
import org.qiyi.video.mcg.arch.utils.ReflectUtils;

import java.lang.reflect.Method;

/**
 * Created by wangallen on 2018/4/8.
 */

public class ParcelableType implements OutableType<Parcelable> {

    @Override
    public void writeToParcel(Parcelable value, Parcel dest, int flags) {
        if(flags==Parcelable.PARCELABLE_WRITE_RETURN_VALUE){
            value.writeToParcel(dest,flags);
        }else{
            dest.writeParcelable(value,flags);
        }
    }

    @Override
    public void readFromParcel(Parcel in, Parcelable value) {
        if (value instanceof Outable) {
            ((Outable) value).readFromParcel(in);
            return;
        }
        Method readFromParcelMethod = ReflectUtils.getMethod(value.getClass(), "readFromParcel", Parcel.class);
        if (null == readFromParcelMethod) {
            throw new StellarException(value.getClass().getCanonicalName() + " should implement Outable cause it's annotated with @out or @inout!");
        }
        try {
            readFromParcelMethod.invoke(value, in);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
