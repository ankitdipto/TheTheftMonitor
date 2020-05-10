package com.example.myemailclient;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProtectionReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
            Runnable r = new Runnable() {
                @Override
                public void run() {


                    //Log.i("onStartCommand", "about to close");
                    //String actionIntent = intent.getStringExtra(context.getString(C0123R.string.receiver_reason));
                    boolean showing = ((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE)).isKeyguardLocked();
                    //boolean showing=false;
                    //Log.d("CAM_ACTION", "about to close keyguard " + showing);
                    if (showing) {

                        //Log.d("CAM_ACTION", "must close");
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //Log.m2v("Manda llamar cerrar");
                        Intent closeDialog = new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");
                        context.sendBroadcast(closeDialog);
                        //Log.m2v("cerrando");
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        context.sendBroadcast(closeDialog);
                    }
                }
            };
            Thread t= new Thread(r);
            t.start();
        }
    }
}
