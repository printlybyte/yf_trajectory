package com.yinfeng.yf_trajectory.moudle.utils;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：NotificationManagerUtils
 * 创建人：liuguodong
 * 创建时间：2019/9/10 11:23
 * ============================================
 **/

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

import com.blankj.utilcode.util.VibrateUtils;
import com.caitiaobang.core.app.app.Latte;
import com.yinfeng.yf_trajectory.moudle.activity.MapActivity;
import com.yinfeng.yf_trajectory.moudle.login.SplashActivity;


public class NotificationManagerUtils {

    //开始通知


    @SuppressLint("WrongConstant")
    public static void startNotificationManager(String title, int idIco) {
//        VibrateUtils.vibrate(500);
        Context applicationContext = Latte.getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + "com.yinfeng.yf_trajectory"));
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0);
        long[] vibrate = {0, 500, 1000, 1500};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder notification = new Notification
                    .Builder(applicationContext)
                    .setContentTitle("通知")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(idIco)
                    .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
                    .setVibrate(vibrate)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setChannelId(applicationContext.getPackageName())
                    .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
            NotificationChannel channel = new NotificationChannel(
                    applicationContext.getPackageName(),
                    "银丰轨迹",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0, notification.build());
        } else {

            Notification.Builder notification = new Notification
                    .Builder(applicationContext)
                    .setContentTitle("通知")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(idIco)
                    .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
                    .setVibrate(vibrate)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
            notificationManager.notify(0, notification.build());
        }
    }



    @SuppressLint("WrongConstant")
    public static void startBootNotificationManager(String title, int idIco) {
//        VibrateUtils.vibrate(500);
        Context applicationContext = Latte.getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(applicationContext, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0);
        long[] vibrate = {0, 500, 1000, 1500};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder notification = new Notification
                    .Builder(applicationContext)
                    .setContentTitle("通知")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(idIco)
                    .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
                    .setVibrate(vibrate)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setChannelId(applicationContext.getPackageName())
                    .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
            NotificationChannel channel = new NotificationChannel(
                    applicationContext.getPackageName(),
                    "银丰轨迹",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0, notification.build());
        } else {

            Notification.Builder notification = new Notification
                    .Builder(applicationContext)
                    .setContentTitle("通知")
                    .setContentText(title)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(idIco)
                    .setLargeIcon(BitmapFactory.decodeResource(applicationContext.getResources(), idIco))
                    .setVibrate(vibrate)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(MediaStore.Audio.Media.INTERNAL_CONTENT_URI);
            notificationManager.notify(0, notification.build());
        }
    }
}

