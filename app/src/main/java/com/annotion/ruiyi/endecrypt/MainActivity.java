package com.annotion.ruiyi.endecrypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.shuame.rootgenius.sdk.JniHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MainActivity", String.valueOf(JniHelper.encrypt("chenlong".getBytes())));
        Log.e("MainActivity", String.valueOf(JniHelper.decrypt(JniHelper.encrypt("chenlong".getBytes()))));

        Log.e("MianActivity",new ProtoData.RootingDev().toString());
    }
}
