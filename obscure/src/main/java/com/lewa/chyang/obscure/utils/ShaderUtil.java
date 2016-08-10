/*
 * Copyright (C) 2014 The Alex Study Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lewa.chyang.obscure.utils;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.opengl.GLES20;


import com.lewa.chyang.obscure.L;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ShaderUtil can load the shader source from the asset director.
 * <p/>
 * Author: Alex Liu
 * Date: 15-1-20
 * Version: 1.0
 */
public class ShaderUtil {

    private static final L.Tag TAG = new L.Tag("ShaderUtil");

    /**
     * Load the special shader, such as vertex shader or fragment shader
     *
     * @param shaderType the shader type, must be one of
     *                   android.opengl.GLES20.GL_FRAGMENT_SHADER and android.opengl.GLES20.GL_VERTEX_SHADER
     * @param source     the shader source.
     * @return the handle of shader
     */
    public static int loadShader(int shaderType, String source) {
        if (shaderType != GLES20.GL_VERTEX_SHADER && shaderType != GLES20.GL_FRAGMENT_SHADER) {
            throw new RuntimeException("Not support shader type");
        }
        int shaderHandle = GLES20.glCreateShader(shaderType);
        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, source);
            GLES20.glCompileShader(shaderHandle);
            int[] compileStates = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStates, 0);
            if (compileStates[0] == GLES20.GL_FALSE) {
                String error = GLES20.glGetShaderInfoLog(shaderHandle);
                GLES20.glDeleteShader(shaderHandle);
                L.e(TAG, "Compile Shader Error: " + " \n" + error);
                throw new RuntimeException("Compile Shader Error: " + error);
            }
        }
        return shaderHandle;
    }

    /**
     * Use the vertex shader source and fragment shader source to create a gles program.
     *
     * @param vertexSource   vertex shader source
     * @param fragmentSource fragment shader source
     * @return the handle of gles program.
     */
    public static int createProgram(String vertexSource, String fragmentSource) {
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
            GLES20.glAttachShader(programHandle, vertexShader);
            checkGLError(TAG, " Attach Vertex Shader");
            GLES20.glAttachShader(programHandle, fragmentShader);
            checkGLError(TAG, "Attach Fragment Shader");
            GLES20.glLinkProgram(programHandle);
            int[] linkState = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkState, 0);
            if (linkState[0] == GLES20.GL_FALSE) {
                GLES20.glDeleteProgram(programHandle);
                throw new RuntimeException("Link Program Error");
            }
            GLES20.glDeleteShader(vertexShader);
            GLES20.glDeleteShader(fragmentShader);
            GLES20.glReleaseShaderCompiler();
        }
        return programHandle;
    }

    /**
     * Check is or not has error occur in OpenGL ES. if has error occur. throw a Runtime Exception.
     *
     * @param tag       Log TAG.
     * @param operation the description of operation
     */
    public static void checkGLError(L.Tag tag, String operation) {
        int errorCode = -1;
        while ((errorCode = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            String errorMessage = operation + " : GLError (" + errorCode + ")";
            L.e(tag, errorMessage);
        }
    }

    /**
     * Load a file which is place the asset folder.
     *
     * @param fileName  the file name
     * @param resources android context resources object.
     * @return the content of file.
     */
    public static String loadFromAssetsFile(String fileName, Resources resources) {
        String result = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = resources.getAssets().open(fileName, AssetManager.ACCESS_STREAMING);
            out = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[128];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            result = new String(out.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}

