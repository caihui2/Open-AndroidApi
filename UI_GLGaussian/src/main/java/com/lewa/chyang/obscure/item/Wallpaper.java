package com.lewa.chyang.obscure.item;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.renderscript.Matrix4f;
import android.view.View;


import com.lewa.chyang.obscure.L;
import com.lewa.chyang.obscure.ShaderProgram;
import com.lewa.chyang.obscure.utils.BufferUtils;
import com.lewa.chyang.obscure.utils.ShaderUtil;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_TEXTURE0;

public class Wallpaper implements GLItem {
    private static final L.Tag TAG = new L.Tag("Wallpaper");

    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    static final int T_COORDS_PER_VERTEX = 2;

    private WallpaperShader mProgram;

    private int mTexture;
    private View mView;
    private Bitmap mWallpaper;

    private short drawOrder[] = {0, 1, 2, 3}; // order to draw vertices

    private int mLastSurfaceWidth;
    private int mLastSurfaceHeight;
    private boolean mOffsetsChanged = true;
    private boolean mRedrawNeeded = true;
    private int mLastXTranslation;
    private int mLastYTranslation;
    private float mXOffset = 0.5f;
    private float mYOffset = 0.5f;
    private float mScale = 1.0f;

    final Rect frame = new Rect();
    boolean isBindWallpaper = false;
    public Wallpaper(View view) {
        mView = view;
    }

    public void bindWallpaper(Bitmap wallpaper) {
        if (wallpaper != null) {
            mWallpaper = wallpaper;
            mProgram.bindBitmapToTexture(mWallpaper, GLES20.GL_TEXTURE0, mTexture, false);
            isBindWallpaper = true;
        }
    }

    @Override
    public void create() {
        drawListBuffer = BufferUtils.getShortBuffer(drawOrder);
        mProgram = new WallpaperShader();
        mTexture = mProgram.genTexture();
    }

    @Override
    public void destroy() {
        drawListBuffer.clear();
        drawListBuffer = null;
        mProgram.deleteTexture(mTexture);
        mProgram.destroy();
        mWallpaper = null;
        mView = null;
    }

    @Override
    public void draw() {
        if (!isBindWallpaper) {
           L.e(TAG, "not bind wallpaper");
            return;
        }
        draw(true);
    }

    public void updateOffset(float xOffset, float yOffset,
                             float xOffsetStep, float yOffsetStep,
                             int xPixels, int yPixels) {
        if (mXOffset != xOffset || mYOffset != yOffset) {
            mXOffset = xOffset;
            mYOffset = yOffset;
            draw(true);
        }
    }

    private FloatBuffer createMesh(int left, int top, float right, float bottom) {
        final float[] verticesData = {
                left, bottom, 0.0f, 0.0f, 1.0f,
                right, bottom, 0.0f, 1.0f, 1.0f,
                right, top, 0.0f, 1.0f, 0.0f,
                left, top, 0.0f, 0.0f, 0.0f};
        final FloatBuffer triangleVertices = BufferUtils.getFloatBuffer(verticesData);
        return triangleVertices;
    }

    public void draw(boolean force) {
        mView.getDrawingRect(frame);
        final int dw = frame.width();
        final int dh = frame.height();
        boolean surfaceDimensionsChanged = dw != mLastSurfaceWidth
                || dh != mLastSurfaceHeight;

        boolean redrawNeeded = surfaceDimensionsChanged || force;
        if (!redrawNeeded && !mOffsetsChanged) {
            return;
        }

        // Center the scaled image
        mScale = Math.max(1f, Math.max(dw / (float) getWidth(),
                dh / (float) getHeight()));
        final int availw = dw - (int) (getWidth() * mScale);
        final int availh = dh - (int) (getHeight() * mScale);
        int xPixels = availw / 2;
        int yPixels = availh / 2;

        // Adjust the image for xOffset/yOffset values. If window manager is handling offsets,
        // mXOffset and mYOffset are set to 0.5f by default and therefore xPixels and yPixels
        // will remain unchanged
        final int availwUnscaled = dw - getWidth();
        final int availhUnscaled = dh - getHeight();
        if (availwUnscaled < 0)
            xPixels += (int) (availwUnscaled * (mXOffset - .5f) + .5f);
        if (availhUnscaled < 0)
            yPixels += (int) (availhUnscaled * (mYOffset - .5f) + .5f);

        mOffsetsChanged = false;
        if (surfaceDimensionsChanged) {
            mLastSurfaceWidth = dw;
            mLastSurfaceHeight = dh;
            redrawNeeded = true;
        }
        if (!redrawNeeded && xPixels == mLastXTranslation && yPixels == mLastYTranslation) {
            return;
        }
        mLastXTranslation = xPixels;
        mLastYTranslation = yPixels;

        drawWallpaperWithOpenGL(availw, availh, xPixels, yPixels);
    }

    private int getWidth() {
        return Math.round(mWallpaper.getWidth());// / IMAGE_SCALE);
    }

    private int getHeight() {
        return Math.round(mWallpaper.getHeight());// / IMAGE_SCALE);
    }


    public void drawWallpaperWithOpenGL(int w, int h, int left, int top) {
        // Add program to OpenGL ES environment
        mProgram.useProgram();
        mProgram.enableAllVertexAttribArray();
        final float right = left + getWidth() * mScale;
        final float bottom = top + getHeight() * mScale;
        final Matrix4f ortho = new Matrix4f();
        ortho.loadOrtho(0, frame.width(), frame.height(), 0f, -1f ,1f);

        mProgram.loadUniformMatrix4fv(mProgram.mProjectionHandler, 1, false, ortho.getArray(), 0);

        vertexBuffer = createMesh(left, top, right, bottom);
        // Prepare the triangle coordinate data
        vertexBuffer.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(mProgram.mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                TRIANGLE_VERTICES_DATA_STRIDE_BYTES, vertexBuffer);
        vertexBuffer.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
        GLES20.glVertexAttribPointer(mProgram.mCoordinatesHandle, T_COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                TRIANGLE_VERTICES_DATA_STRIDE_BYTES, vertexBuffer);

        // Set color for drawing the triangle
        mProgram.loadTexture(GLES20.GL_TEXTURE_2D, mTexture);

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        mProgram.disEnableAllVertexAttribArray();
    }


    class WallpaperShader extends ShaderProgram {

        private int mPositionHandle;
        private int mCoordinatesHandle;
        private int mTextureHandle;
        private int mProjectionHandler;

        public WallpaperShader() {
            String vertexShaderCode =
                    "attribute vec4 vPosition;" +
                            "attribute vec2 vCoordinates;" +
                            "uniform mat4 vProjection;" +
                            "varying vec2 outCoordinates;" +
                            "void main() {" +
                            "   gl_Position = vProjection * vPosition;" +
                            "   outCoordinates = vCoordinates;" +
                            "}";
            String fragmentShaderCode =
                    "precision mediump float;" +
                            "uniform vec4 vColor;" +
                            "uniform sampler2D texture;" +
                            "varying vec2 outCoordinates;" +
                            "void main() {" +
                            "\n  gl_FragColor = texture2D(texture, outCoordinates);" +
                            "\n}";
            mProgram = ShaderUtil.createProgram(vertexShaderCode, fragmentShaderCode);
        }

        public WallpaperShader(String vertexShaderSource, String fragmentShaderSource) {
            super(vertexShaderSource, fragmentShaderSource);
        }

        @Override
        protected void getAllUniformLocation() {
            mTextureHandle = GLES20.glGetUniformLocation(mProgram, "texture");
            mProjectionHandler = GLES20.glGetUniformLocation(mProgram, "vProjection");
        }

        @Override
        protected void getAllAttributeLocation() {
            //notice don't not use glBindAttribLocation
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            mCoordinatesHandle = GLES20.glGetAttribLocation(mProgram, "vCoordinates");
        }

        @Override
        public void enableAllVertexAttribArray() {
            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glEnableVertexAttribArray(mCoordinatesHandle);
        }

        @Override
        public void disEnableAllVertexAttribArray() {
            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mCoordinatesHandle);
        }

        public void bindBitmapToTexture(Bitmap bitmap, int activeTexture, int texture, boolean recycle) {
            GLES20.glActiveTexture(activeTexture);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            if (recycle) {
                bitmap.recycle();
            }
        }

        public void loadTexture(int target, int texutre) {
            GLES20.glActiveTexture(GL_TEXTURE0);
            GLES20.glBindTexture(target, texutre);
            loadUniformInt(mTextureHandle, 0);
        }
    }
}
