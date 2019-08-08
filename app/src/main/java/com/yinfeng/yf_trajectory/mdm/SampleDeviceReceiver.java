package com.yinfeng.yf_trajectory.mdm;//package com.yinfeng.yinfengtrajectory;


import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusUtils;


public class SampleDeviceReceiver extends DeviceAdminReceiver {


    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        new EventBusUtils().send(1);
//        Toast.makeText(context, "激活ok====", Toast.LENGTH_SHORT).show();
//
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        new EventBusUtils().send(2);
//        Toast.makeText(context, "取消激活====", Toast.LENGTH_SHORT).show();

    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub
        new EventBusUtils().send(2);
        return context.getString(R.string.disable_warning);
    }

}