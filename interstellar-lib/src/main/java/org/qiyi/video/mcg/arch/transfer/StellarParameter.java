package org.qiyi.video.mcg.arch.transfer;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import org.qiyi.video.mcg.arch.exception.StellarException;
import org.qiyi.video.mcg.arch.parcelable.Outable;
import org.qiyi.video.mcg.arch.type.BaseType;
import org.qiyi.video.mcg.arch.type.TransferType;
import org.qiyi.video.mcg.arch.type.Type;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * Created by wangallen on 2018/3/31.
 */
//TODO AIDL的分裂之处在于，对于List<Integer>这种类型可以支持，但是对于Integer和Integer[]却不支持

public class StellarParameter implements Parcelable,Outable {

    private int type;
    private int baseType;
    private int transferType = TransferType.IN;
    //参数值
    private Object value;


    public static final Parcelable.Creator<StellarParameter> CREATOR = new Parcelable.Creator<StellarParameter>() {
        @Override
        public StellarParameter createFromParcel(Parcel source) {
            return new StellarParameter(source);
        }

        @Override
        public StellarParameter[] newArray(int size) {
            return new StellarParameter[size];
        }
    };

    /**
     * @param param
     * @param annotations
     * @param genericParaType
     */
    public StellarParameter(Object param, Annotation[] annotations, java.lang.reflect.Type genericParaType) {
        parseTypeAndBaseType(param.getClass(), genericParaType);
        parseTransferType(annotations);
        this.value = param;
    }

    //TODO 这里其实还要加一个注解解释器，在里面检查参数是否需要加in,out，以及in, out等是否与当前参数类型相冲突，这样就可以在编译时进行提示
    //TODO 而不是在运行时再抛出错误了!
    private void parseTransferType(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof org.qiyi.video.mcg.arch.annotation.in) {
                transferType = TransferType.IN;
                break;
            } else if (annotation instanceof org.qiyi.video.mcg.arch.annotation.out) {
                transferType = TransferType.OUT;
                break;
            } else if (annotation instanceof org.qiyi.video.mcg.arch.annotation.inout) {
                transferType = TransferType.INOUT;
                break;
            }
        }
    }

    public StellarParameter(Parcel in) {
        this.type = in.readInt();
        this.baseType = in.readInt();
        this.transferType = in.readInt();
        parseValue(in);
    }

    private void parseValue(Parcel in) {
        switch (type) {
            case Type.BASE:
                parseValue4Base(in);
                break;
            case Type.ARRAY:

                break;
            case Type.LIST:

                break;
            case Type.MAP:

                break;
        }
    }

    private void parseValue4Base(Parcel in) {
        if (baseType == BaseType.INT) {
            this.value = in.readInt();
        } else if (baseType == BaseType.SHORT) {
            this.value = (short) (in.readInt());
        } else if (baseType == BaseType.LONG) {
            this.value = in.readLong();
        } else if (baseType == BaseType.FLOAT) {
            this.value = in.readFloat();
        } else if (baseType == BaseType.DOUBLE) {
            this.value = in.readDouble();
        } else if (baseType == BaseType.BYTE) {
            this.value = in.readByte();
        } else if (baseType == BaseType.BOOLEAN) {
            this.value = in.readInt() == 1;
        } else if (baseType == BaseType.CHAR) {
            this.value = (char) (in.readInt());
        } else if (baseType == BaseType.CHARSEQUENCE) {
            throw new StellarException("Charsequence array is not supported by InterStellar yet!");
        } else if (baseType == BaseType.STRING) {
            this.value = in.readString();
        } else if (baseType == BaseType.PARCELABLE) {
            parseValue4Parcelable(in);
        } else if (baseType == BaseType.IBINDER) {
            this.value = in.readStrongBinder();
        } else if (baseType == BaseType.BUNDLE) {
            parseValue4Bundle(in);
        }
    }

    private void parseValue4Parcelable(Parcel in) {
        //TODO 要考虑in,out和inout的问题
        this.value = in.readParcelable(getClass().getClassLoader());

        /*
        //out的情形
        if(0!=in.readInt()){

        }else{
            this.value=null;
        }
        */
    }

    private void parseInValue4Parcelable(Parcel in) {
        //in的情形则要调用XX.CREATOR.createFromParcel(in);来获取对象
        //所以这样看来，还是要保存类名?
        this.value = in.readParcelable(getClass().getClassLoader());
    }

    private void parseValue4Bundle(Parcel in) {
        if (0 != in.readInt()) {
            this.value = Bundle.CREATOR.createFromParcel(in);
        } else {
            this.value = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //TODO 注意:这里不仅跟flags有关，还跟in,out修饰符有关
        dest.writeInt(type);
        dest.writeInt(baseType);
        dest.writeInt(transferType);

        //TODO 其实如果简单粗暴一点的话，可以不分什么in,out,而是在写入时把所有参数都写入
        //TODO 在读取时把所有参数都读取
        if (flags == PARCELABLE_WRITE_RETURN_VALUE) {
            //TODO

        } else {
            writeParcelAction(dest, flags);
        }

    }

    @Override
    public void readFromParcel(Parcel in) {

    }

    private void writeParcelAction(Parcel dest, int flags) {
        if (type == Type.BASE) {
            writeBase2Parcel(dest, flags);
        } else if (type == Type.ARRAY) {
            writeArray2Parcel(dest, flags);
        } else if (type == Type.LIST) {
            writeList2Parcel(dest, flags);
        } else if (type == Type.MAP) {
            writeMap2Parcel(dest, flags);
        }
    }

    private void writeMap2Parcel(Parcel dest, int flags) {
        switch (transferType) {
            case TransferType.IN:
                writeInMap(dest, flags);
                break;
            case TransferType.OUT:
                //do nothing
                break;
            case TransferType.INOUT:
                writeInMap(dest, flags);
                break;
        }
    }

    private void writeInMap(Parcel dest, int flags) {
        dest.writeMap((Map) value);
    }

    //TODO 注意:List的话比较特殊，因为List中可以有包装类，而且Parcel是支持的!
    private void writeList2Parcel(Parcel dest, int flags) {
        switch (transferType) {
            case TransferType.IN:
                writeInList(dest, flags);
                break;
            case TransferType.OUT:
                //do nothing
                break;
            case TransferType.INOUT:
                writeInList(dest, flags);
                break;
        }
    }

    private void writeInList(Parcel dest, int flags) {
        dest.writeList((List) value);
    }

    private void writeArray2Parcel(Parcel dest, int flags) {
        switch (transferType) {
            case TransferType.IN:
                writeInArray(dest, flags);
                break;
            case TransferType.OUT:
                writeOutArray(dest);
                break;
            case TransferType.INOUT:
                writeInArray(dest, flags);
                break;
        }
    }

    //TODO 是不是可以不用这么麻烦，直接一个writeArray()就可以的呢？
    private void writeInArray(Parcel dest, int flags) {
        if (baseType == BaseType.INT || baseType == BaseType.SHORT) {
            dest.writeIntArray((int[]) value);
        } else if (baseType == BaseType.LONG) {
            dest.writeLongArray((long[]) value);
        } else if (baseType == BaseType.FLOAT) {
            dest.writeFloatArray((float[]) value);
        } else if (baseType == BaseType.DOUBLE) {
            dest.writeDoubleArray((double[]) value);
        } else if (baseType == BaseType.BYTE) {
            dest.writeByteArray((byte[]) value);
        } else if (baseType == BaseType.BOOLEAN) {
            dest.writeBooleanArray((boolean[]) value);
        } else if (baseType == BaseType.CHAR) {
            dest.writeCharArray((char[]) value);
        } else if (baseType == BaseType.CHARSEQUENCE) {
            //TODO 后面考虑扩展进行支持，目前先跟原生的AIDL保持一致吧!
            throw new StellarException("Charsequence array is not supported by InterStellar yet!");
        } else if (baseType == BaseType.STRING) {
            dest.writeStringArray((String[]) value);
        } else if (baseType == BaseType.PARCELABLE) {
            dest.writeTypedArray((Parcelable[]) value, flags);
        }
    }

    private void writeOutArray(Parcel dest) {
        if (null == value) {
            dest.writeInt(-1);
        } else {
            dest.writeInt(getArrayLength());
        }
    }


    private int getArrayLength() {
        if (baseType == BaseType.INT) {
            return ((int[]) value).length;
        } else if (baseType == BaseType.SHORT) {
            return ((short[]) value).length;
        } else if (baseType == BaseType.LONG) {
            return ((long[]) value).length;
        } else if (baseType == BaseType.FLOAT) {
            return ((float[]) value).length;
        } else if (baseType == BaseType.DOUBLE) {
            return ((double[]) value).length;
        } else if (baseType == BaseType.BYTE) {
            return ((byte[]) value).length;
        } else if (baseType == BaseType.BOOLEAN) {
            return ((boolean[]) value).length;
        } else if (baseType == BaseType.CHAR) {
            return ((char[]) value).length;
        } else if (value instanceof Object[]) {
            return ((Object[]) value).length;
        }
        throw new StellarException(value.getClass().getCanonicalName() + " is not supported by InterStellar!");
    }

    /**
     * BaseType除了PARCELABLE和Bundle之外，其他只能用in修饰，所以比较简单
     *
     * @param dest
     */
    private void writeBase2Parcel(Parcel dest, int flags) {
        if (baseType == BaseType.INT) {
            dest.writeInt((int) value);
        } else if (baseType == BaseType.SHORT) {
            dest.writeInt((int) value);
        } else if (baseType == BaseType.LONG) {
            dest.writeLong((long) value);
        } else if (baseType == BaseType.FLOAT) {
            dest.writeFloat((float) value);
        } else if (baseType == BaseType.DOUBLE) {
            dest.writeDouble((double) value);
        } else if (baseType == BaseType.BYTE) {
            dest.writeByte((byte) value);
        } else if (baseType == BaseType.BOOLEAN) {
            dest.writeInt((boolean) value ? 1 : 0);
        } else if (baseType == BaseType.CHAR) {
            dest.writeInt((int) value);
        } else if (baseType == BaseType.CHARSEQUENCE) { //TODO 如果后面打算扩展的话，Parcelable之外用一句dest.writeValue()就解决了!
            //TODO 后续考虑支持Charsequence
            throw new StellarException("Charsequence is not supported by InterStellar yet!");
        } else if (baseType == BaseType.STRING) {
            dest.writeString((String) value);
        } else if (baseType == BaseType.PARCELABLE) {
            writeParcelable(dest, flags);
        } else if (baseType == BaseType.IBINDER) {
            dest.writeStrongBinder((IBinder) value);
        } else if (baseType == BaseType.BUNDLE) {
            writeBundle(dest, flags);
        } else {
            dest.writeValue(value);
        }
    }

    private void writeBundle(Parcel dest, int flags) {
        switch (transferType) {
            case TransferType.IN:
                writeInBundle(dest, flags);
                break;
            case TransferType.OUT:
                //do nothing
                break;
            case TransferType.INOUT:
                writeInBundle(dest, flags);
                break;

        }
    }

    private void writeInBundle(Parcel dest, int flags) {
        if (value != null) {
            dest.writeInt(1);
            ((Bundle) value).writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
    }

    private void writeParcelable(Parcel dest, int flags) {
        switch (transferType) {
            case TransferType.IN:
                writeInParcelable(dest, flags);
                break;
            case TransferType.OUT:
                //do nothing
                break;
            case TransferType.INOUT:
                writeInParcelable(dest, flags);
                break;
        }
    }

    private void writeInParcelable(Parcel dest, int flags) {
        if (value != null) {
            dest.writeInt(1);
            ((Parcelable) value).writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
    }

    //TODO 后面可以考虑扩展到包装类，这样就比AIDL使用上方便更多了
    private void parseTypeAndBaseType(Class<?> clazz, java.lang.reflect.Type genericParaType) {
        if (clazz.isPrimitive()) {
            type = Type.BASE;
            parsePrimitiveType(clazz);
            return;
        }
        if (clazz == CharSequence.class) {
            type = Type.BASE;
            baseType = BaseType.CHARSEQUENCE;
            return;
        }
        if (clazz == String.class) {
            type = Type.BASE;
            baseType = BaseType.STRING;
            return;
        }
        if (IBinder.class.isAssignableFrom(clazz)) {
            type = Type.BASE;
            baseType = BaseType.IBINDER;
            return;
        }
        if (Bundle.class.isAssignableFrom(clazz)) {
            type = Type.BASE;
            baseType = BaseType.BUNDLE;
            return;
        }
        if (Parcelable.class.isAssignableFrom(clazz)) {
            type = Type.BASE;
            baseType = BaseType.PARCELABLE;
            return;
        }
        if (clazz.isArray()) {
            type = Type.ARRAY;
            parseArrayType(clazz);
            return;
        }
        if (List.class.isAssignableFrom(clazz)) {
            type = Type.LIST;
            //TODO 这样做不行，至少要分String,Parcelable,IBinder
            baseType = BaseType.UNKNOWN;
            //parseGenericType(genericParaType);
            return;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            type = Type.MAP;
            baseType = BaseType.UNKNOWN;
            return;
        }
        throw new StellarException(clazz.getCanonicalName() + " is not supported by InterStellar! Please change type!");

    }

    //TODO 暂时不解析泛型参数，而是统一处理成writeList()和readList();
    /*
    private void parseGenericType(java.lang.reflect.Type genericParaType){
        if(genericParaType instanceof ParameterizedType){

        }else{

        }
    }
    */

    private void parseArrayType(Class<?> clazz) {
        if (clazz == int[].class) {
            baseType = BaseType.INT;
        } else if (clazz == short[].class) {
            baseType = BaseType.SHORT;
        } else if (clazz == long[].class) {
            baseType = BaseType.LONG;
        } else if (clazz == float[].class) {
            baseType = BaseType.FLOAT;
        } else if (clazz == double[].class) {
            baseType = BaseType.DOUBLE;
        } else if (clazz == byte[].class) {
            baseType = BaseType.BYTE;
        } else if (clazz == boolean[].class) {
            baseType = BaseType.BOOLEAN;
        } else if (clazz == char[].class) {
            baseType = BaseType.CHAR;
        } else if (clazz == CharSequence[].class) {
            baseType = BaseType.CHARSEQUENCE;
        } else if (clazz == String[].class) {
            baseType = BaseType.STRING;
        } else if (Parcelable[].class.isAssignableFrom(clazz)) {
            baseType = BaseType.PARCELABLE;
        }
    }

    private void parsePrimitiveType(Class<?> clazz) {
        if (clazz == int.class) {
            baseType = BaseType.INT;
        } else if (clazz == short.class) {
            baseType = BaseType.SHORT;
        } else if (clazz == long.class) {
            baseType = BaseType.LONG;
        } else if (clazz == float.class) {
            baseType = BaseType.FLOAT;
        } else if (clazz == double.class) {
            baseType = BaseType.DOUBLE;
        } else if (clazz == byte.class) {
            baseType = BaseType.BYTE;
        } else if (clazz == boolean.class) {
            baseType = BaseType.BOOLEAN;
        } else if (clazz == char.class) {
            baseType = BaseType.CHAR;
        }
    }


}
