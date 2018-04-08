package org.qiyi.video.mcg.arch.exception;

/**
 * Created by wangallen on 2018/4/2.
 */

public class StellarException extends RuntimeException {

    public StellarException(String msg) {
        super(msg);
    }

    public StellarException(Throwable throwable) {
        super(throwable);
    }

}
