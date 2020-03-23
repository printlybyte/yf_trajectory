package com.yinfeng.yf_trajectory.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * ============================================
 * 描  述：
 * 包  名：com.lgd.test_app
 * 类  名：AlarmReceiver
 * 创建人：liuguodong
 * 创建时间：2020/2/19 11:49
 * ============================================
 **/
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (TextUtils.equals(action, IntentConst.Action.matchRemind)) {
            //通知比赛开始
            long matchID = intent.getLongExtra(Net.Param.ID, 0);
//            showNotificationRemindMe(context, matchID);
            Logger.i("接收到定时广播 更新联系人数据");
            Logger.i("接收到定时广播 更新联系人数据");
            Intent intentActon = new Intent(context, ContactWorkService.class);
            intentActon.setAction(IntentConst.Action.downloadcontact);
            context.startService(intentActon);


        }

    }
}
