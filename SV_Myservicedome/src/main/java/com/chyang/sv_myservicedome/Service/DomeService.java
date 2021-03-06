package com.chyang.sv_myservicedome.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.chyang.androidapidome.IMyAidlInterface;

/**
 * Created by chyang on 16/5/20.
 */
public class DomeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "onBind", Toast.LENGTH_SHORT).show();
        return mDomeBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"onDestory", Toast.LENGTH_SHORT).show();
    }

    public void print() {
        Toast.makeText(this, "调用了serivce的方法",Toast.LENGTH_SHORT).show();
    }


    class DomeBinder extends IMyAidlInterface.Stub {

        @Override
        public void tosts() throws RemoteException {
            print();
        }
    }

    private final DomeBinder mDomeBinder = new DomeBinder();
}
