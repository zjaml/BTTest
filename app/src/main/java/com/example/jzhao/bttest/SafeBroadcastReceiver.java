package com.example.jzhao.bttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * A BroadcastReceiver that's safe to safeRegister and safeUnregister.
 * (each can perform multiple times without error being thrown)
 */
public abstract class SafeBroadcastReceiver extends BroadcastReceiver {
    public boolean isRegistered;

    /**
     * safeRegister receiver
     *
     * @param context - Context
     * @param filter  - Intent Filter
     * @return see Context.registerReceiver(BroadcastReceiver,IntentFilter)
     */
    public Intent safeRegister(Context context, IntentFilter filter) {
        if (isRegistered)
            return null;
        isRegistered = true;
        return context.registerReceiver(this, filter);
    }

    /**
     * safeUnregister received
     *
     * @param context - context
     * @return true if was registered else false
     */
    public boolean safeUnregister(Context context) {
        if (isRegistered) {
            context.unregisterReceiver(this);  // edited
            isRegistered = false;
            return true;
        }
        return false;
    }
}