package com.chyang.ui_mycanvasdome.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chyang on 16/5/8.
 */
public class DomeView extends View {

    Paint mPaint = null;

    private long x = 50;

    private long y = 50;

    private CanvasLoop mCanvasLoop = null;


    public DomeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(20);
        mCanvasLoop = new CanvasLoop(this);
        mCanvasLoop.start();

    }


    public DomeView(Context context) {
        this(context, null, 0);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // canvas.drawPoint(100, 100, mPaint);
        canvas.drawRect(0, 0, x, y, mPaint);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(200);
        canvas.drawText("辉哥万岁", 0, y, mPaint);
        mPaint.setColor(Color.RED);
        mCanvasLoop.onResume();
    }

    public void onUpdate() {
        if(x > 1080) {
            x = 0;
        }
        if(y > 1980) {
            y = 0;
        }
        x+=2;
        y+=2;
    }


    class CanvasLoop extends Thread {
        private DomeView domeView;
        private boolean isRun = false;
        public CanvasLoop(DomeView mDomeView) {
            domeView = mDomeView;
        }

        @Override
        public void run() {
            while (true) {
                if(isRun) {
                    domeView.onUpdate();
                    domeView.postInvalidateDelayed(0);
                    onPause();
                }
            }
        }


        public void onPause() {
            isRun = false;
        }

        public void onResume() {
            isRun = true;
        }


    }
}
