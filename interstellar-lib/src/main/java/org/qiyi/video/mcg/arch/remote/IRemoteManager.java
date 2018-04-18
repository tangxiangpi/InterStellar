package org.qiyi.video.mcg.arch.remote;

/**
 * Created by wangallen on 2018/4/17.
 */

public interface IRemoteManager {

    <T> T getRemoteService(Class<?> serviceInterface);
}
