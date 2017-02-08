package com.example.jzhao.bttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrash.log("Activity created");
        FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught");
        FirebaseCrash.report(new Exception("Ah oh"));
        setContentView(R.layout.activity_main);
    }
}
