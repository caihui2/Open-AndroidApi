package com.chyang.androidapidome.opencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.chyang.androidapidome.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class OpenCvDomeActivity extends AppCompatActivity {

    private ImageView mImageView;

    BaseLoaderCallback mBaseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
        }
    };

    static {
        System.loadLibrary("libopencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cv_dome);
        mImageView = (ImageView) findViewById(R.id.image);
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //获取lena彩色图像所对应的像素数据
        Utils.bitmapToMat(mBitmap, rgbMat);
        //将彩色图像数据转换为灰度图像数据并存储到grayMat中
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
        //创建一个灰度图像
        Bitmap grayBmp = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.RGB_565);
        //将矩阵grayMat转换为灰度图像
        Utils.matToBitmap(grayMat, grayBmp);
        mImageView.setImageBitmap(grayBmp);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("a", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mBaseLoaderCallback);
        } else {
            Log.d("a", "OpenCV library found inside package. Using it!");
            mBaseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}
