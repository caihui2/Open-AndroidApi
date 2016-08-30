package com.chyang.ui_myanimation.Tools;

import android.animation.TimeInterpolator;

/**
 * Created by chyang on 2016/8/30.
 */
public class DecelerateAccelerateInterpoltor implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {
        float result;
        if(input <= 0.5) {
            result = (float)(Math.sin(Math.sin(Math.PI * input))) / 2;
        } else {
            result = (float) (Math.sin(2 - Math.sin(Math.PI * input))) / 2;
        }
        return result;
    }
}
