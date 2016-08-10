package com.lewa.chyang.obscure.item;

import android.opengl.GLES20;
import android.renderscript.Matrix4f;
import android.view.View;


import com.lewa.chyang.obscure.ShaderProgram;
import com.lewa.chyang.obscure.renderer.Framebuffer;
import com.lewa.chyang.obscure.utils.BufferUtils;
import com.lewa.chyang.obscure.utils.GaussianFunction;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_TEXTURE0;

/**
 * Created by alex on 16-6-28.
 */
public class GaussianItem implements GLItem {

    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
    private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
    private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;

    private FloatBuffer vertexBuffer;
    private FloatBuffer vertexFixedBuffer;
    private ShortBuffer drawListBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    static final int T_COORDS_PER_VERTEX = 2;

    public View mView;

    private GaussianShader mProgram;
    private Framebuffer mFramebuffer;
    private int mSourceTexture;
    private int mVertTargetTexture;
    private int mHoriTargetTexture;


    private int mRadius = 0;
    private int mBlurStep = 1;
    private float [] mWeights;


    public GaussianItem(View view) {
        mView = view;
    }

    public void bind() {
        mFramebuffer.setTargetTexture(mSourceTexture);
        mFramebuffer.bind();
    }

    public void unBind() {
        mFramebuffer.unbind();
    }

    private short drawOrder[] = {0, 1, 2, 3}; // order to draw vertices

    @Override
    public void create() {
        mProgram = new GaussianShader();

        int width = mView.getWidth();
        int height = mView.getHeight();
        int tWidth = 64;
        int tHeight = (int) (height * tWidth * 1.0f / width);
        if (mFramebuffer == null) {
            mFramebuffer = new Framebuffer(tWidth, tHeight);
        }
        mSourceTexture = mProgram.genTargetTexture(tWidth, tHeight, GLES20.GL_TEXTURE0);
        mVertTargetTexture = mProgram.genTargetTexture(tWidth, tHeight, GLES20.GL_TEXTURE1);
        mHoriTargetTexture = mProgram.genTargetTexture(tWidth, tHeight, GLES20.GL_TEXTURE2);
        mFramebuffer = new Framebuffer(tWidth, tHeight);
        drawListBuffer = BufferUtils.getShortBuffer(drawOrder);
    }

    @Override
    public void destroy() {
        int[] textures = new int[] {mSourceTexture, mVertTargetTexture, mHoriTargetTexture};
        mProgram.deleteTextures(textures);
        mProgram.destroy();
        mFramebuffer.destroy();
        mProgram = null;
        mFramebuffer = null;
        drawListBuffer.clear();
        drawListBuffer = null;
    }

    /**
     * Set the blur radius, must greater that zero and letter than 32.
     *
     * @param blurRadius
     */
    public void setBlurRadius(int blurRadius) {
        if (blurRadius < 0 || blurRadius > 32) {
            throw new IllegalArgumentException("blur radius must greater that zero and letter than 32.");
        }
        if (mRadius != blurRadius) {
            mRadius = blurRadius;
            mWeights =  GaussianFunction.getWeights(mRadius);
        }
    }

    public int getRadius() {
        return mRadius;
    }


    @Override
    public void draw() {
        mProgram.useProgram();
        mProgram.enableAllVertexAttribArray();

        final Matrix4f ortho = new Matrix4f();
        ortho.loadOrtho(0, mView.getWidth(), mView.getHeight(), 0f, -1f, 1f);

        mProgram.loadUniformMatrix4fv(mProgram.mUniformProjectionHandler, 1, false, ortho.getArray(), 0);

        mProgram.loadUniformInt(mProgram.mUniformSamplerStepHandler, mBlurStep);
        mProgram.loadUniformInt(mProgram.mUniformWidthHandler, mFramebuffer.getWidth());
        mProgram.loadUniformInt(mProgram.mUniformHeightHandler, mFramebuffer.getHeight());


        final int radius = getRadius();
        mProgram.loadUniformInt(mProgram.mUniformBlurRadiusHandler, radius);

        final float[] tmp = new float[16];
        System.arraycopy(mWeights, 0, tmp, 0, mWeights.length > 16 ? 16 : mWeights.length);
        mProgram.loadUniformMatrix4fv(mProgram.mUniformWeight1Handler, 1, false, tmp, 0);

        if (radius > 16) {
            System.arraycopy(mWeights, 16, tmp, 0, mWeights.length - 16);
            mProgram.loadUniformMatrix4fv(mProgram.mUniformWeight2Handler, 1, false, tmp, 0);
        }


        mProgram.loadUniformInt(mProgram.mUniformHeightHandler, mFramebuffer.getHeight());

        vertexBuffer = createMesh(0, 0, mView.getWidth(), mView.getHeight());
        // Prepare the triangle coordinate data
        vertexBuffer.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
        GLES20.glVertexAttribPointer(mProgram.mAttribPositionHandler, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                TRIANGLE_VERTICES_DATA_STRIDE_BYTES, vertexBuffer);
        vertexBuffer.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
        GLES20.glVertexAttribPointer(mProgram.mAttribCoordinateHandler, T_COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                TRIANGLE_VERTICES_DATA_STRIDE_BYTES, vertexBuffer);

        // render 1
        {
            mFramebuffer.resetTargetTexture(mVertTargetTexture);
            mProgram.loadUniform2f(mProgram.mUniformBlurDirHandler, 1.0f, 0.0f);
            mProgram.loadTexture(GLES20.GL_TEXTURE_2D, mSourceTexture);
            // Draw the square
            GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        }

        unBind();
        GLES20.glViewport(0, 0, mView.getWidth(), mView.getHeight());
        // render 2
        {
            mProgram.loadTexture(GLES20.GL_TEXTURE_2D, mVertTargetTexture);
            mProgram.loadUniform2f(mProgram.mUniformBlurDirHandler, 0.0f, 1.0f);
            GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        }
        mProgram.disEnableAllVertexAttribArray();
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

    public class GaussianShader extends ShaderProgram {

        private static final String vertexShaderCode =
                "\n attribute vec4 vPosition;" +
                        "\n attribute vec2 vCoordinates;" +
                        "\n uniform mat4 vProjection;" +
                        "\n varying vec2 outTexCoords;" +
                        "\n void main() {" +
                        "\n    gl_Position = vProjection * vPosition;" +
                        "\n    outTexCoords = vCoordinates;" +
                        "\n }";

        private static final String fragmentShaderCode =
                        "\n precision highp float;" +
                        "\n uniform sampler2D sampler;" +
                        "\n uniform int blurRadius;" +
                        "\n uniform vec2 blurDirection;" +
                        "\n uniform mat4 weights1;" +
                        "\n uniform mat4 weights2;" +
                        "\n uniform int samplerStep;" +
                        "\n uniform int width;" +
                        "\n uniform int height;" +
                        "\n uniform int action;" +
                        "\n varying vec2 outTexCoords;" +

                        "\n void main() {" +
                        "\n     vec4 sum = vec4(0.0);" +
                        "\n         sum += texture2D(sampler, outTexCoords) * weights1[0][0];" +
                        "\n         float step = 1.0 / float(width);" +
                        "\n         if (blurDirection.x == 0.0) {" +
                        "\n              step = 1.0 / float(height);" +
                        "\n         }" +

                        "\n         vec2 tmp = step * float(samplerStep) * blurDirection;" +
                        "\n         int len = blurRadius > 16 ? 16 : blurRadius;" +
                        "\n         for (int i = 1; i < len; ++i) {" +
                        "\n              vec4 wv = weights1[i/4];" +
                        "\n              sum += texture2D(sampler, outTexCoords + float(i) * tmp) * wv[int(mod(float(i),  4.0))];" +
                        "\n              sum += texture2D(sampler, outTexCoords - float(i) * tmp) * wv[int(mod(float(i),  4.0))];" +
                        "\n         }" +

                        "\n         len = blurRadius > 16 ? blurRadius - 16 : 0;" +
                        "\n         for (int i = 0; i < len; ++i) {" +
                        "\n              vec4 wv = weights2[i/4];" +
                        "\n              sum += texture2D(sampler,  outTexCoords + float(i + 16) * tmp) * wv[int(mod(float(i),  4.0))];" +
                        "\n              sum += texture2D(sampler,  outTexCoords - float(i + 16) * tmp) * wv[int(mod(float(i),  4.0))];" +
                        "\n         }" +
                        "\n         sum.a = 1.0;\n" +
                        "\n     gl_FragColor = sum;\n" +
                        "\n     //gl_FragColor = vec4(1.0, 0.0, 0.0, 0.5);\n" +
                        "\n }\n";

        private int mUniformTextureHandler;
        private int mUniformBlurRadiusHandler;
        private int mUniformBlurDirHandler;
        private int mUniformWeight1Handler;
        private int mUniformWeight2Handler;
        private int mUniformSamplerStepHandler;
        private int mUniformWidthHandler;
        private int mUniformHeightHandler;
        private int mUniformProjectionHandler;

        private int mAttribPositionHandler = 0;
        private int mAttribCoordinateHandler = 1;

        public GaussianShader() {
            this(vertexShaderCode, fragmentShaderCode);
        }

        public GaussianShader(String vertexShaderSource, String fragmentShaderSource) {
            super(vertexShaderSource, fragmentShaderSource);
        }

        @Override
        protected void getAllUniformLocation() {
            mUniformTextureHandler = GLES20.glGetUniformLocation(mProgram, "sampler");
            mUniformBlurRadiusHandler = GLES20.glGetUniformLocation(mProgram, "blurRadius");
            mUniformBlurDirHandler = GLES20.glGetUniformLocation(mProgram, "blurDirection");
            mUniformWeight1Handler = GLES20.glGetUniformLocation(mProgram, "weights1");
            mUniformWeight2Handler = GLES20.glGetUniformLocation(mProgram, "weights2");
            mUniformSamplerStepHandler = GLES20.glGetUniformLocation(mProgram, "samplerStep");
            mUniformWidthHandler = GLES20.glGetUniformLocation(mProgram, "width");
            mUniformHeightHandler = GLES20.glGetUniformLocation(mProgram, "height");
            mUniformProjectionHandler = GLES20.glGetUniformLocation(mProgram, "vProjection");
        }

        @Override
        protected void getAllAttributeLocation() {
            //do not use glBindAttribLocation
            mAttribPositionHandler = GLES20.glGetAttribLocation(mProgram, "vPosition");
            mAttribCoordinateHandler = GLES20.glGetAttribLocation(mProgram, "vCoordinates");
        }

        @Override
        public void enableAllVertexAttribArray() {
            GLES20.glEnableVertexAttribArray(mAttribPositionHandler);
            GLES20.glEnableVertexAttribArray(mAttribCoordinateHandler);
        }

        @Override
        public void disEnableAllVertexAttribArray() {
            GLES20.glDisableVertexAttribArray(mAttribPositionHandler);
            GLES20.glDisableVertexAttribArray(mAttribCoordinateHandler);
        }

        public void loadTexture(int target, int texutre) {
            GLES20.glActiveTexture(GL_TEXTURE0);
            GLES20.glBindTexture(target, texutre);
            loadUniformInt(mUniformTextureHandler, 0);
        }
    }
}
