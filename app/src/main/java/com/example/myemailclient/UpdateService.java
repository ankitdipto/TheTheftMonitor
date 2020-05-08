package com.example.myemailclient;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class UpdateService extends Service {
    boolean result=false;
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (!screenOn) {
            // YOUR CODE
            result=((KeyguardManager)getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE)).isKeyguardLocked();
            if(result==true) {
                startService(new Intent(getApplicationContext(), DemoCamService.class));
            }
        } else {
            // YOUR CODE
            //startService(new Intent(getApplicationContext(),DemoCamService.class));
        }
        return super.onStartCommand(intent,flags,startId);
    }
}
