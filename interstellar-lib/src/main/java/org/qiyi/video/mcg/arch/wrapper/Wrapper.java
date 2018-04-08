package org.qiyi.video.mcg.arch.wrapper;

import android.os.Parcel;

/**
 * Created by wangallen on 2018/3/31.
 */

public interface Wrapper<T> {

    //void writeToParcel(Parcel dest,int flags, T value);

    //T readFromParcel(Parcel in);

    void writeToParcel(Parcel dest,int flags);

    T readFromParcel(Parcel in);





}
