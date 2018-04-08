package org.qiyi.video.mcg.arch.param;

import android.os.Parcel;

import org.qiyi.video.mcg.arch.exception.StellarException;
import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.parcelable.Outable;
import org.qiyi.video.mcg.arch.type.BaseType;
import org.qiyi.video.mcg.arch.type.Type;
import org.qiyi.video.mcg.arch.utils.ReflectUtils;

import java.lang.reflect.Method;

/**
 * Created by wangallen on 2018/4/4.
 */

public class OutParameter extends AbstractParameter implements Outable {

    public OutParameter(Object param, java.lang.reflect.Type genericParaType) {
        //super(param, genericParaType);
        this.value = param;
    }

    public OutParameter(Parcel in) {
        //只需要创建新对象即可
        /*
        this.type=in.readInt();
        this.baseType=in.readInt();
        if(type==Type.BASE&&baseType==BaseType.PARCELABLE){
            String className=in.readString();
            value=
        }
        */
        //this.value=in.readValue(getClass().getClassLoader());

        this.value = extendReadType(getClass().getClassLoader(), in);
    }

    //TODO 其实像这样还是做复杂了，测试一下传递null时能否创建对象即可.
    //TODO 这样子是不行的，如果想要只读取类型的话，那么写入时也只能写入类型!否则就必须把值读出来，不然会影响后面其他字段的读取!
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //extendWriteValue(dest,value,flags);

        if (flags == PARCELABLE_WRITE_RETURN_VALUE) {
            extendWriteValue(dest, value, flags);
        } else {
            extendWriteType(value, dest, flags);
        }


        /*
        if (flags == PARCELABLE_WRITE_RETURN_VALUE) {
            extendWriteValue(dest, value, flags);
        } else {
            dest.writeValue(value);
        }
        */
    }


    private void writeType(Parcel dest, int flags) {
        //TODO 这里如果要想简单的话，就像InParameter一样writeValue()，如果要做得精细一点，就不要writeValue(),而是只writeType()即可
        //TODO 不对，好像也不行，因为如果client端传入的是null的话，难道在server端就不新建对象了，显然这样不合理。
        //TODO 所以还是要用writeType()和readType()
        dest.writeInt(type);
        dest.writeInt(baseType);
        if (flags == PARCELABLE_WRITE_RETURN_VALUE) {
            extendWriteValue(dest, value, flags);
        } else {
            if (type == Type.BASE && baseType == BaseType.PARCELABLE) {
                //TODO 这个应该写入value.getClass().getCanonicalName()吧？
                dest.writeString(value.getClass().getName());
            } else {
                if (type == Type.ARRAY) {
                    dest.writeInt(getArrayLength());
                    if (baseType == BaseType.PARCELABLE) {
                        dest.writeString(value.getClass().getComponentType().getName());
                    } else {
                        //TODO 其他类型不用写入类名吗?确实不用，有type和baseType就足够了
                        //TODO 其实还有IBinder和BUNDLE吧
                        //do nothing
                    }
                }//TODO 不用考虑List和Map吗?
            }
        }
    }

    //TODO 运行时会抛出java.lang.RuntimeException: Parcel android.os.Parcel@a95e7ce: Unmarshalling unknown type code 51 at offset 92
    @Override
    public void readFromParcel(Parcel in) {
        //TODO 这种方式可行吗？或者说怎么保证它写入的时候是以writeValue()进行写入的呢？
        //this.value = in.readValue(getClass().getClassLoader());
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
        Logger.d("OutParameter-->readFromParcel(),value:" + value.toString());
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
