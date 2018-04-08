package org.qiyi.video.mcg.arch.param.out;

import android.os.Parcel;

import java.util.List;

/**
 * Created by wangallen on 2018/4/8.
 */

public class ListType implements OutableType<List> {

    @Override
    public void writeToParcel(List value, Parcel dest, int flags) {
        //TODO

    }

    @Override
    public void readFromParcel(Parcel in, List value) {
        value.clear();
        int N = in.readInt();
        while (N > 0) {
            Object obj = in.readValue(getClass().getClassLoader());
            value.add(value);
            --N;
        }
    }
}
