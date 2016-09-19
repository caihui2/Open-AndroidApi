package com.chyang.ui_x_screen.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by chyang on 2016/8/24.
 */
public class LovelyRecyclerView extends RecyclerView {

    private static final boolean DEBUG = true;
    private static final String TAG = "LovelyRecyclerView";

    public boolean isChangeHolder = false;

    public interface OnScrollHeaderOffsetListener {
        float getHeaderHeight();
        void onHeaderScrollOffset(float offset);
    }

    private OnScrollHeaderOffsetListener mOnScrollHeaderOffsetListener;


    public LovelyRecyclerView(Context context) {
        super(context);
    }

    public LovelyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LovelyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
       int action = e.getAction();
        System.out.println("action:"+ action);
        switch (action) {
            case KeyEvent.ACTION_DOWN:
                System.out.println("我点击了屏幕"+e.getY());
                break;
        }
        return super.onTouchEvent(e);
    }

    public void setOnScrollHeaderOffsetListener(OnScrollHeaderOffsetListener mOnScrollHeaderOffsetListener) {
        this.mOnScrollHeaderOffsetListener = mOnScrollHeaderOffsetListener;
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if(mOnScrollHeaderOffsetListener != null) {
            float headerHeight = mOnScrollHeaderOffsetListener.getHeaderHeight();
            float offset =  Math.max((headerHeight - computerHeaderScrollOffset(headerHeight)) / headerHeight, 0f);
            mOnScrollHeaderOffsetListener.onHeaderScrollOffset(offset);
            if(DEBUG)Log.e(TAG,"offset:"+offset);
        }
    }



    private float computerHeaderScrollOffset(float height) {
        LinearLayoutManager linearLayoutManager = null;
        float offset = -1;
        if(getLayoutManager() instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            View c = getChildAt(0);
            if (c == null) return 0;
            int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
            int top = c.getTop();
            float headerHeight = 0;
            if (firstVisiblePosition >= 1) headerHeight = height;
            offset = -top + firstVisiblePosition * c.getHeight() + headerHeight;
            return offset;
        }
        return offset;
    }



}
