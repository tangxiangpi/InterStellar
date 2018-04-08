package org.qiyi.video.mcg.arch.transfer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangallen on 2018/3/31.
 */

public class Reply implements Parcelable {

    public static int NO_SERVICE=-1;
    public static int NO_METHOD=-2;
    public static int EXECUTE_ERR=-3;

    public static int SUCCEED=1;

    private int code;
    private String msg;
    private Object result;

    public Reply(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public Reply(Parcel in) {
        this.code = in.readInt();
        this.msg = in.readString();
        this.result = in.readValue(getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(msg);
        dest.writeValue(result);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getResult() {
        return result;
    }

    public static final Creator<Reply> CREATOR = new Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel source) {
            return new Reply(source);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };


}
