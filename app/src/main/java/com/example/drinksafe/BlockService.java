package com.example.drinksafe;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by MoonKyuTae on 2017-11-28.
 */

public class BlockService extends Service {
    Intent intent2;
    public void onCreate() {
        super.onCreate();
        intent2 = new Intent(WindowChangeDetectingService.ACTION_BLOCK);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        intent2.setClass(getApplicationContext(), WindowChangeDetectingService.class);
        getApplicationContext().startService(intent2);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().stopService(intent2);
    }
}
