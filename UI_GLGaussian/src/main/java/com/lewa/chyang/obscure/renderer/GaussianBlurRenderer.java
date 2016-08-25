package com.lewa.chyang.obscure.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.lewa.chyang.obscure.GLTextureView;
import com.lewa.chyang.obscure.L;
import com.lewa.chyang.obscure.item.GaussianItem;
import com.lewa.chyang.obscure.item.Wallpaper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Gaussian blur render
 */
public class GaussianBlurRenderer implements GLSurfaceView.Renderer, GLTextureView.Renderer {
    private final static L.Tag TAG = new L.Tag("BlurRender");

    private boolean isGLContextInit = false;
    private volatile int mRadius = 24;
    private Bitmap mScreenShot;
    private Bitmap mWallpaper;

    private GLTextureView mView;

    private boolean mIsLiveWallpaper;
    private boolean mShouldRebindWallpaper = true;
    private volatile boolean isWallpaperChanged = true;
    private volatile boolean isScreenShotChanged = false;

    private Wallpaper mWallpaperItem;
    private Wallpaper mScreenShotItem;
    private GaussianItem mGaussianItem;
    private Context mContext;

    public GaussianBlurRenderer(Context launcher, GLTextureView glTextureView) {
        L.d(TAG, "GaussianBlurRenderer reCreate");
        mContext = launcher;
        mView = glTextureView;
        mWallpaperItem = new Wallpaper(mView);
        mScreenShotItem = new Wallpaper(mView);
        mGaussianItem = new GaussianItem(mView);
        mGaussianItem.setBlurRadius(mRadius);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        L.d(TAG, "onSurfaceCreated");
        isGLContextInit = true;
        mShouldRebindWallpaper = true;
        mWallpaperItem.create();
        if (!mOnlyDrawWallpaper) {
            mScreenShotItem.create();
        }
        mGaussianItem.create();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        L.d(TAG, "onSurfaceChanged");
        isGLContextInit = true;
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
    }

    private long startDrawTime;
    private long endDrawTime;
    private double fps;

    private boolean mOnlyDrawWallpaper = false;
    public void setOnlyDrawWallpaper(boolean onlyDrawWallpaper) {
        mOnlyDrawWallpaper = onlyDrawWallpaper;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        startDrawTime = System.currentTimeMillis();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        if (isWallpaperChanged || mShouldRebindWallpaper) {
            if (mWallpaper == null) {
                return;
            }
            mWallpaperItem.bindWallpaper(mWallpaper);
        }

        if (isScreenShotChanged && !mOnlyDrawWallpaper) {
            if (mScreenShot == null) {
                return;
            }
            mScreenShotItem.bindWallpaper(mScreenShot);
        }

        if (!mIsLiveWallpaper) {
            //mWallpaperItem.updateOffset(x, 0.5f, 0f, 0f, 0, 0);
        }

        mGaussianItem.bind();
        mWallpaperItem.draw();
        if (!mOnlyDrawWallpaper) {
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            mScreenShotItem.draw();
            GLES20.glDisable(GLES20.GL_BLEND);
        }
        mGaussianItem.draw();
        endDrawTime = System.currentTimeMillis();
        fps = 1000f / (endDrawTime - startDrawTime);
        L.d(TAG, " Draw gaussian blur FPS: " + fps);
    }

    public void setWallpaper(Bitmap wallpaper, boolean isLiveWallpaper) {
        L.d(TAG, "Update the wallpaper bitmap");
        if (wallpaper == null) {
            L.e(TAG, "Wallpaper can't be null");
            return;
        }
        mWallpaper = wallpaper;
        mIsLiveWallpaper = isLiveWallpaper;
        isWallpaperChanged = true;
    }

    public boolean hasWallpaper() {
        return mWallpaper != null;
    }

    private void onSurfaceDestroyImpl() {
        if (isGLContextInit) {
            isGLContextInit = false;
        }
    }

    /**
     * Set the blur radius, must greater that zero and letter than 32.
     *
     * @param blurRadius
     */
    public void setBlurRadius(int blurRadius) {
        mGaussianItem.setBlurRadius(blurRadius);
    }

    public void setScreenShot(Bitmap screenShot) {
        L.d(TAG, "Update the screen shot bitmap");
        if (screenShot == null) {
            L.e(TAG, "Screen shot can't be null");
            return;
        }
        mScreenShot = screenShot;
        isScreenShotChanged = true;
    }

    @Override
    public void onSurfaceCreate() {
        onSurfaceCreated(null, null);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        onSurfaceChanged(null, width, height);
    }

    @Override
    public boolean onDrawFrame() {
        onDrawFrame(null);
        return true;
    }

    @Override
    public void onSurfaceDestroy() {
        L.d(TAG, "onSurfaceDestroy");
        onSurfaceDestroyImpl();
        mWallpaperItem.destroy();
        if (!mOnlyDrawWallpaper) {
            mScreenShotItem.destroy();
        }
        mGaussianItem.destroy();
    }

    public void destroyContext() {
        mContext = null;
        mWallpaper = null;
        mScreenShot = null;
        mGaussianItem = null;
        mView = null;
    }
}
