package org.qiyi.video.mcg_arch.service;

import org.qiyi.video.mcg.arch.log.Logger;

/**
 * Created by wangallen on 2018/4/7.
 */

public class AppleService implements IAppleService {

    @Override
    public int getApple(int money) {
        return money / 2;
    }

    @Override
    public float getAppleCalories(int appleNum) {
        return appleNum * 5;
    }


    @Override
    public String getAppleDetails(int appleNum, String manifacture, String tailerName, String userName, int userId) {
        manifacture = "IKEA";
        tailerName = "muji";
        userId = 1024;
        if ("Tom".equals(userName)) {
            return manifacture + "-->" + tailerName;
        } else {
            return tailerName + "-->" + manifacture;
        }
    }


    @Override
    public String outTest1(Apple apple) {
        if (apple == null) {
            apple = new Apple(3.2f, "Shanghai");
        }
        apple.setWeight(apple.getWeight() * 2);
        apple.setFrom("Beijing");
        return "Have a nice day!";
    }

    @Override
    public String outTest2(int[] appleNum) {
        if (null == appleNum) {
            return "";
        }
        for (int i = 0; i < appleNum.length; ++i) {
            appleNum[i] = i + 1;
        }
        return "Have a nice day 02!";
    }

    @Override
    public String outTest3(int[] array1, String[] array2) {
        for (int i = 0; i < array1.length; ++i) {
            array1[i] = i + 2;
        }
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = "Hello world" + (i + 1);
        }

        return "outTest3";
    }

    @Override
    public String outTest4(Apple[] apples) {
        for (int i = 0; i < apples.length; ++i) {
            apples[i] = new Apple(i + 2f, "Shanghai");
        }

        return "outTest4";
    }

    @Override
    public String inoutTest1(Apple apple) {
        Logger.d("AppleService-->inoutTest1,apple:" + apple.toString());
        apple.setWeight(3.14159f);
        apple.setFrom("Germany");
        return "inoutTest1";
    }

    @Override
    public String inoutTest2(Apple[] apples) {
        Logger.d("AppleService-->inoutTest2,apples[0]:" + apples[0].toString());
        for (int i = 0; i < apples.length; ++i) {
            apples[i].setWeight(i * 1.5f);
            apples[i].setFrom("Germany" + i);
        }
        return "inoutTest2";
    }
}
