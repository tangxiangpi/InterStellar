package org.qiyi.video.mcg.arch.parcelable;

import android.os.Parcel;

/**
 * 可以用out修饰的参数需要实现这个接口
 * Created by wangallen on 2018/4/2.
 */

public interface Outable {
    void readFromParcel(Parcel in);
}
