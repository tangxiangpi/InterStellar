package org.qiyi.video.mcg_arch.service;

import android.os.Parcel;
import android.os.Parcelable;

import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.parcelable.Outable;

/**
 * Created by wangallen on 2018/4/8.
 */

public class Apple implements Parcelable, Outable {

    private float weight;
    private String from;

    public Apple(float weight, String from) {
        this.weight = weight;
        this.from = from;
    }

    public Apple(Parcel in) {
        this.weight = in.readFloat();
        this.from = in.readString();
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(weight);
        dest.writeString(from);
    }

    @Override
    public void readFromParcel(Parcel in) {
        Logger.d("Apple-->readFromParcel");
        this.weight = in.readFloat();
        this.from = in.readString();
    }

    public static final Creator<Apple> CREATOR = new Creator<Apple>() {
        @Override
        public Apple createFromParcel(Parcel source) {
            return new Apple(source);
        }

        @Override
        public Apple[] newArray(int size) {
            return new Apple[size];
        }
    };

    @Override
    public String toString() {
        return "weight:" + weight + ",from:" + from;
    }

}
