package com.example.jzhao.bttest;

import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
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
//todo: listen to bluetooth state change event and connect bluetooth client when bluetooth get switched on
public class MainActivity extends AppCompatActivity {
    public static final String TARGET_DEVICE_NAME = "Nexus 7";
    //static inner class doesn't hold an implicit reference to the outer class
    private static class MyHandler extends Handler {
        private static final String TAG = "MainActivity";
        //Using a weak reference means you won't prevent garbage collection
        private final WeakReference<MainActivity> mainActivityWeakReference;

        MyHandler(MainActivity mainActivity) {
            mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "State Changed: " + msg.obj);
                    break;
                case Constants.MESSAGE_INCOMING_MESSAGE:
                    break;
            }
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
//                ...do work here...
            }
        }
    }

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
        Log.d("MainActivity", "onStart");
        mBluetoothClient = new BluetoothClient(myHandler, TARGET_DEVICE_NAME);
        //will this work without retry? will the Socket.connect block on when remote device is not in range?
        mBluetoothClient.connect();
        mBluetoothClient.getBluetoothBroadcastReceiver()
                .safeRegister(this, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
        //important to purge mBluetoothClient here as it cannot maintain correct state while the app get into background.
        //it must be recreated at onStart()
        mBluetoothClient.disconnect();
        mBluetoothClient.getBluetoothBroadcastReceiver().safeUnregister(this);
        mBluetoothClient = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //todo: review here
        if(mBluetoothClient != null && mBluetoothClient.getState() == BluetoothClient.STATE_NONE) {
            mBluetoothClient.connect();
            mBluetoothClient.getBluetoothBroadcastReceiver()
                    .safeRegister(this, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        }
    }


}
