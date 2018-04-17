package org.qiyi.video.mcg.arch.param;

import android.os.Parcel;

import org.qiyi.video.mcg.arch.log.Logger;

/**
 * Created by wangallen on 2018/4/4.
 */

public class OutParameter extends AbstractParameter {

    public OutParameter(Object param, java.lang.reflect.Type genericParaType) {
        this.value = param;
    }

    public OutParameter(Parcel in) {
        //只需要创建新对象即可
        this.value = extendReadType(getClass().getClassLoader(), in);
        Logger.d("OutParameter,value.getClass():" + value.getClass().getCanonicalName());
        //TODO 这样不行的，因为比如int[]不能强制转换为Object[]
        /*
        if (value.getClass().isArray()) {
            Logger.d("length of value:" + ((Object[]) value).length);
        }
        */
    }

    //TODO 其实像这样还是做复杂了，测试一下传递null时能否创建对象即可.
    //TODO 这样子是不行的，如果想要只读取类型的话，那么写入时也只能写入类型!否则就必须把值读出来，不然会影响后面其他字段的读取!
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //要有返回值时，flags就是PARCELABLE_WRITE_RETURN_VALUE,一般发生在server序列化到client端时。
        if (flags == PARCELABLE_WRITE_RETURN_VALUE) {
            Logger.d("OutParameter-->writeToParcel()");
            extendWriteValue2Parcel(dest, flags);
        } else {
            extendWriteType(value, dest, flags);
        }
    }

    //TODO 运行时会抛出java.lang.RuntimeException: Parcel android.os.Parcel@a95e7ce: Unmarshalling unknown type code 51 at offset 92
    //TODO 关键是要让它调用到各种参数类型的readFromParcel()方法，所以不能简单地调用in.readValue(...)来读取
    //KP 对应的是writeToParcel()方法中flags==PARCELABLE_WRITE_RETURN_VALUE这种情形
    @Override
    public void readFromParcel(Parcel in) {
        Logger.d("OutParameter-->readFromParcel()");
        readValueFromParcel(in);
    }

    public static final Creator<OutParameter> CREATOR = new Creator<OutParameter>() {
        @Override
        public OutParameter createFromParcel(Parcel source) {
            return new OutParameter(source);
        }

        @Override
        public OutParameter[] newArray(int size) {
            return new OutParameter[size];
        }
    };

}
