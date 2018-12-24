package com.annotion.ruiyi.endecrypt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.shuame.rootgenius.sdk.proto.ProtoData;

import org.xmlpull.v1.XmlPullParser;

public class MainActivity extends AppCompatActivity {

    private ProtoData.RootingDev rootingDev;
    private ProtoData.QueryRootingResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootingDev = CommUtils.parseRootingDev(this);
        result = new ProtoData.QueryRootingResult();

        Log.e("MainActivity",rootingDev.toString());
    }
}
