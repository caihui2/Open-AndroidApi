package com.chyang.androidapidome.Service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chyang.androidapidome.IMyAidlInterface;
import com.chyang.androidapidome.R;

public class DomeServiceActivity extends AppCompatActivity implements View.OnClickListener {

  public  IMyAidlInterface mServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dome_service);
        findViewById(R.id.start_service).setOnClickListener(this);
        findViewById(R.id.stop_service).setOnClickListener(this);
        findViewById(R.id.binder_service).setOnClickListener(this);
        findViewById(R.id.call_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = new Intent();
        switch (v.getId()) {
            case R.id.start_service:
                mIntent.setClass(this, DomeService.class);
                startService(mIntent);
                break;
            case R.id.stop_service:
                mIntent.setClass(this, DomeService.class);
                stopService(mIntent);
                break;
            case R.id.binder_service:
                mIntent.setAction("com.chyang.service").setPackage("com.chyang.androidapidome");
                bindService(mIntent, new Binder(), BIND_AUTO_CREATE);
                break;
            case R.id.call_service:
                try {
                    mServices.tosts();
                } catch (RemoteException e) {
                    e.printStackTrace();

                }
                break;
        }
    }

  public  class Binder implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServices = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
