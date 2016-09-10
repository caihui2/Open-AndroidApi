package com.chyang.ui_mycanvasdome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by chyang on 2016/8/30.
 */
public class MyLayout1 extends LinearLayout {

    private static final String TAG = "MyLayout1";

    public MyLayout1(Context context) {
        super(context);
    }

    public MyLayout1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,TAG +"---dispatchTouchEvent  anction:"+ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG,TAG +"---onInterceptTouchEvent  anction:"+ev.getAction());
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,TAG +"---onTouchEvent  anction:"+event.getAction());
        return true;
    }
}
