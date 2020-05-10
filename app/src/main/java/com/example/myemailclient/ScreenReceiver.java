package com.example.myemailclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    private boolean screenOff;
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("CAM_ACTION","in onReceive function");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
            Intent intent2=new Intent(context,DemoCamService.class);
            context.startService(intent2);
        }
        //Intent i = new Intent(context, UpdateService.class);
        //i.putExtra("screen_state", screenOff);
        //context.startService(i);
    }
}
