package com.chyang.ui_myanimation.Tools;

import android.animation.TypeEvaluator;

import com.chyang.ui_myanimation.enter.Point;

/**
 * Created by chyang on 2016/8/26.
 */
public class PointEvaluator implements TypeEvaluator<Point> {
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {

        float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
        float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());
        Point point = new Point(x, y);
        return point;
    }
}
