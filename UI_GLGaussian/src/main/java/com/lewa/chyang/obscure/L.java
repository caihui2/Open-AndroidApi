package com.lewa.chyang.obscure;

import android.util.Log;

/**
 * Log utils provide static function of Log that contain classname and linenumber.
 * Created by jacob on 15-8-31.
 */
public class L {
    public static final String LAUNCHER_LOG_TAG_PREFIX = "THE_";
    private static final Tag TAG = new Tag("L");

    public static boolean DEBUG = true;

    public static final class Tag {
        private static final int MAX_TAG_LEN = 23 - LAUNCHER_LOG_TAG_PREFIX.length();

        final String mValue;

        public Tag(String tag) {
            final int lenDiff = tag.length() - MAX_TAG_LEN;
            if (lenDiff > 0) {
                w(TAG, "Tag " + tag + " is " + lenDiff + " chars longer than limit.");
            }
            mValue = LAUNCHER_LOG_TAG_PREFIX + (lenDiff > 0 ? tag.substring(0, MAX_TAG_LEN) : tag);
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public static void v(Tag tag,  String msg) {
        if (isLoggable()) {
            Log.v(tag.toString(), msg);
        }
    }

    public static void v(Tag tag,  String msg, Throwable tr) {
        if (isLoggable()) {
            Log.v(tag.toString(), msg, tr);
        }
    }

    public static void d(Tag tag,  String msg) {
        if (isLoggable()) {
            Log.d(tag.toString(), msg);
        }
    }

    public static void d(Tag tag,  String msg, Throwable tr) {
        if (isLoggable()) {
            Log.d(tag.toString(), msg, tr);
        }
    }

    public static void i(Tag tag,  String msg) {
        Log.i(tag.toString(), msg);
    }

    public static void i(Tag tag,  String msg, Throwable tr) {
        Log.i(tag.toString(), msg, tr);
    }

    public static void w(Tag tag,  String msg) {
        Log.w(tag.toString(), msg);
    }

    public static void w(Tag tag,  String msg, Throwable tr) {
        Log.w(tag.toString(), msg, tr);
    }

    public static void e(Tag tag,  String msg) {
        Log.e(tag.toString(), msg);
    }

    public static void e(Tag tag,  String msg, Throwable tr) {
        Log.e(tag.toString(), msg, tr);
    }

    private static boolean isLoggable() {
        return DEBUG;
    }
}
