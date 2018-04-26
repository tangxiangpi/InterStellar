package org.qiyi.video.mcg_arch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.qiyi.video.mcg.arch.InterStellar;
import org.qiyi.video.mcg.arch.log.Logger;
import org.qiyi.video.mcg_arch.service.Apple;
import org.qiyi.video.mcg_arch.service.IAppleService;

public class BananaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banana);

        findViewById(R.id.useServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IAppleService appleService = InterStellar.with(BananaActivity.this).getRemoteService(IAppleService.class);
                if (appleService != null) {
                    int appleNum = appleService.getApple(120);
                    Logger.d("appleNum:" + appleNum);
                    float calories = appleService.getAppleCalories(30);
                    Logger.d("calories:" + calories);

                    Apple apple = new Apple(1.3f, "Guangzhou");
                    String desc = appleService.outTest1(apple);
                    Logger.d("now apple:" + apple.toString());

                }
            }

        });

        findViewById(R.id.onewayTestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAppleService appleService = InterStellar.with(BananaActivity.this).getRemoteService(IAppleService.class);
                if (appleService != null) {
                    Logger.d("start of oneWayTest");
                    appleService.oneWayTest(new Apple(690f, "New York"));
                    Logger.d("end of oneWayTest");
                }
            }
        });

        findViewById(R.id.outTest1Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.outTest2Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAppleService appleService = InterStellar.with(BananaActivity.this).getRemoteService(IAppleService.class);
                if (appleService != null) {
                    int[] intArray = new int[3];
                    String result = appleService.outTest2(intArray);
                    for (int i = 0; i < intArray.length; ++i) {
                        Logger.d("intArray[" + i + "]=" + intArray[i]);
                    }
                }

            }
        });

        findViewById(R.id.outTest3Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAppleService appleService = InterStellar.with(BananaActivity.this).getRemoteService(IAppleService.class);
                if (appleService != null) {
                    int[] intArray = new int[3];
                    String[] strArray = new String[5];
                    appleService.outTest3(intArray, strArray);
                    printIntArray(intArray);
                    printStringArray(strArray);
                }
            }
        });

        findViewById(R.id.outTest4Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAppleService appleService = InterStellar.with(BananaActivity.this).getRemoteService(IAppleService.class);
                if (appleService != null) {
                    Apple[] apples = new Apple[6];
                    appleService.outTest4(apples);
                    printObjArray(apples, "apples");
                }
            }
        });

        findViewById(R.id.inoutTest1Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAppleService appleService = InterStellar.with(BananaActivity.this).getRemoteService(IAppleService.class);
                if (appleService != null) {
                    Apple apple = new Apple(1.8f, "Jiangxi");
                    appleService.inoutTest1(apple);
                    Logger.d("inoutTest1 result:" + apple.toString());
                }

            }
        });

        findViewById(R.id.inoutTest2Btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IAppleService appleService = InterStellar.with(BananaActivity.this).getRemoteService(IAppleService.class);
                if (appleService != null) {
                    Apple[] apples = new Apple[3];
                    for (int i = 0; i < apples.length; ++i) {
                        apples[i] = new Apple(1.1f + i, "Guangzhou" + i);
                    }
                    appleService.inoutTest2(apples);
                    Logger.d("inoutTest2 result is as follows:");
                    printObjArray(apples, "apples");
                }
            }
        });

    }

    private void printIntArray(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            Logger.d("intArray[" + i + "]=" + array[i]);
        }
    }

    private void printStringArray(String[] array) {
        for (int i = 0; i < array.length; ++i) {
            Logger.d("strArray[" + i + "]=" + array[i]);
        }
    }

    private void printObjArray(Object[] objs, String name) {
        if (objs == null) {
            return;
        }
        for (int i = 0; i < objs.length; ++i) {
            if (objs[i] == null) {
                continue;
            }
            Logger.d(name + "[" + i + "]=" + objs[i].toString());
        }
    }


}
