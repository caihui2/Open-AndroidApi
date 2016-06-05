package com.chyang.androidapidome.jni;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.chyang.androidapidome.R;

public class DomeJniActivity extends AppCompatActivity {

    private TextView mTextView;

    static {
        System.loadLibrary("jniTest");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dome_jni);
        mTextView = (TextView) findViewById(R.id.text);
       mTextView.setText(getString());
        int a[] = {1,2,3,4,5,6,7,8,9,10};
        int count =  getCount(a);

        int result[] = getIntArray();

        for(int i = 0; i < result.length; i++)
        {
            System.out.println(result[i]);
        }
    }


    private native String  getString () ;
    private native int  getCount(int [] a);
    private native int[] getIntArray();
}
