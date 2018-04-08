package org.qiyi.video.mcg.arch.param.out;

import android.os.Parcel;

/**
 * Created by wangallen on 2018/4/8.
 */

public interface OutableType<T> {
    void writeToParcel(T value, Parcel dest, int flags);

    void readFromParcel(Parcel in, T value);
}
