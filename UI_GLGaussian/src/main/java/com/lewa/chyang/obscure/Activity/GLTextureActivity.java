package com.lewa.chyang.obscure.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import com.lewa.chyang.obscure.GLTextureView;
import com.lewa.chyang.obscure.R;
import com.lewa.chyang.obscure.renderer.GaussianBlurRenderer;

public class GLTextureActivity extends AppCompatActivity {
    private GLTextureView mView = null;
    private GaussianBlurRenderer mRender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gltexture);
        mView = (GLTextureView) findViewById(R.id.gl_view);
        mView.setRenderMode(GLTextureView.RENDERMODE_WHEN_DIRTY);
        mRender = new GaussianBlurRenderer(this, mView);
        mRender.setBlurRadius(24);
        mRender.setOnlyDrawWallpaper(true);
        mRender.setWallpaper(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), false);
        mView.setRenderer(mRender);
        mView.onResume();
        mView.requestRender();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mView, "translationX", 0.0f, 350.0f, 0f);
        animator.setDuration(2500).start();
    }

    public static void slowChangeView(ImageView fontView, ImageView afterView, Bitmap mBitmap, boolean isShowAfter) {
        AnimatorSet mAnimationSet = new AnimatorSet();
        mAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimationSet.setDuration(1000);
        if (isShowAfter) {
            afterView.setImageBitmap(mBitmap);
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(fontView, "alpha", 1f, 0.0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(afterView, "alpha", 0.0f, 1f);
            mAnimationSet.play(animator2).with(animator1);
        } else {
            fontView.setImageBitmap(mBitmap);
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(afterView, "alpha", 1f, 0.0f);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(fontView, "alpha", 0.0f, 1f);
            mAnimationSet.play(animator2).with(animator1);

        }
        mAnimationSet.start();

    }
}
