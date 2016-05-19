package com.chyang.androidapidome.Service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chyang.androidapidome.R;

public class DomeServiceActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dome_service);
        findViewById(R.id.start_service).setOnClickListener(this);
        findViewById(R.id.stop_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent();
        switch (v.getId()) {
            case R.id.start_service:
                mIntent.setClass(this, DomeService.class);
                startService(mIntent);
                break;
            case R.id.stop_service:
                mIntent.setClass(this, DomeService.class);
                stopService(mIntent);
                break;
        }
    }
}
