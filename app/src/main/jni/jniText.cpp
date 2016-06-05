//
// Created by chyang on 16/5/22.
//
#include "jniTest.h"
#include <cstdio>

JNIEXPORT jstring JNICALL Java_com_chyang_androidapidome_jni_DomeJniActivity_getString(JNIEnv * env, jobject thiz)
{

    return env->NewStringUTF("ssss");
}

JNIEXPORT jint JNICALL Java_com_chyang_androidapidome_jni_DomeJniActivity_getCount(JNIEnv * env, jobject thiz, jintArray jar)
{
    jint * ca = env->GetIntArrayElements(jar, false);
    if(ca == NULL)
    {
        return 0;
    }

    jint cont = 0;
    for(int i = 0; i < 11; i++)
    {
        cont += i;
    }

    return cont;

}

JNIEXPORT jintArray JNICALL Java_com_chyang_androidapidome_jni_DomeJniActivity_getIntArray(JNIEnv * env, jobject thiz)
{
    int size = 20;
    jint tmp[size];
    jintArray  result = env->NewIntArray(size);
    for(int i = 0; i < size; i++) {
        tmp[i] = i;
    }

    env->SetIntArrayRegion(result, 0, size, tmp);
    return result;


}

