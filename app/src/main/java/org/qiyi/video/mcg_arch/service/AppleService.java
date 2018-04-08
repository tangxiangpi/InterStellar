package org.qiyi.video.mcg_arch.service;

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
    public String getAppleDesc(Apple apple) {
        if (apple == null) {
            apple = new Apple(3.2f, "Shanghai");
        }
        apple.setWeight(apple.getWeight() * 2);
        apple.setFrom("Beijing");
        return "Have a nice day!";
    }
}
