package com.chyang.ui_mycanvasdome.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chyang.ui_mycanvasdome.R;
import com.chyang.ui_mycanvasdome.ui.DragViewGroup;

public class CustomUIActivity1 extends AppCompatActivity implements View.OnClickListener {

    private DragViewGroup mDragViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_custom_ui1);
        mDragViewGroup = (DragViewGroup) findViewById(R.id.dg_view);
        findViewById(R.id.bt_down).setOnClickListener(this);
        findViewById(R.id.bt_up).setOnClickListener(this);
        findViewById(R.id.bt_abc).setOnClickListener(this);
        mDragViewGroup.setGapHeight(130);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bt_down) {
            mDragViewGroup.startDown();
            findViewById(R.id.ll1).setVisibility(View.GONE);
        } else if(id == R.id.bt_up) {
            mDragViewGroup.startUp();
            findViewById(R.id.ll1).setVisibility(View.VISIBLE);
        } else if(id == R.id.bt_abc) {
            mDragViewGroup.startUp(8000, 10);
        }
    }
}
