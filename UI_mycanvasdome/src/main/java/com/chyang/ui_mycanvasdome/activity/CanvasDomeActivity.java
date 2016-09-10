package com.chyang.ui_mycanvasdome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chyang.ui_mycanvasdome.R;

public class CanvasDomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_dome);
        findViewById(R.id.touch_test).setOnClickListener(this);
        findViewById(R.id.custom_view_1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Intent mIntent = new Intent();
        if (i == R.id.touch_test) {
            mIntent.setClass(this, OnTouchTestActivity.class);
        } else if(i == R.id.custom_view_1) {
            mIntent.setClass(this, CustomUIActivity1.class);
        }

        startActivity(mIntent);
    }
}
