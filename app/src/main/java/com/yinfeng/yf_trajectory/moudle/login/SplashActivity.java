package com.yinfeng.yf_trajectory.moudle.login;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.storge.LattePreference;
import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MToast;
import com.orhanobut.hawk.Hawk;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.mdm.MDMUtils;
import com.yinfeng.yf_trajectory.mdm.SampleDeviceReceiver;
import com.yinfeng.yf_trajectory.mdm.SampleEula;
import com.yinfeng.yf_trajectory.moudle.activity.MainActivity;
import com.yinfeng.yf_trajectory.moudle.activity.MapActivity;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusBean;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusUtils;
import com.yinfeng.yf_trajectory.moudle.utils.SMSCore;

import org.greenrobot.eventbus.Subscribe;

public class SplashActivity extends AppCompatActivity {
    private MDMUtils mdmUtils = null;
    private DevicePolicyManager mDevicePolicyManager = null;
    private ComponentName mAdminName = null;
    private SampleEula sampleEula = null;

    /**
     * 初始化相关组件
     */
    private void initHuaWeiHDM() {
        mdmUtils = new MDMUtils();
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, SampleDeviceReceiver.class);
        sampleEula = new SampleEula(this, mDevicePolicyManager, mAdminName);
        sampleEula.activeProcessApp();

//        initNetworkConnect();

    }

    /**
     * 订阅接受者
     */
    @Subscribe
    public void onEventMainThread(EventBusBean event) {
        Toast.makeText(this, "" + event.getType(), Toast.LENGTH_SHORT).show();
        if (event.getType() == 1) { //激活
            //禁止关闭GPS
            boolean isActive = mdmUtils.setLocationModeDisabled(true);

            if (!isActive) {
                Toast.makeText(this, "此手机无权限", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            //禁用隐私空间
            mdmUtils.setAddUserDisabled(true);

            //禁止反激活
            mdmUtils.addDisabledDeactivateMdmPackages();
            //强制开启数据服务
            mdmUtils.forceMobiledataOn();
            //保持某应用始终运行
            mdmUtils.addPersistentApp();
//            //设置应用为信任应用
            mdmUtils.setSuperWhiteListForHwSystemManger();
//            //开启禁止卸载
            mdmUtils.setDisallowedUninstallPackages(true);
//            //允许/禁止定位服务设置（EMUI8.0）
            mdmUtils.setLocationServiceDisabled(false);
            //设置一级菜单 //禁用应用查看一级菜单
            mdmUtils.setCustomSettingsMenu();
            //禁止系统升级
            mdmUtils.setSystemUpdateDisabled(true);
            //禁止应用通知消息
//            mdmUtils.setNotificationDisabled(true);
            //禁止出厂设置
            mdmUtils.setRestoreFactoryDisabled(true);
            //禁用搜索
            mdmUtils.setSearchIndexDisabled(true);



            ActivityUtils.startActivity(SMSActivity.class);
            Hawk.put(ConstantApi.isActivation, "1");
            finish();
        } else if (event.getType() == 2) { //取消
            if (mAdminName != null && mAdminName != null) {
                sampleEula.activeProcessApp();
            } else {
                Toast.makeText(this, "mAdminName= null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        new EventBusUtils().unregister(this);
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new EventBusUtils().register(this);

        String isLoginStatus = Hawk.get(ConstantApi.isActivation, "");
        if (TextUtils.isEmpty(isLoginStatus)) {
            initHuaWeiHDM();
        } else {
            ActivityUtils.startActivity(SMSActivity.class);
            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            finish();
        }
//        Toast.makeText(this, resultCode + "", Toast.LENGTH_SHORT).show();

    }


}
