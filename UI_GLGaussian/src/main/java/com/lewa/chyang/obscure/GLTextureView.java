package com.lewa.chyang.obscure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.view.TextureView;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class GLTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    private final static L.Tag TAG = new L.Tag("GLTextureView");
    public final static int RENDERMODE_WHEN_DIRTY = 0;
    public final static int RENDERMODE_CONTINUOUSLY = 1;

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    private int mWidth;
    private int mHeight;

    private RenderThread mRenderThread;
    private Renderer mRenderer;
    private OnFrameAvailableListener mListener;

    private SurfaceTexture mSurfaceTexture;

    public interface Renderer {
        void onSurfaceCreate();

        void onSurfaceChanged(int width, int height);

        boolean onDrawFrame();

        void onSurfaceDestroy();
    }

    public interface OnFrameAvailableListener {
        void onFrameAvailable();
    }

    public interface OnFrameAvailableListenerPlus extends OnFrameAvailableListener {
        void onResultBitmap(Bitmap bitmap);
    }

    private final OnFrameAvailableListenerPlus mUpdateListener = new OnFrameAvailableListenerPlus() {
        @Override
        public void onFrameAvailable() {
            if (mListener != null) {
                mListener.onFrameAvailable();
            }
        }

        @Override
        public void onResultBitmap(Bitmap bitmap) {
            if (mListener instanceof OnFrameAvailableListenerPlus) {
                ((OnFrameAvailableListenerPlus) mListener).onResultBitmap(bitmap);
            }
        }
    };

    public GLTextureView(Context context) {
        this(context, null);
    }

    public GLTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GLTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWeakRefs = new WeakReference<>(this);
        setSurfaceTextureListener(this);
        L.d(TAG, "GLTextureView has generate ");
    }

    /**
     * Set the aspect ratio for this view.
     * This size of this view will be measured based on the ratio
     * calculated from the parameters.
     * Note that the actual size of parameters don't matter, that is
     * calling setAspectRatio(200, 300) and setAspectRatio(400, 600) make the same result.
     *
     * @param width  Relative horizontal size.
     * @param height Relative vertical size.
     */
    public void setAspectRatio(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Size can't be negative");
        }
        mRatioWidth = width;
        mRatioHeight = height;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mRatioWidth == 0 || mRatioHeight == 0) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

    public void onPause() {
        if (mRenderThread != null) {
            mRenderThread.pauseGLRender();
        }
    }

    public void onResume() {
        if (mRenderThread != null) {
            mRenderThread.resumeGLRender();
        }
    }

    public void setOnFrameAvailableListener(OnFrameAvailableListener listener) {
        mNeedResult = false;
        mListener = listener;
    }

    private boolean mNeedResult = false;
    public void setOnFrameAvailableListener(OnFrameAvailableListenerPlus listener) {
        mNeedResult = true;
        mListener = listener;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        L.d(TAG, "GLTextureView has get surface texture, now Give to openGL render context : " + surface + "  w:" + width + "  h:" + height);
        mSurfaceTexture = surface;
        mWidth = width;
        mHeight = height;
        if (mRenderThread != null) {
            mRenderThread.setSurfaceTexture(mSurfaceTexture, mWidth, mHeight);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        L.d(TAG, "GLTextureView's size has changed: SurfaceTexture: " + surface + "  Size = " + width + " x " + height);
        mWidth = width;
        mHeight = height;
        if (mRenderThread != null) {
            mRenderThread.setSize(mWidth, mHeight);
        }
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        L.d(TAG, "onSurfaceTextureDestroyed : " + surface);
        mSurfaceTexture = null;
        if (mRenderThread != null) {
            mRenderThread.destroySurfaceTexture();
        }
        return true;
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    public void setRenderer(Renderer renderer) {
        mRenderer = renderer;
        initRenderContext();
    }

    private WeakReference<GLTextureView> mWeakRefs;

    private boolean attched = false;
    private boolean detached = true;

    @Override
    protected void onAttachedToWindow() {
        L.d(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
        if (!attched && detached) {
            if (mRenderThread != null && !mRenderThread.isAlive()) {
                mRenderThread.start();
            }
        }
        attched = true;
        detached = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        L.d(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
        if (attched && !detached) {
            if (mRenderThread != null) {
                mRenderThread.stopRenderer();
            }
        }
        attched = false;
        detached = true;
    }

    private int mRenderMode = RENDERMODE_CONTINUOUSLY;

    public void setRenderMode(int renderMode) {
        mRenderMode = renderMode;
        if (mRenderThread != null) {
            mRenderThread.setRenderMode(renderMode);
        }
    }

    public void requestRender() {
        if (mRenderThread != null) {
            mRenderThread.requestRender();
        }
    }

    private void initRenderContext() {
        // I remove all this method code, there is no memory leak.
        if (mRenderer != null && mRenderThread == null) {
            L.d(TAG, "initRenderContext");
            mRenderThread = new RenderThread(mWeakRefs);
            mRenderThread.setRenderer(mRenderer);
            mRenderThread.setRenderMode(mRenderMode);
            if (mSurfaceTexture != null) {
                mRenderThread.setSurfaceTexture(mSurfaceTexture, mWidth, mHeight);
            }
            mRenderThread.start();
        } else if (mRenderer != null){
            mRenderThread.start();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroy();
    }

    public void destroy() {
        L.d(TAG, "Destroy Context");
        if (mRenderThread != null) {
            mRenderThread.stopRenderer();
            mRenderThread = null;
        }
        setSurfaceTextureListener(null);
        if (mWeakRefs != null) {
            mWeakRefs.clear();
            mWeakRefs = null;
        }
        mRenderer = null;
        if (mSurfaceTexture != null) {
            mSurfaceTexture = null;
        }
    }

    private class ThreadManager {
        private RenderThread mOwner;

        public ThreadManager(RenderThread thread) {
            mOwner = thread;
        }

        public void exit() {
            mOwner = null;
        }
    }

    private class RenderThread extends Thread {
        private final L.Tag TAG = new L.Tag("RenderThread");
        private SurfaceTexture mSurfaceTexture;
        private int mWidth;
        private int mHeight;

        private EGL10 mEgl;
        private EGLContext mEglContext;
        private EGLDisplay mEglDisplay;
        private EGLSurface mEglSurface;
        private EGLConfig mEglConfig;

        private Renderer mRenderer;

        private int mRenderMode = RENDERMODE_CONTINUOUSLY;

        private boolean mShouldExit;
        private boolean mRequestRender;
        private boolean mSurfaceSizeChanged;
        private boolean mHasEglSurface;
        private boolean mHasEglContext;
        private boolean mSurfaceTextureChanged;
        private boolean mHasEgl;
        private boolean mHasEglConfig;

        private boolean mNeedMakeCurrent = true;

        private boolean mRequestPaused = true;

        private ThreadManager mThreadManager;

        private WeakReference<GLTextureView> mWeakRef;

        public RenderThread(WeakReference<GLTextureView> view) {
            L.d(TAG, "new RenderThread()");
            mWeakRef = view;
            init();
        }

        private void init() {
            if (mRenderThread == null) {
                mThreadManager = new ThreadManager(this);
            }
        }

        public void setSize(int width, int height) {
            if (mWidth != width || mHeight != height) {
                mWidth = width;
                mHeight = height;
                mSurfaceSizeChanged = true;
            }
        }

        public void setSurfaceTexture(SurfaceTexture surfaceTexture, int mWidth, int mHeight) {
            if (mSurfaceTexture != surfaceTexture) {
                setSize(mWidth, mHeight);
                if (mHasEglSurface) {
                    destroyEglSurface();
                }
                mSurfaceTexture = surfaceTexture;
                mSurfaceTextureChanged = true;
                requestRender();
            }
        }

        private void createEGLAndDisplay() {
            // Get EGL
            if (!mHasEgl) {
                mEgl = (EGL10) EGLContext.getEGL();
                mHasEgl = true;
                L.d(TAG, "EGL create success");
            } else {
                L.e(TAG, "EGL has created, something has wrong please check : " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
            }
            // Get display device
            mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (mEglDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new IllegalStateException("Can't open connection to local windowing system");
            }
            /* Get version
            * version[0] major version code
            * version[1] minor version code
            */
            int version[] = new int[2];
            if (!mEgl.eglInitialize(mEglDisplay, version)) {
                destroyEGLAndDisplay();
                throw new IllegalStateException("Can't initialize EGL");
            } else {
                L.d(TAG, "EGL and EGL Display initialize success: mEgl = " + mEgl + " \tmEglDisplay = " + mEglDisplay);
                mHasEgl = true;
            }
        }

        private void destroyEGLAndDisplay() {
            if (mHasEgl) {
                if (mEgl.eglTerminate(mEglDisplay)) {
                    mEglDisplay = null;
                    mEgl = null;
                    mHasEgl = false;
                    L.d(TAG, "EGL and EGL Display destroy success");
                } else {
                    L.e(TAG, "EGL and EGL Display destroy has something error: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                }
            } else {
                if (mHasEgl) {
                    L.e(TAG, "EGL and EGL Display destroy error: please check did you create egl and display!: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                } else {
                    L.e(TAG, "EGL and EGL Display destroy error: please check did you create egl and display!: ");
                }
            }
        }

        private void createEglSurface() {
            //create EGL window surface
            if (!mHasEglSurface) {
                mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, mEglConfig, mSurfaceTexture, null);
                if (mEglSurface != null) {
                    mHasEglSurface = true;
                    L.d(TAG, "EGL Surface create success: mEglSurface =  " + mEglSurface);
                } else {
                    L.d(TAG, "EGL Surface create has something error: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                }
            } else {
                L.e(TAG, "EGL Surface create error: please check did you has created egl surface: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
            }
        }

        private void destroyEglSurface() {
            if (mHasEglSurface) {
                if (mEgl != null && mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
                    if (mEglSurface != null) {
                        if (mEgl.eglDestroySurface(mEglDisplay, mEglSurface)) {
                            mEglSurface = null;
                            mSurfaceTexture = null;
                            mHasEglSurface = false;
                            L.d(TAG, "EGL Surface destroy success!");
                        } else {
                            L.e(TAG, "EGL Surface destroy has something wrong: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                        }
                    } else {
                        L.e(TAG, "EGL Surface destroy has something wrong: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                    }
                } else {
                    L.e(TAG, "EGL Surface destroy error: make current error: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                }
            } else {
                if (mHasEgl) {
                    L.e(TAG, "EGL Surface destroy error: please check did you has created egl surface: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                } else {
                    L.e(TAG, "EGL Surface destroy error: please check did you has created egl surface: ");
                }
            }
            mSurfaceTexture = null;
        }

        private void createEglContext() {
            // create mEgl context
            if (!mHasEglContext) {
                mEglContext = createEGLContext(mEgl, mEglDisplay, mEglConfig);
                if (mEglContext != null) {
                    mHasEglContext = true;
                    L.d(TAG, "EGL Context created success: mEglContext = " + mEglContext);
                } else {
                    L.e(TAG, "EGL Context created has something wrong: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                }
            } else {
                if (mHasEgl) {
                    L.e(TAG, "EGL Context create error: please check did you has created egl context: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                } else {
                    L.e(TAG, "EGL Context create error: please check did you has created egl context: ");
                }
            }
        }

        private void destroyEglContext() {
            if (mHasEglContext) {
                if (mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
                    if (mEgl.eglDestroyContext(mEglDisplay, mEglContext)) {
                        mEglContext = null;
                        mHasEglContext = false;
                        L.d(TAG, "EGL Context destroy success.");
                    } else {
                        L.e(TAG, "EGL Context destroy has something wrong: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                    }
                } else {
                    L.e(TAG, "EGL Context destroy has something wrong: make current error: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                }
            } else {
                if (mHasEgl) {
                    L.e(TAG, "EGL Context destroy error: please check did you has created egl context: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                } else {
                    L.e(TAG, "EGL Context destroy error: please check did you has created egl context: ");
                }
            }
        }

        private void checkAndUseCurrentContext() {
            // set current renderer context
            try {
                if (mEglSurface == EGL10.EGL_NO_SURFACE || mEglContext == EGL10.EGL_NO_CONTEXT) {
                    throw new RuntimeException("GL error: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                }
                if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
                    throw new RuntimeException("GL Make current Error" + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                }
            } catch (Exception e) {
                L.e(TAG, "Init EGL Error: " + e.getLocalizedMessage());
            }
        }

        private void deInitEGL() {
            destroyEGLConfig();
            destroyEglSurface();
            destroyEglContext();
            destroyEGLAndDisplay();
            if (mWeakRef != null) {
                mWeakRef.clear();
            }
            mWeakRef = null;
            mRenderer = null;
        }

        private EGLConfig createEglConfig() {
            if (!mHasEglConfig) {
                int[] configsCount = new int[1];
                EGLConfig[] configs = new EGLConfig[1];
                int[] attrs = getEGLAttributes();
                if (!mEgl.eglChooseConfig(mEglDisplay, attrs, configs, configsCount.length, configsCount)) {
                    throw new IllegalArgumentException("Failed to choose EGL config");
                } else if (configsCount[0] > 0) {
                    mEglConfig = configs[0];
                }
                L.d(TAG, "EGL Config create success: mEglConfig = " + mEglConfig);
                mHasEglConfig = true;
            } else {
                if (mHasEgl) {
                    L.e(TAG, "EGL Config create has something wrong : please check your code : " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                } else {
                    L.e(TAG, "EGL Config create has something wrong : please check your code : ");
                }
            }
            return mEglConfig;
        }

        private void destroyEGLConfig() {
            if (mHasEglConfig) {
                mEglConfig = null;
                mHasEglConfig = false;
                L.d(TAG, "EGL Config destroy success. ");
            } else {
                if (mHasEgl) {
                    L.e(TAG, "EGL Config destroy has something wrong: please check your code :" + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                } else {
                    L.e(TAG, "EGL Config destroy has something wrong: please check your code ");
                }
            }
        }

        private int[] getEGLAttributes() {
            final int[] configs = new int[]{
                    EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                    EGL10.EGL_ALPHA_SIZE, 8,
                    EGL10.EGL_RED_SIZE, 8,
                    EGL10.EGL_GREEN_SIZE, 8,
                    EGL10.EGL_BLUE_SIZE, 8,
                    EGL10.EGL_DEPTH_SIZE, 0,
                    EGL10.EGL_STENCIL_SIZE, 0,
                    EGL10.EGL_NONE

            };
            return configs;
        }

        private EGLContext createEGLContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig) {
            final int[] attrs = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL10.EGL_NONE
            };
            return egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrs);
        }

        public void setRenderer(Renderer renderer) {
            mRenderer = renderer;
        }

        public void setRenderMode(int renderMode) {
            mRenderMode = renderMode;
        }

        @Override
        public void run() {
            try {
                init();
                guardedRun(); // remove this line no has memory leak.
            } catch (Exception e) {
                L.e(TAG, "Run Exception: " + e.getLocalizedMessage());
            } finally {
                mThreadManager.exit();
                mThreadManager = null;
            }
        }

        private void guardedRun() throws InterruptedException { // memory leak in this method.
            try {
                // We exec render code in try catch code
                mShouldExit = false;
                mHasEgl = false;
                mHasEglConfig = false;
                mHasEglContext = false;
                mHasEglSurface = false;
                mNeedMakeCurrent = true;
                mSurfaceSizeChanged = false;
                mSurfaceTextureChanged = false;
                if (mSurfaceTexture != null) {
                    mSurfaceTextureChanged = true;
                }
                if (mWidth != 0 && mHeight != 0) {
                    mSurfaceSizeChanged = true;
                }

                while (true) {
                    synchronized (mThreadManager) {
                        if (mShouldExit) {
                            break;
                        }

                        if (!mHasEgl) {
                            createEGLAndDisplay();
                        }

                        if (!mHasEglConfig) {
                            createEglConfig();
                            mHasEglSurface = false;
                            mHasEglContext = false;
                        }

                        boolean canCreateContext = false;
                        if (!mHasEglSurface && (mSurfaceSizeChanged || mSurfaceTextureChanged)) {
                            createEglSurface();
                            mSurfaceTextureChanged = false;
                            mNeedMakeCurrent = true;
                            mSurfaceSizeChanged = true;
                            canCreateContext = true;
                        }
                        boolean needNotifyCreate = false;
                        if (!mHasEglContext && canCreateContext) {
                            createEglContext();
                            mNeedMakeCurrent = true;
                            needNotifyCreate = true;
                        }

                        if (mNeedMakeCurrent && mHasEglSurface) {
                            checkAndUseCurrentContext();
                            if (needNotifyCreate) {
                                mRenderer.onSurfaceCreate();
                            }
                            mNeedMakeCurrent = false;
                            mSurfaceSizeChanged = true;
                        }
                    }

                    if (mSurfaceSizeChanged) {
                        mRenderer.onSurfaceChanged(mWidth, mHeight);
                        mSurfaceSizeChanged = false;
                    }
                    try {
                        if (readyToDraw() && onDraw()) {
                            if (mNeedResult) {
                                ByteBuffer buffer = ByteBuffer.allocate(mWidth * mHeight * 4);
                                GLES20.glReadPixels(0, 0, mWidth, mHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
                                Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
                                bitmap.copyPixelsFromBuffer(buffer);
                                mUpdateListener.onResultBitmap(bitmap);
                            }
                            if (!mEgl.eglSwapBuffers(mEglDisplay, mEglSurface)) {
                                L.e(TAG, "EGL SwapBuffers Error: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
                            } else {
                                mUpdateListener.onFrameAvailable();
                            }
                            mRequestRender = false;
                        } else if (mRenderMode == GLTextureView.RENDERMODE_WHEN_DIRTY || mRequestPaused) {
                            try {
                                synchronized (mThreadManager) {
                                    L.d(TAG, "Paused ........  Waiting notify");
                                    mThreadManager.wait();
                                    L.d(TAG, "Resume ........... get notify ");
                                }
                            } catch (InterruptedException e) {
                                L.e(TAG, "Pausing Render Thread Error: " + e.getLocalizedMessage());
                                return;
                            }
                        }
                    } catch (Exception e) {
                        mRequestRender = false;
                        L.d(TAG, "Draw Exception: " + e.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
                L.e(TAG, "GuardedRun end: " + e.getLocalizedMessage());
            } finally {
                mRenderer.onSurfaceDestroy();
                deInitEGL();
                L.d(TAG, "guardedRun() end!");
            }
        }

        private boolean readyToDraw() {
            return !mRequestPaused && mHasEglContext && mHasEglSurface && (mRequestRender || mRenderMode == GLTextureView.RENDERMODE_CONTINUOUSLY);
        }

        public void stopRenderer() {
            mShouldExit = true;
            notifyStartRender();
        }

        public void destroySurfaceTexture() {
            destroyEglSurface();
        }

        private boolean onDraw() {
            return mRenderer.onDrawFrame();
        }

        public void pauseGLRender() {
            mRequestPaused = true;
        }

        public void resumeGLRender() {
            mRequestPaused = false;
            mRequestRender = true;
            notifyStartRender();
        }

        public void requestRender() {
            mRequestRender = true;
            notifyStartRender();
        }

        private void notifyStartRender() {
            if (mThreadManager != null) {
                synchronized (mThreadManager) {
                    mThreadManager.notifyAll();
                }
            }
        }
    }

}
