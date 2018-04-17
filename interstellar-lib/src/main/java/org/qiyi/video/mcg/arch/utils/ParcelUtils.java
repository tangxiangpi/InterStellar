package org.qiyi.video.mcg.arch.utils;

import android.os.Parcel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Created by wangallen on 2018/4/16.
 */

public final class ParcelUtils {

    private ParcelUtils() {
    }

    public static Object readCharSequence(Parcel source) {
        Method method = ReflectUtils.getMethod(Parcel.class, "readCharSequence");
        if (null == method) {
            return null;
        }
        try {
            return method.invoke(source, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object readCharSequenceArray(Parcel source) {
        Method method = ReflectUtils.getMethod(Parcel.class, "readCharSequenceArray");
        if (null == method) {
            return null;
        }
        try {
            return method.invoke(source, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String[] readStringArray(Parcel source) {
        String[] array = null;

        int length = source.readInt();
        if (length >= 0) {
            array = new String[length];

            for (int i = 0; i < length; i++) {
                array[i] = source.readString();
            }
        }

        return array;
    }

    public static Serializable readSerializable(Parcel source, final ClassLoader loader) {
        String name = source.readString();
        if (name == null) {
            // For some reason we were unable to read the name of the Serializable (either there
            // is nothing left in the Parcel to read, or the next value wasn't a String), so
            // return null, which indicates that the name wasn't found in the parcel.
            return null;
        }

        byte[] serializedData = source.createByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass osClass)
                        throws IOException, ClassNotFoundException {
                    // try the custom classloader if provided
                    if (loader != null) {
                        Class<?> c = Class.forName(osClass.getName(), false, loader);
                        if (c != null) {
                            return c;
                        }
                    }
                    return super.resolveClass(osClass);
                }
            };
            return (Serializable) ois.readObject();
        } catch (IOException ioe) {
            throw new RuntimeException("Parcelable encountered " +
                    "IOException reading a Serializable object (name = " + name +
                    ")", ioe);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("Parcelable encountered " +
                    "ClassNotFoundException reading a Serializable object (name = "
                    + name + ")", cnfe);
        }
    }

    public static Object readParcelableArray(Parcel source) {
        String componentType = source.readString();
        try {
            Class componentClass = Class.forName(componentType);
            return createTypedArray(source,componentClass);
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private static <T> T[] createTypedArray(Parcel source, Class<T> clazz) {
        int N = source.readInt();
        if (N < 0) {
            return null;
        }
        T[] arr = (T[]) Array.newInstance(clazz, N);
        for (int i = 0; i < N; i++) {
            arr[i] = source.readParcelable(clazz.getClassLoader());
        }
        return arr;
    }

}
