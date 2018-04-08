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
                IAppleService appleService = InterStellar.getRemoteService(IAppleService.class);
                if (appleService != null) {
                    int appleNum = appleService.getApple(120);
                    Logger.d("appleNum:" + appleNum);
                    float calories = appleService.getAppleCalories(30);
                    Logger.d("calories:" + calories);

                    String manifacture = "manifacture", tailer = "tailer";
                    // String detail = appleService.getAppleDetails(10, manifacture, tailer, "Tom", 1000);
                    //TODO 测试下来发现值还是没有改变，为什么?manifacture和tailer不是标记为out吗?
                    //Logger.d("manifacture:"+manifacture+",tailer:"+tailer+",detail:" + detail);

                    Apple apple = new Apple(1.3f, "Guangzhou");
                    String desc = appleService.getAppleDesc(apple);
                    Logger.d("now apple:" + apple.toString());

                }
            }
        });
    }


}
