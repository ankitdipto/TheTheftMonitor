package com.example.myemailclient;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ProtectionService extends Service {

    ProtectionReceiver preceiver=new ProtectionReceiver();
    public ProtectionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        IntentFilter infilter=new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        registerReceiver(preceiver,infilter);
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy()
    {
        try
        {
            unregisterReceiver(preceiver);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
