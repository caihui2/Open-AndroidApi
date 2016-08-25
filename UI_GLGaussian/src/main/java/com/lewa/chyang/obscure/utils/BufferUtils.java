package com.lewa.chyang.obscure.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by alex on 15-7-7.
 */
public class BufferUtils {
    public static FloatBuffer getFloatBuffer(float[] datas) {
        FloatBuffer fb = ByteBuffer.allocateDirect(datas.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(datas);
        fb.position(0);
        return fb;
    }
    public static ShortBuffer getShortBuffer(short[] datas) {
        ShortBuffer fb = ByteBuffer.allocateDirect(datas.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        fb.put(datas);
        fb.position(0);
        return fb;
    }
}
