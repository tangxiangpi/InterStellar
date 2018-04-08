package org.qiyi.video.mcg.arch.transfer;

import android.os.RemoteException;

/**
 * Created by wangallen on 2018/4/7.
 */

public interface IApi {
    Reply invoke(StellarMethod method) throws RemoteException;
}
