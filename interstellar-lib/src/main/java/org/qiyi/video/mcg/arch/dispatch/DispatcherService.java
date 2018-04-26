package org.qiyi.video.mcg.arch.dispatch;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by wangallen on 2018/4/17.
 */
//TODO 考虑一下是否要使用这个
public class DispatcherService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
