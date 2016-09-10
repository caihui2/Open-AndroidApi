package com.chyang.ui_mycanvasdome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by chyang on 2016/9/1.
 */
public class MyLayout2 extends LinearLayout {

    private static final String TAG = "MyLayout2";

    public MyLayout2(Context context) {
        super(context);
    }

    public MyLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
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
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,TAG +"---onTouchEvent  anction:"+event.getAction());
        return false;
    }
}
