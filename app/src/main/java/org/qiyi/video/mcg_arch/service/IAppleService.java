package org.qiyi.video.mcg_arch.service;

import org.qiyi.video.mcg.arch.annotation.inout;
import org.qiyi.video.mcg.arch.annotation.out;

/**
 * Created by wangallen on 2018/4/7.
 */

public interface IAppleService {

    int getApple(int money);

    float getAppleCalories(int appleNum);

    //这种情形已经测试通过
    //String getAppleDetails(int appleNum,  String manifacture,  String tailerName, String userName,  int userId);

    String getAppleDesc(@out Apple apple);

    //TODO 像String和int是不能用@out或@inout修饰的，这个其实最好在编译时报错!
    String getAppleDetails(int appleNum, @out String manifacture, @out String tailerName, String userName, @out int userId);
    //String getAppleDetails(int appleNum, @inout String manifacture, @inout String tailerName, String userName, @inout int userId);

}
