package org.qiyi.video.mcg.arch.param;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import org.qiyi.video.mcg.arch.exception.StellarException;
import org.qiyi.video.mcg.arch.parcelable.Outable;
import org.qiyi.video.mcg.arch.type.BaseType;
import org.qiyi.video.mcg.arch.type.Type;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangallen on 2018/4/3.
 */
//TODO 更好的一种方式是不是让它实现in和out的所有功能，然后在InParameter和OutParameter中再重写相应的方法
public abstract class AbstractParameter implements Parcelable,Outable{

    // Keep in sync with frameworks/native/include/private/binder/ParcelValTypes.h.
    protected static final int VAL_NULL = -1;
    protected static final int VAL_STRING = 0;
    protected static final int VAL_INTEGER = 1;
    protected static final int VAL_MAP = 2;
    protected static final int VAL_BUNDLE = 3;
    protected static final int VAL_PARCELABLE = 4;
    protected static final int VAL_SHORT = 5;
    protected static final int VAL_LONG = 6;
    protected static final int VAL_FLOAT = 7;
    protected static final int VAL_DOUBLE = 8;
    protected static final int VAL_BOOLEAN = 9;
    protected static final int VAL_CHARSEQUENCE = 10;
    protected static final int VAL_LIST = 11;
    protected static final int VAL_SPARSEARRAY = 12;
    protected static final int VAL_BYTEARRAY = 13;
    protected static final int VAL_STRINGARRAY = 14;
    protected static final int VAL_IBINDER = 15;
    protected static final int VAL_PARCELABLEARRAY = 16;
    protected static final int VAL_OBJECTARRAY = 17;
    protected static final int VAL_INTARRAY = 18;
    protected static final int VAL_LONGARRAY = 19;
    protected static final int VAL_BYTE = 20;
    protected static final int VAL_SERIALIZABLE = 21;
    protected static final int VAL_SPARSEBOOLEANARRAY = 22;
    protected static final int VAL_BOOLEANARRAY = 23;
    protected static final int VAL_CHARSEQUENCEARRAY = 24;
    protected static final int VAL_PERSISTABLEBUNDLE = 25;
    protected static final int VAL_SIZE = 26;
    protected static final int VAL_SIZEF = 27;
    protected static final int VAL_DOUBLEARRAY = 28;

    protected int type;
    protected int baseType;
    //private int transferType= TransferType.IN;
    protected Object value;

    public AbstractParameter() {
    }

    public AbstractParameter(Object param, java.lang.reflect.Type genericParaType) {
        parseTypeAndBaseType(param.getClass(), genericParaType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void readFromParcel(Parcel in){
        //do nothing
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

    private final void writeParcelableCreator(Parcel dest, Parcelable p) {
        dest.writeString(p.getClass().getName());
    }

    //TODO 这个是否为NULL其实不应该影响类型的写入的!所以是不是应该再处理一下呢?
    public final void extendWriteType(Object val, Parcel dest, int flags) {
        if (val == null) {
            dest.writeInt(VAL_NULL);
        } else if (val instanceof String) {
            dest.writeInt(VAL_STRING);
        } else if (val instanceof Integer) {
            dest.writeInt(VAL_INTEGER);
        } else if (val instanceof Map) {
            dest.writeInt(VAL_MAP);
        } else if (val instanceof Bundle) {
            dest.writeInt(VAL_BUNDLE);
        } else if (val instanceof PersistableBundle) {
            dest.writeInt(VAL_PERSISTABLEBUNDLE);
        } else if (val instanceof Parcelable) {
            dest.writeInt(VAL_PARCELABLE);
            //TODO 先简单粗暴一点，就直接写入值好了
            dest.writeParcelable((Parcelable) val, flags);
            //writeParcelableCreator(dest, (Parcelable) val);
        } else if (val instanceof Short) {
            dest.writeInt(VAL_SHORT);
        } else if (val instanceof Long) {
            dest.writeInt(VAL_LONG);
        } else if (val instanceof Float) {
            dest.writeInt(VAL_FLOAT);
        } else if (val instanceof Double) {
            dest.writeInt(VAL_DOUBLE);
        } else if (val instanceof Boolean) {
            dest.writeInt(VAL_BOOLEAN);
        } else if (val instanceof CharSequence) {
            dest.writeInt(VAL_CHARSEQUENCE);
        } else if (val instanceof List) {
            dest.writeInt(VAL_LIST);
        } else if (val instanceof SparseArray) {
            dest.writeInt(VAL_SPARSEARRAY);
        } else if (val instanceof boolean[]) {
            dest.writeInt(VAL_BOOLEANARRAY);
            dest.writeInt(((boolean[]) val).length);
        } else if (val instanceof byte[]) {
            dest.writeInt(VAL_BYTEARRAY);
            dest.writeInt(((byte[]) val).length);
        } else if (val instanceof String[]) {
            dest.writeInt(VAL_STRINGARRAY);
            dest.writeInt(((String[]) val).length);
        } else if (val instanceof CharSequence[]) {
            dest.writeInt(VAL_CHARSEQUENCEARRAY);
            dest.writeInt(((CharSequence[]) val).length);
        } else if (val instanceof IBinder) {
            dest.writeInt(VAL_IBINDER);
        } else if (val instanceof Parcelable[]) {
            dest.writeInt(VAL_PARCELABLEARRAY);
            dest.writeParcelableArray((Parcelable[]) val, flags);
            //dest.writeInt(((Parcelable[]) val).length);
            //writeParcelableCreator(dest, ((Parcelable[]) val)[0]);
        } else if (val instanceof int[]) {
            dest.writeInt(VAL_INTARRAY);
            dest.writeInt(((int[]) val).length);
        } else if (val instanceof long[]) {
            dest.writeInt(VAL_LONGARRAY);
            dest.writeInt(((long[]) val).length);
        } else if (val instanceof Byte) {
            dest.writeInt(VAL_BYTE);
        } else if (val instanceof Size) {
            dest.writeInt(VAL_SIZE);
        } else if (val instanceof SizeF) {
            dest.writeInt(VAL_SIZEF);
        } else if (val instanceof double[]) {
            dest.writeInt(VAL_DOUBLEARRAY);
            dest.writeInt(((double[]) val).length);
        } else {
            Class<?> clazz = val.getClass();
            if (clazz.isArray() && clazz.getComponentType() == Object.class) {
                // Only pure Object[] are written here, Other arrays of non-primitive types are
                // handled by serialization as this does not record the component type.
                dest.writeInt(VAL_OBJECTARRAY);
                extendWriteArray(dest, (Object[]) val, flags);
                //dest.writeArray((Object[]) v);
            } else if (val instanceof Serializable) {
                // Must be last
                dest.writeInt(VAL_SERIALIZABLE);
                dest.writeSerializable((Serializable) val);
            } else {
                throw new RuntimeException("Parcel: unable to marshal value " + val);
            }
        }

    }


    //TODO 要确认一件事，就是如果Parcel里面有值但是没有去读取，会不会对后面造成影响

    /**
     * 之所以叫readType()而不是readValue(),是因为这里实际上并不读取值，而只是读取类型
     *
     * @param loader
     * @param in
     * @return
     */
    public final Object extendReadType(ClassLoader loader, Parcel in) {
        int type = in.readInt();
        switch (type) {
            case VAL_NULL:
                return null;
            case VAL_STRING:
                return new String();
            case VAL_INTEGER:
                return 0;
            case VAL_MAP:
                return new HashMap<>();
            case VAL_PARCELABLE: {
                return in.readParcelable(loader);
            }
            case VAL_SHORT:
                return (short) 0;
            case VAL_LONG:
                return (long) 0;
            case VAL_FLOAT:
                return 0f;
            case VAL_DOUBLE:
                return 0.0;
            case VAL_BOOLEAN:
                return false;
            case VAL_CHARSEQUENCE:
                return createDefaultCharSequence();
            case VAL_LIST:
                return new ArrayList<>();
            case VAL_BOOLEANARRAY: {
                int length = in.readInt();
                if (length > 0) {
                    return new boolean[length];
                } else {
                    return null;
                }
            }
            //return in.createBooleanArray();
            case VAL_BYTEARRAY: {
                int length = in.readInt();
                if (length > 0) {
                    return new byte[length];
                } else {
                    return null;
                }
            }
            //return in.createByteArray();
            case VAL_STRINGARRAY: {
                int length = in.readInt();
                if (length > 0) {
                    return new String[length];
                } else {
                    return null;
                }
            }
            //return in.createStringArray();
            case VAL_CHARSEQUENCEARRAY: {
                int length = in.readInt();
                if (length > 0) {
                    return new CharSequence[length];
                } else {
                    return null;
                }
            }
            //return in.readCharSequenceArray();
            //return createCharSequenceArray(in);
            case VAL_IBINDER:
                return in.readStrongBinder();
            case VAL_OBJECTARRAY:
                return in.readArray(loader);
            case VAL_INTARRAY: {
                int length = in.readInt();
                if (length > 0) {
                    return new int[length];
                }
                return null;
            }
            //return in.createIntArray();
            case VAL_LONGARRAY: {
                int length = in.readInt();
                if (length > 0) {
                    return new long[length];
                } else {
                    return null;
                }
            }
            //return in.createLongArray();
            case VAL_BYTE:
                return (byte) 0;
            case VAL_SERIALIZABLE:
                return in.readSerializable();
            case VAL_PARCELABLEARRAY:
                //TODO 要创建对象
                return in.readParcelableArray(loader);
            case VAL_SPARSEARRAY:
                return in.readSparseArray(loader);
            case VAL_SPARSEBOOLEANARRAY:
                return in.readSparseBooleanArray();
            case VAL_BUNDLE:
                Bundle bundle = in.readBundle(loader);
                if (null == bundle) {
                    bundle = new Bundle();
                }
                return bundle;
            case VAL_PERSISTABLEBUNDLE:
                //TODO 要做版本适配
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return in.readPersistableBundle(loader);
                } else {
                    return null;
                }
            case VAL_SIZE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Size size = in.readSize();
                    if (size == null) {
                        size = new Size(0, 0);
                    }
                    return size;
                } else {
                    return null;
                }
            case VAL_SIZEF:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    SizeF sizeF = in.readSizeF();
                    if (null == sizeF) {
                        sizeF = new SizeF(0f, 0f);
                    }
                    return sizeF;
                } else {
                    return null;
                }
            case VAL_DOUBLEARRAY: {
                int length = in.readInt();
                if (length > 0) {
                    return new double[length];
                } else {
                    return null;
                }
            }
            //return in.createDoubleArray();
            default:
                int off = in.dataPosition() - 4;
                throw new RuntimeException(
                        "Parcel " + this + ": Unmarshalling unknown type code " + type + " at offset " + off);


        }
    }

    private CharSequence[] createCharSequenceArray(Parcel in) {
        CharSequence[] array = null;
        int length = in.readInt();
        if (length > 0) {
            array = new CharSequence[length];
        }
        return array;
    }

    private CharSequence createDefaultCharSequence() {
        return new CharSequence() {
            @Override
            public int length() {
                return 0;
            }

            @Override
            public char charAt(int index) {
                return 0;
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return null;
            }
        };
    }

    /*
    private <T extends Parcelable> T extendReadParcelable(ClassLoader loader,Parcel in){
        Parcelable.Creator<?>creator=in.readPar
    }
    */

    /**
     * Flatten a generic object in to a parcel.  The given Object value may
     * currently be one of the following types:
     * <p>
     * <ul>
     * <li> null
     * <li> String
     * <li> Byte
     * <li> Short
     * <li> Integer
     * <li> Long
     * <li> Float
     * <li> Double
     * <li> Boolean
     * <li> String[]
     * <li> boolean[]
     * <li> byte[]
     * <li> int[]
     * <li> long[]
     * <li> Object[] (supporting objects of the same type defined here).
     * <li> {@link Bundle}
     * <li> Map (as supported by {writeMap}).
     * <li> Any object that implements the {@link Parcelable} protocol.
     * <li> Parcelable[]
     * <li> CharSequence (as supported by {@link TextUtils#writeToParcel}).
     * <li> List (as supported by {writeList}).
     * <li> {@link SparseArray} (as supported by {writeSparseArray(SparseArray)}).
     * <li> {@link IBinder}
     * <li> Any object that implements Serializable (but see
     * {writeSerializable} for caveats).  Note that all of the
     * previous types have relatively efficient implementations for
     * writing to a Parcel; having to rely on the generic serialization
     * approach is much less efficient and should be avoided whenever
     * possible.
     * </ul>
     * <p>
     * <p class="caution">{@link Parcelable} objects are written with
     * {@link Parcelable#writeToParcel} using contextual flags of 0.  When
     * serializing objects containing {@link ParcelFileDescriptor}s,
     * this may result in file descriptor leaks when they are returned from
     * Binder calls (where {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     * should be used).</p>
     */
    public final void extendWriteValue(Parcel dest, Object v, int flags) {  //TODO 这里唯一要担心的可能就是各Android版本的差异，如果这方面有差异的话要修改!
        if (v == null) {
            dest.writeInt(VAL_NULL);
        } else if (v instanceof String) {
            dest.writeInt(VAL_STRING);
            dest.writeString((String) v);
        } else if (v instanceof Integer) {
            dest.writeInt(VAL_INTEGER);
            dest.writeInt((Integer) v);
        } else if (v instanceof Map) {
            dest.writeInt(VAL_MAP);
            dest.writeMap((Map) v);
        } else if (v instanceof Bundle) {
            // Must be before Parcelable
            dest.writeInt(VAL_BUNDLE);
            dest.writeBundle((Bundle) v);
        } else if (v instanceof PersistableBundle) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dest.writeInt(VAL_PERSISTABLEBUNDLE);
                dest.writePersistableBundle((PersistableBundle) v);
            } else {
                //TODO 是抛出异常还是直接就不处理呢?这个其实让注解处理器在编译时抛出异常最好了
            }

        } else if (v instanceof Parcelable) {
            // IMPOTANT: cases for classes that implement Parcelable must
            // come before the Parcelable case, so that their specific VAL_*
            // types will be written.
            dest.writeInt(VAL_PARCELABLE);
            dest.writeParcelable((Parcelable) v, flags);
        } else if (v instanceof Short) {
            dest.writeInt(VAL_SHORT);
            dest.writeInt(((Short) v).intValue());
        } else if (v instanceof Long) {
            dest.writeInt(VAL_LONG);
            dest.writeLong((Long) v);
        } else if (v instanceof Float) {
            dest.writeInt(VAL_FLOAT);
            dest.writeFloat((Float) v);
        } else if (v instanceof Double) {
            dest.writeInt(VAL_DOUBLE);
            dest.writeDouble((Double) v);
        } else if (v instanceof Boolean) {
            dest.writeInt(VAL_BOOLEAN);
            dest.writeInt((Boolean) v ? 1 : 0);
        } else if (v instanceof CharSequence) {
            // Must be after String
            dest.writeInt(VAL_CHARSEQUENCE);
            //TODO 这是个隐藏方法，要用反射来做!
            //dest.writeCharSequence((CharSequence) v);
            writeCharSequence(dest, (CharSequence) v);
        } else if (v instanceof List) {
            dest.writeInt(VAL_LIST);
            dest.writeList((List) v);
        } else if (v instanceof SparseArray) {
            dest.writeInt(VAL_SPARSEARRAY);
            dest.writeSparseArray((SparseArray) v);
        } else if (v instanceof boolean[]) {
            dest.writeInt(VAL_BOOLEANARRAY);
            dest.writeBooleanArray((boolean[]) v);
        } else if (v instanceof byte[]) {
            dest.writeInt(VAL_BYTEARRAY);
            dest.writeByteArray((byte[]) v);
        } else if (v instanceof String[]) {
            dest.writeInt(VAL_STRINGARRAY);
            dest.writeStringArray((String[]) v);
        } else if (v instanceof CharSequence[]) {
            // Must be after String[] and before Object[]
            dest.writeInt(VAL_CHARSEQUENCEARRAY);
            writeCharSequenceArray(dest, (CharSequence[]) v);
            //dest.writeCharSequenceArray((CharSequence[]) v);
        } else if (v instanceof IBinder) {
            dest.writeInt(VAL_IBINDER);
            dest.writeStrongBinder((IBinder) v);
        } else if (v instanceof Parcelable[]) {
            dest.writeInt(VAL_PARCELABLEARRAY);
            dest.writeParcelableArray((Parcelable[]) v, flags);
        } else if (v instanceof int[]) {
            dest.writeInt(VAL_INTARRAY);
            dest.writeIntArray((int[]) v);
        } else if (v instanceof long[]) {
            dest.writeInt(VAL_LONGARRAY);
            dest.writeLongArray((long[]) v);
        } else if (v instanceof Byte) {
            dest.writeInt(VAL_BYTE);
            dest.writeInt((Byte) v);
        } else if (v instanceof Size) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dest.writeInt(VAL_SIZE);
                dest.writeSize((Size) v);
            } else {
                //TODO
            }

        } else if (v instanceof SizeF) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dest.writeInt(VAL_SIZEF);
                dest.writeSizeF((SizeF) v);
            } else {
                //TODO
            }

        } else if (v instanceof double[]) {
            dest.writeInt(VAL_DOUBLEARRAY);
            dest.writeDoubleArray((double[]) v);
        } else {
            Class<?> clazz = v.getClass();
            if (clazz.isArray() && clazz.getComponentType() == Object.class) {
                // Only pure Object[] are written here, Other arrays of non-primitive types are
                // handled by serialization as this does not record the component type.
                dest.writeInt(VAL_OBJECTARRAY);
                extendWriteArray(dest, (Object[]) v, flags);
                //dest.writeArray((Object[]) v);
            } else if (v instanceof Serializable) {
                // Must be last
                dest.writeInt(VAL_SERIALIZABLE);
                dest.writeSerializable((Serializable) v);
            } else {
                throw new RuntimeException("Parcel: unable to marshal value " + v);
            }
        }
    }

    public final void extendWriteArray(Parcel dest, Object[] val, int flags) {
        if (val == null) {
            dest.writeInt(-1);
            return;
        }
        int N = val.length;
        int i = 0;
        dest.writeInt(N);
        while (i < N) {
            extendWriteValue(dest, val[i], flags);
            i++;
        }
    }

    private void writeCharSequence(Parcel dest, CharSequence c) {
        try {
            Method method = Parcel.class.getMethod("writeCharSequence", CharSequence.class);
            method.invoke(dest, c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void writeCharSequenceArray(Parcel dest, CharSequence[] array) {
        try {
            Method method = Parcel.class.getMethod("writeCharSequenceArray", CharSequence[].class);
            method.invoke(dest, (Object) array);
            //method.invoke(array,dest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    protected int getArrayLength() {
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

    public Object getValue() {
        return value;
    }
}
