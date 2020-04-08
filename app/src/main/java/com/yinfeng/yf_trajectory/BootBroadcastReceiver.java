package com.yinfeng.yf_trajectory;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.moudle.login.SplashActivity;
import com.yinfeng.yf_trajectory.moudle.service.BootStartService;
import com.yinfeng.yf_trajectory.moudle.utils.NotificationManagerUtils;

import static com.yinfeng.yf_trajectory.moudle.utils.PermissionUtilsx.getSystemVersion;


/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.wypzh
 * 类  名：BootBroadcastReceiver
 * 创建人：liuguodong
 * 创建时间：2019/8/25 16:51
 * ============================================
 **/
public class BootBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        Log.i("testrexc","开机广播： ");

        Logger.i("开机广播： "+ getSystemVersion() );

        try {
            if (getSystemVersion() >= 1000) {
                NotificationManagerUtils.startBootNotificationManager("请点击此处打开银丰轨迹", R.mipmap.ic_app_start_icon);
            }else {
                startOneActivity(context);
            }

        }catch (Exception e){
            Logger.i("开机广播： " + e.getMessage() + "   " + e);
        }


//        String action = intent.getAction();
//        try {
//            startOneService(context);
//
////            startOneActivity(context);
//        } catch (Exception e) {
//            Log.i("BootBroadcastReceiver", e.getMessage() + "   " + e);
//            Logger.i("开机广播： " + e.getMessage() + "   " + e);
//            Toast.makeText(context, "" + e.getMessage() + "   " + e, Toast.LENGTH_SHORT).show();
//        }
//        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
//        }

    }

    private void startOneActivity(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startOneService(Context context) {
        Intent intent = new Intent(context, BootStartService.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startForegroundService(intent);
    }

}
