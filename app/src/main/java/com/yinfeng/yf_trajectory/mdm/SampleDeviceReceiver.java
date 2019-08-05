package com.yinfeng.yf_trajectory.mdm;//package com.yinfeng.yinfengtrajectory;


import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.yinfeng.yf_trajectory.MainActivity;
import com.yinfeng.yf_trajectory.R;


public class SampleDeviceReceiver extends DeviceAdminReceiver {


    @Override
    public void onEnabled(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Toast.makeText(context, "激活ok====", Toast.LENGTH_SHORT).show();
//
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        Toast.makeText(context, "取消激活====", Toast.LENGTH_SHORT).show();

    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // TODO Auto-generated method stub

        return context.getString(R.string.disable_warning);
    }

}