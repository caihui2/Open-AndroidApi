package com.lewa.chyang.obscure;

import android.opengl.GLES20;


import com.lewa.chyang.obscure.utils.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by alex on 15-12-21.
 */
public abstract class ShaderProgram {
    protected int mProgram;
    public ShaderProgram() {}

    public ShaderProgram(String vertexShaderSource, String fragmentShaderSource) {
        mProgram = ShaderUtil.createProgram(vertexShaderSource, fragmentShaderSource);
    }

    protected abstract void getAllUniformLocation();

    protected abstract void getAllAttributeLocation();

    public abstract void enableAllVertexAttribArray();

    public abstract void disEnableAllVertexAttribArray();

    protected void bindAttributeLocation(int index, String name) {
        GLES20.glBindAttribLocation(mProgram, index, name);
    }

    public void loadUniformInt(int loc, int value) {
        GLES20.glUniform1i(loc, value);
    }

    public void loadUniformFloat(int loc, float value) {
        GLES20.glUniform1f(loc, value);
    }

    public void loadUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {
        GLES20.glUniformMatrix4fv(location, count, transpose, value, offset);
    }

    public void loadUniform2f(int location, float x, float y) {
        GLES20.glUniform2f(location, x, y);
    }

    public void useProgram() {
        GLES20.glUseProgram(mProgram);
        getAllAttributeLocation();
        getAllUniformLocation();
    }


    public int genTexture() {
        int[] texts = new int[1];
        GLES20.glGenTextures(1, texts, 0);
        return texts[0];
    }

    public int genTargetTexture(int tWidth, int tHeight, int activeTexture) {
        GLES20.glActiveTexture(activeTexture);
        int texture = genTexture();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        int[] buf = new int[tWidth * tHeight];
        IntBuffer texBuffer = ByteBuffer.allocateDirect(buf.length
                * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        texBuffer.position(0);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, tWidth, tHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, texBuffer);

        return texture;
    }

    public int getProgram() {
        return mProgram;
    }

    public void deleteTexture(int texture) {
        GLES20.glDeleteTextures(1, new int[]{texture}, 0);
    }

    public void deleteTextures(int [] textures) {
        GLES20.glDeleteTextures(textures.length, textures, 0);
    }

    public void destroy() {
        GLES20.glDeleteProgram(mProgram);
    }
}
