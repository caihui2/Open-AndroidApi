package com.lewa.chyang.obscure.renderer;

import android.opengl.GLES20;

/**
 * Created by alex on 15-7-30.
 */
public class Framebuffer {
    private int mFbo;
    private int mTargetTexture;
    private int mDepthTexture; // not need the depth texture
    private int mWidth, mHeight;

    public Framebuffer(int width, int height) {
        mWidth = width;
        mHeight = height;
        createFramebuffer();
    }

    private void createFramebuffer() {
        int[] fbo = new int[1];
        GLES20.glGenFramebuffers(1, fbo, 0);
        mFbo = fbo[0];
    }

    public int getFBO() {
        return mFbo;
    }

    public int getDepthTexture() {
        // do not use
        if (true) {
            throw new RuntimeException("FrameBuffer create error");
        }
        return mDepthTexture;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void bind() {
        if (mTargetTexture <= 0) {
            throw new IllegalStateException("must set target texture when used");
        }
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFbo);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTargetTexture, 0);
        int state = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if (state != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            switch (state) {
                case GLES20.GL_FRAMEBUFFER_UNSUPPORTED:
                    throw new RuntimeException("unsupported");
                case GLES20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                    throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            }
            throw new RuntimeException("FrameBuffer not complete: State: " + state);
        }
    }

    public void unbind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public void setTargetTexture(int textures) {
        mTargetTexture = textures;
    }

    public void resetTargetTexture(int texture) {
        setTargetTexture(texture);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTargetTexture, 0);
    }

    public void destroy() {
        GLES20.glDeleteTextures(1, new int[]{mTargetTexture}, 0);
        GLES20.glDeleteFramebuffers(1, new int[]{mFbo}, 0);
        mFbo = 0;
    }
}
