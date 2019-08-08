package com.yinfeng.yf_trajectory.moudle.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.huawei.android.app.admin.DeviceApplicationManager;
import com.huawei.android.app.admin.DeviceControlManager;
import com.huawei.android.app.admin.DeviceHwSystemManager;
import com.huawei.android.app.admin.DevicePackageManager;
import com.huawei.android.app.admin.DeviceRestrictionManager;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.mdm.MDMUtils;
import com.yinfeng.yf_trajectory.mdm.SampleDeviceReceiver;
import com.yinfeng.yf_trajectory.mdm.SampleEula;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusBean;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusUtils;
import com.yinfeng.yf_trajectory.moudle.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusBuilder;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    /**
     * Hello World!
     */
    private TextView mActivityMainDisplayTxt;

    /**
     * 安装apk
     */
    private Button mActivityMainInstallApk;

    private DevicePolicyManager mDevicePolicyManager = null;
    private ComponentName mAdminName = null;
    private SampleEula sampleEula = null;
    /**
     * 启用wifi
     */
    private Button mActivityMainWifiStart;
    /**
     * 禁用wifi
     */
    private Button mActivityMainWifiStop;
    /**
     * 检测wifi功能
     */
    private Button mActivityMainWifiIsCheck;
    /**
     * 安装apk
     */
    private Button mActivityMainUninstallApk;
    /**
     * 开启禁止卸载
     */
    private Button mActivityMainOpenUninstall;
    /**
     * 关闭禁止卸载
     */
    private Button mActivityMainCloseUninstall;
    private MDMUtils mdmUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new EventBusUtils().register(this);

        initView();
    }

    private void initView() {
        mActivityMainDisplayTxt = (TextView) findViewById(R.id.activity_main_display_txt);
        mActivityMainDisplayTxt.setText("apk path：" + mdmUtils.mPath + '\n' + AppUtils.getAppVersionCode());
        mActivityMainDisplayTxt.setOnClickListener(this);
        mActivityMainInstallApk = (Button) findViewById(R.id.activity_main_install_apk);
        mActivityMainInstallApk.setOnClickListener(this);
        mActivityMainWifiStart = (Button) findViewById(R.id.activity_main_wifi_start);
        mActivityMainWifiStart.setOnClickListener(this);
        mActivityMainWifiStop = (Button) findViewById(R.id.activity_main_wifi_stop);
        mActivityMainWifiStop.setOnClickListener(this);
        mActivityMainWifiIsCheck = (Button) findViewById(R.id.activity_main_wifi_is_check);
        mActivityMainWifiIsCheck.setOnClickListener(this);
        mActivityMainUninstallApk = (Button) findViewById(R.id.activity_main_uninstall_apk);
        mActivityMainUninstallApk.setOnClickListener(this);
        mActivityMainOpenUninstall = (Button) findViewById(R.id.activity_main_open_uninstall);
        mActivityMainOpenUninstall.setOnClickListener(this);
        mActivityMainCloseUninstall = (Button) findViewById(R.id.activity_main_close_uninstall);
        mActivityMainCloseUninstall.setOnClickListener(this);
//        AppOpsManagerEx appManager = new AppOpsManagerEx();
//        try {
//            appManager.setMode(AppOpsManagerEx.TYPE_OPEN_BLUETOOTH, "com.yinfeng.yinfengtrajectory", AppOpsManagerEx.MODE_ALLOWED);
//        } catch (RemoteException e) {
//            // TODO Auto-generated catch block
//            Toast.makeText(this, "AppOpsManagerEx", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }

        mdmUtils = new MDMUtils();

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, SampleDeviceReceiver.class);

        sampleEula = new SampleEula(this, mDevicePolicyManager, mAdminName);
        sampleEula.activeProcessApp();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_main_display_txt:
                break;
            case R.id.activity_main_uninstall_apk:
                mdmUtils.installApk(false);
                break;
            case R.id.activity_main_install_apk:
                mdmUtils.installApk(true);
                break;
            case R.id.activity_main_wifi_start:
                mdmUtils.setWifiDisabled(false);
                break;
            case R.id.activity_main_wifi_stop:
                mdmUtils.setWifiDisabled(true);
                break;
            case R.id.activity_main_wifi_is_check:
                Toast.makeText(this, "wifi 功能 ：" + !mdmUtils.isWifiDisabled(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.activity_main_open_uninstall:
                mdmUtils.setDisallowedUninstallPackages(true);
                break;
            case R.id.activity_main_close_uninstall:
                mdmUtils.setDisallowedUninstallPackages(false);
                break;
        }
    }

    //定义处理接收方法
    @Subscribe
    public void onEventMainThread(EventBusBean event) {
        Toast.makeText(this, "" + event.getType(), Toast.LENGTH_SHORT).show();
        if (event.getType() == 1) {//激活
            //强制开启数据服务
            mdmUtils.forceMobiledataOn();
            //保持某应用始终运行
            mdmUtils.addPersistentApp();
            //设置应用为信任应用
            //setSuperWhiteListForHwSystemManger();
            //开启禁止卸载
            mdmUtils.setDisallowedUninstallPackages(true);


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
        super.onDestroy();
        new EventBusUtils().unregister(this);

    }
}
