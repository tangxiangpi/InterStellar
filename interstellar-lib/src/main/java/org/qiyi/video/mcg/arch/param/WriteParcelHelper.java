package org.qiyi.video.mcg.arch.param;

import android.os.IBinder;
import android.os.Parcel;

import org.qiyi.video.mcg.arch.exception.StellarException;
import org.qiyi.video.mcg.arch.type.BaseType;
import org.qiyi.video.mcg.arch.type.Type;

/**
 * Created by wangallen on 2018/4/3.
 */

public final class WriteParcelHelper {

    private WriteParcelHelper(){}

    /*
    public void writeValue2Parcel(Parcel dest,int flags,int type,
                                  int baseType,Object value){
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
    */

    /**
     * BaseType除了PARCELABLE和Bundle之外，其他只能用in修饰，所以比较简单
     *
     * @param dest
     */
    /*
    private void writeBase2Parcel(Parcel dest, int flags,int baseType,Object value) {
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
    */


}
