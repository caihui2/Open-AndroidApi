package com.chyang.ui_myanimation.Activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import com.chyang.ui_myanimation.R;

public class PropertyAnimationActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_animation);
        imageView = (ImageView) findViewById(R.id.iv_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("你点击了anglababy");
            }
        });
        findViewById(R.id.bt_value_animation).setOnClickListener(this);
        findViewById(R.id.bt_animator_set).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bt_value_animation) {
            startValueAnimation();
        } else if(id == R.id.bt_animator_set) {
            startAnimatorSet();
        }
    }



    private void startValueAnimation() {
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f,1f, 0f, 0f, 1f);
//        valueAnimator.setDuration(8000);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//             float value = (float) animation.getAnimatedValue();
//              //  imageView.setTranslationX(imageView.getWidth() * value);
//                imageView.setTranslationY(imageView.getHeight() * value);
//                imageView.setScaleX(value);
//                imageView.setScaleY(value);
//                imageView.setRotationX(360 * value);
//            }
//        });
//        valueAnimator.start();

        final ValueAnimator mValueAnimator = ValueAnimator.ofInt(0, imageView.getHeight());
        mValueAnimator.setDuration(3000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int)animation.getAnimatedValue();
                 imageView.setTranslationY(value);
                 float offset = ((float) value) / ((float) imageView.getHeight());
                 imageView.setAlpha(offset);
                System.out.println(offset);
                imageView.setScaleX(offset);
                imageView.setScaleY(offset);
                imageView.setRotationX(360 * offset);
            }
        });
       mValueAnimator.start();
    }

    private void startAnimatorSet() {
        ObjectAnimator alpha  = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(imageView, "rotationY", 0f , 360);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(imageView,"translationX", 0, -500f, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(imageView,"translationY", 0, -500f, 0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 3f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 3f, 1f);
        AnimatorSet animatorSet =  new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) { //动画重复执行的时候调用

            }
        });
        animatorSet.play(alpha).with(rotation).with(translationX).with(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(8000);
        animatorSet.start();
    }
}
