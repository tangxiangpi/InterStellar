package org.qiyi.video.mcg_arch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.qiyi.video.mcg.arch.InterStellar;
import org.qiyi.video.mcg_arch.service.AppleService;
import org.qiyi.video.mcg_arch.service.IAppleService;

public class AppleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apple);

        findViewById(R.id.gotoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AppleActivity.this,BananaActivity.class));
            }
        });


        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterStellar.registerRemoteService(IAppleService.class,new AppleService());
            }
        });

    }
}
