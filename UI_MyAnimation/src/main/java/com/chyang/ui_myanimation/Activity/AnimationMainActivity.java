package com.chyang.ui_myanimation.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chyang.ui_myanimation.R;

public class AnimationMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_main);
        findViewById(R.id.bt_property).setOnClickListener(this);
        findViewById(R.id.bt_property2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        Intent mIntent = new Intent();
        if (i == R.id.bt_property) {
            mIntent.setClass(this, PropertyAnimationActivity.class);
        } else if(i == R.id.bt_property2) {
            mIntent.setClass(this, PropertyAnimationActivity2.class);
        }
        startActivity(mIntent);
    }
}
