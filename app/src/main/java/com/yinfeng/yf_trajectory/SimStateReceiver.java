package com.yinfeng.yf_trajectory;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.caitiaobang.core.app.storge.LattePreference;
import com.orhanobut.hawk.Hawk;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory
 * 类  名：SimStateReceiver
 * 创建人：liuguodong
 * 创建时间：2019/8/22 10:01
 * ============================================
 **/
public class SimStateReceiver extends BroadcastReceiver {
    public final static String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_SIM_STATE_CHANGED.equals(intent.getAction())) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            int state = tm.getSimState();
            if (state == 1) {
                Log.i("testre", "sim 卡槽弹开 ：" + state);
                LattePreference.clear();
                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                mIntent.putExtra("result", ConstantApi.RECEVIER_NO_SIM_READY);
                //发送广播
                context.sendBroadcast(mIntent);
            }
        }
    }


}
