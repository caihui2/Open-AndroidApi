package com.chyang.ui_myanimation.View;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.chyang.ui_myanimation.Tools.PointEvaluator;
import com.chyang.ui_myanimation.enter.ColorEvaluator;
import com.chyang.ui_myanimation.enter.Point;

/**
 * Created by chyang on 2016/8/29.
 */
public class MyAnimView extends View {

    public static final float RADUIS = 50f;

    private Point currentPoint;

    private Paint mPaint;

    private String color;

    public MyAnimView(Context context) {
        super(context);
    }

    public MyAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //构造画并且设置反锯齿属性
        mPaint.setColor(Color.BLUE); //设置画笔颜色为蓝色
    }

    public MyAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(currentPoint == null) {
            currentPoint = new Point(RADUIS, RADUIS);
            startAnimator();
        } else {
            drawCircle(canvas);
        }
    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADUIS, mPaint);
    }


    private void startAnimator() {
        Point startPoint = new Point(RADUIS, RADUIS);
        Point endPoint = new Point(getWidth() - RADUIS, getHeight() - RADUIS);

        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point)animation.getAnimatedValue();
                invalidate();
            }
        });
        ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(), "#00000ff","#FF0000");
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim).with(anim2);
        animSet.setDuration(5000);
        animSet.start();
    }
}
