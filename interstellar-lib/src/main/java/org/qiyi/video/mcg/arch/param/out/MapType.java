package org.qiyi.video.mcg.arch.param.out;

import android.os.Parcel;

import java.util.Map;

/**
 * Created by wangallen on 2018/4/8.
 */

public class MapType implements OutableType<Map> {

    @Override
    public void writeToParcel(Map value, Parcel dest, int flags) {
        //TODO
    }

    @Override
    public void readFromParcel(Parcel in, Map value) {
        ClassLoader loader = getClass().getClassLoader();
        value.clear();
        int N = in.readInt();
        while (N > 0) {
            Object key = in.readValue(loader);
            Object val = in.readValue(loader);
            value.put(key, val);
            --N;
        }
    }
}
