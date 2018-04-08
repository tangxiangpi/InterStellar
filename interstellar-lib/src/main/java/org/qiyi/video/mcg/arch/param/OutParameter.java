package org.qiyi.video.mcg.arch.param;

import android.os.Parcel;
import android.os.Parcelable;

import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg.arch.param.out.MapType;
import org.qiyi.video.mcg.arch.param.out.OutableType;
import org.qiyi.video.mcg.arch.param.out.ParcelableType;
import org.qiyi.video.mcg.arch.type.BaseType;
import org.qiyi.video.mcg.arch.type.Type;

/**
 * Created by wangallen on 2018/4/4.
 */

public class OutParameter extends AbstractParameter {

    public OutParameter(Object param, java.lang.reflect.Type genericParaType) {
        super(param, genericParaType);
        this.value = param;
    }

    public OutParameter(Parcel in) {
        //只需要创建新对象即可
        this.type=in.readInt();
        this.baseType=in.readInt();

        this.value = extendReadType(getClass().getClassLoader(), in);
    }

    //TODO 其实像这样还是做复杂了，测试一下传递null时能否创建对象即可.
    //TODO 这样子是不行的，如果想要只读取类型的话，那么写入时也只能写入类型!否则就必须把值读出来，不然会影响后面其他字段的读取!
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //extendWriteValue(dest,value,flags);
        dest.writeInt(type);
        dest.writeInt(baseType);

        //要有返回值时，flags就是PARCELABLE_WRITE_RETURN_VALUE,一般发生在server序列化到client端时。
        if (flags == PARCELABLE_WRITE_RETURN_VALUE) {
            Logger.d("OutParameter-->outableType.writeToParcel()");
            //TODO 不能那么简单粗暴，需要让各种类型调用到它们各自的writeToParcel()方法，而extendWriteValue()做不到这一点
            //TODO 但是仔细对比下来，发现其实也就是Parcelable和ParcelableArray的writeToParcel()和readFromParcel()方法需要特殊处理，其他的都可以用Parcel已有的方法解决
            //extendWriteValue(dest, value, flags);
            OutableType outableType=chooseType();
            outableType.writeToParcel(value,dest,flags);
        } else {
            extendWriteType(value, dest, flags);
        }
    }


    private OutableType chooseType(){
        if(type==Type.BASE){
            if(baseType==BaseType.PARCELABLE){
                return getParcelableType();
            }
        }
        return null;
    }

    private ParcelableType getParcelableType(){
        if (parcelableType == null) {
            parcelableType = new ParcelableType();
        }
        return parcelableType;
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
    //TODO 关键是要让它调用到各种参数类型的readFromParcel()方法，所以不能简单地调用in.readValue(...)来读取
    //KP 对应的是writeToParcel()方法中flags==PARCELABLE_WRITE_RETURN_VALUE这种情形
    @Override
    public void readFromParcel(Parcel in) {
        //TODO 这种方式可行吗？或者说怎么保证它写入的时候是以writeValue()进行写入的呢？
        //this.value = in.readValue(getClass().getClassLoader());
        //KP 还要先读出type(因为extendWriteValue()时会先写入类型),然后再调用readFromParcel读出值
        //TODO 注意:这种方式只是针对Parcelable的，而对于List和Map,则不是这个规则，所以下面这种方式不全面!
        //跟extendWriteValue()中的类型对应
        Logger.d("OutParameter-->readFromParcel()");
        type = in.readInt();
        baseType=in.readInt();
        OutableType outableType=chooseType();
        outableType.readFromParcel(in,value);
        /*
        switch (type) {
            case VAL_MAP:

                break;
            case VAL_BUNDLE:

                break;
            case VAL_PARCELABLE: {
                parcelableType.readFromParcel(in, (Parcelable) value);
            }
            break;
            case VAL_PARCELABLEARRAY:

                break;
            case VAL_LIST:

                break;
            case VAL_BYTEARRAY:

                break;
            case VAL_STRINGARRAY:

                break;
            case VAL_OBJECTARRAY:

                break;
            case VAL_INTARRAY:

                break;
            case VAL_LONGARRAY:

                break;
            case VAL_SPARSEBOOLEANARRAY:

                break;
            case VAL_BOOLEANARRAY:

                break;
            case VAL_CHARSEQUENCEARRAY:

                break;
            case VAL_DOUBLEARRAY:

                break;
        }
        */
    }

    private MapType mapType;
    private ParcelableType parcelableType;

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
