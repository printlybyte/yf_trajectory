package com.yinfeng.yf_trajectory.moudle.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.utils.NotificationManagerUtils;

public class BootStartService extends Service {
    public BootStartService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManagerUtils.startNotificationManager("android 10 restart", R.mipmap.ic_app_start_icon);
    }


}
