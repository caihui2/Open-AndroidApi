//
// Created by chyang on 16/5/22.
//
#include <jni.h>

#ifndef ANDROIDAPIDOME_JNITEST_H
#define ANDROIDAPIDOME_JNITEST_H
#ifdef __cplusplus
extern "C" {
#endif //ANDROIDAPIDOME_JNITEST_H

JNIEXPORT jstring JNICALL Java_com_chyang_androidapidome_jni_DomeJniActivity_getString(JNIEnv * env, jobject thiz);
JNIEXPORT jint JNICALL Java_com_chyang_androidapidome_jni_DomeJniActivity_getCount(JNIEnv * env, jobject thiz, jintArray jar);
JNIEXPORT jintArray JNICALL Java_com_chyang_androidapidome_jni_DomeJniActivity_getIntArray(JNIEnv * env, jobject thiz);

#ifdef __cplusplus
 }
#endif
#endif