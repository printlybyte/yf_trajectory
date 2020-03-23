package com.yinfeng.yf_trajectory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.moudle.login.SplashActivity;


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
        String action = intent.getAction();
        try {
            startOneActivity(context);
        } catch (Exception e) {
            Logger.i("开机广播： " + e.getMessage() + "   " + e);
            Toast.makeText(context, "" + e.getMessage() + "   " + e, Toast.LENGTH_SHORT).show();
        }
//        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
//        }

    }

    private void startOneActivity(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
