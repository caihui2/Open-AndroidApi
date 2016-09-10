package com.chyang.ui_mycanvasdome.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chyang on 2016/9/10.
 */
public class CountView extends View implements View.OnClickListener {

    private Paint mPaint;

    private Rect mBounds;

    private int mCount;

    public CountView(Context context) {
        super(context);

    }

    public CountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        setOnClickListener(this);
    }

    public CountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            mCount ++;
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0,0, getWidth(), getHeight(), mPaint);
        mPaint.setColor(Color.YELLOW);
        mPaint.setTextSize(130);
        String text = String.valueOf(mCount);
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();
        canvas.drawText(text, getWidth() / 2 - textWidth / 2 ,getHeight() / 2 + textHeight / 2 , mPaint);
    }

    @Override
    public void onClick(View v) {
        mCount++;
        invalidate();
    }
}
