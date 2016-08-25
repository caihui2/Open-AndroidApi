package com.chyang.androidapidome.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chyang.androidapidome.R;
import com.chyang.materialtexttoolbar.Activity.MaterialTextToolbarActivity;
import com.chyang.ui_mycanvasdome.activity.CanvasDomeActivity;
import com.chyang.ui_x_screen.Activity.XScreenActivity;
import com.lewa.chyang.obscure.Activity.GLTextureActivity;

public class UIMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uimain_acitity);
        findViewById(R.id.canvas_dome).setOnClickListener(this);
        findViewById(R.id.bt_fuzzy).setOnClickListener(this);
        findViewById(R.id.bt_materialToolbar).setOnClickListener(this);
        findViewById(R.id.bt_x_screen).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = null;
        switch (v.getId()) {
            case R.id.canvas_dome:
               mIntent = new Intent(this  , CanvasDomeActivity.class);
                break;
            case R.id.bt_fuzzy:
                mIntent = new Intent(this, GLTextureActivity.class);
                break;
            case R.id.bt_materialToolbar:
                mIntent = new Intent(this, MaterialTextToolbarActivity.class);
                break;
            case R.id.bt_x_screen:
                mIntent = new Intent(this, XScreenActivity.class);
                break;
        }
        startActivity(mIntent);
    }
}
