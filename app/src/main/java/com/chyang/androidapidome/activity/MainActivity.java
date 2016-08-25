package com.chyang.androidapidome.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chyang.androidapidome.R;
import com.chyang.sv_myservicedome.Service.DomeServiceActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_ui).setOnClickListener(this);
        findViewById(R.id.bt_service).setOnClickListener(this);
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
        }

        if(mIntent != null) startActivity(mIntent);
    }

}
