package com.chyang.androidapidome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chyang.androidapidome.Service.DomeServiceActivity;
import com.chyang.androidapidome.jni.DomeJniActivity;
import com.chyang.androidapidome.opencv.OpenCvDomeActivity;
import com.chyang.androidapidome.view.UIMainActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_ui).setOnClickListener(this);
        findViewById(R.id.bt_service).setOnClickListener(this);
        findViewById(R.id.bt_jni).setOnClickListener(this);
        findViewById(R.id.opencv).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = null;
        switch (v.getId()) {
            case R.id.bt_ui:
                mIntent = new Intent(this, UIMainActivity.class);
                break;
            case R.id.bt_service:
                mIntent = new Intent(this, DomeServiceActivity.class);
                break;
            case R.id.bt_jni:
                mIntent = new Intent(this, DomeJniActivity.class);
                break;
            case R.id.opencv:
                mIntent = new Intent(this, OpenCvDomeActivity.class);
                break;
        }

        if(mIntent != null) startActivity(mIntent);
    }

}
