package org.qiyi.video.mcg.arch.transfer;

import android.os.IBinder;
import android.os.Parcelable;

/**
 * Created by wangallen on 2018/4/7.
 */

public interface ITransfer extends android.os.IInterface {

    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements org.qiyi.video.mcg.arch.transfer.ITransfer {
        private static final java.lang.String DESCRIPTOR = "org.qiyi.video.mcg.arch.transfer.ITransfer";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an org.qiyi.video.mcg.arch.transfer.ITransfer interface,
         * generating a proxy if needed.
         */
        public static ITransfer asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof org.qiyi.video.mcg.arch.transfer.ITransfer))) {
                return ((org.qiyi.video.mcg.arch.transfer.ITransfer) iin);
            }
            return new Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_invoke: {
                    data.enforceInterface(DESCRIPTOR);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    StellarMethod _arg1;
                    if ((0 != data.readInt())) {
                        _arg1 = StellarMethod.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    Reply _result = this.invoke(_arg0, _arg1);
                    if ((flags & IBinder.FLAG_ONEWAY) != 0) {
                        return true;
                    }
                    reply.writeNoException();
                    if ((_result != null)) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    if (_arg1 != null) {
                        reply.writeInt(1);
                        //所以在OutParameter中要区分flags,像这种情况就不能只是写入类型，而是是必须写入值
                        _arg1.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements org.qiyi.video.mcg.arch.transfer.ITransfer {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public Reply invoke(String serviceInterface, StellarMethod method) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                Reply _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(serviceInterface);
                    if ((method != null)) {
                        _data.writeInt(1);
                        method.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (method.isOneWay()) {
                        mRemote.transact(Stub.TRANSACTION_invoke, _data, null, IBinder.FLAG_ONEWAY);
                        return null;
                    }
                    mRemote.transact(Stub.TRANSACTION_invoke, _data, _reply, 0);
                    _reply.readException();
                    if ((0 != _reply.readInt())) {
                        _result = Reply.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    if (0 != _reply.readInt()) {  //PARCELABLE_WRITE_RETURN_VALUE的值为1
                        //处理out和inout
                        method.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
        }

        static final int TRANSACTION_invoke = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }

    public Reply invoke(String serviceInterface, StellarMethod method) throws android.os.RemoteException;


}
