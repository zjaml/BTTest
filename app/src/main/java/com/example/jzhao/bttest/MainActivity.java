package com.example.jzhao.bttest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;


/*
This activity maintain an instance of BluetoothClient which manages the connection and communication to remote device.
The target device need to be paired first, and its nickname be set as parameter for the BluetoothClient.

The BluetoothClient instance is created at OnCreate, at OnResume, check the connection state and try reconnect it if it is not connected.
This activity listens to the message sent by BluetoothClient to get connect/disconnect event and get messages received from the remote device.
When trying connect, the activity will retry connect in a interval until retry limit is reached.
 */
public class MainActivity extends AppCompatActivity {

    public static final String TARGET_DEVICE_NAME = "vivo X7";
    //static inner class doesn't hold an implicit reference to the outer class
    private static class MyHandler extends Handler {
        //Using a weak reference means you won't prevent garbage collection
        private final WeakReference<MainActivity> mainActivityWeakReference;

        MyHandler(MainActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
//                ...do work here...
            }
        }
    }

    private int mRetried = 0; //reset when connect succeed.
    private BluetoothClient mBluetoothClient = null;
    private final MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBluetoothClient = new BluetoothClient(this, myHandler, TARGET_DEVICE_NAME);
        //will this work without retry? will the Socket.connect block on when remote device is not in range?
        mBluetoothClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBluetoothClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(mBluetoothClient != null && mBluetoothClient.getState() != BluetoothClient.STATE_CONNECTED) {
//            mBluetoothClient.connect();
//        }
    }


}
