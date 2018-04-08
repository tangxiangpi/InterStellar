package org.qiyi.video.mcg.arch.wrapper;

/**
 * Created by wangallen on 2018/3/31.
 */
//TODO 对应基础数据类型
public interface BaseWrapper {

    int BYTE=1;


    int getType();

    Object getParameter();
}
