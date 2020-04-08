package com.yinfeng.yf_trajectory.moudle.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.storge.LattePreference;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.mdm.MDMUtils;
import com.yinfeng.yf_trajectory.mdm.SampleDeviceReceiver;
import com.yinfeng.yf_trajectory.mdm.SampleEula;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusBean;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;

//import com.yinfeng.yf_trajectory.moudle.login.LoginVerActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
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
    /**
     * 允许定位服务设置
     */
    private Button mActivityMainLocationEnabled;
    /**
     * 禁止定位服务设置
     */
    private Button mActivityMainLocationDisabled;
    /**
     * 查看数据库
     */
    private Button mActivityMainQueryDatebase;
    /**
     * 退出登录
     */
    private Button mActivityMainExitApp;
    /**
     * 关闭系统更新
     */
    private Button mActivityMainCloseUpdateSystem;
    /**
     * 关闭禁止恢复出厂设置
     */
    private Button mActivityMainCloseFactorySettings;
    /**
     * 开启应用搜索
     */
    private Button mActivityMainStartAppSearch;
    /**
     * 开启应用通知
     */
    private Button mActivityMainStartNotice;
    /**
     * 移除激活
     */
    private Button mActivityMainRemovePositionApp;
    /**
     * 开启时间设置
     */
    private Button mActivityMainStartTimeSetting;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    protected void initView() {
        super.initView();
        mdmUtils = new MDMUtils();

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, SampleDeviceReceiver.class);

        sampleEula = new SampleEula(this, mDevicePolicyManager, mAdminName);
        sampleEula.activeProcessApp();


        mActivityMainDisplayTxt = (TextView) findViewById(R.id.activity_main_display_txt);
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


        mActivityMainLocationEnabled = (Button) findViewById(R.id.activity_main_location_enabled);
        mActivityMainLocationEnabled.setOnClickListener(this);
        mActivityMainLocationDisabled = (Button) findViewById(R.id.activity_main_location_disabled);
        mActivityMainLocationDisabled.setOnClickListener(this);
        mActivityMainQueryDatebase = (Button) findViewById(R.id.activity_main_query_datebase);
        mActivityMainQueryDatebase.setOnClickListener(this);
        mActivityMainExitApp = (Button) findViewById(R.id.activity_main_exit_app);
        mActivityMainExitApp.setOnClickListener(this);
        mActivityMainCloseUpdateSystem = (Button) findViewById(R.id.activity_main_close_update_system);
        mActivityMainCloseUpdateSystem.setOnClickListener(this);
        mActivityMainCloseFactorySettings = (Button) findViewById(R.id.activity_main_close_factory_settings);
        mActivityMainCloseFactorySettings.setOnClickListener(this);
        mActivityMainStartAppSearch = (Button) findViewById(R.id.activity_main_start_app_search);
        mActivityMainStartAppSearch.setOnClickListener(this);
        mActivityMainStartNotice = (Button) findViewById(R.id.activity_main_start_notice);
        mActivityMainStartNotice.setOnClickListener(this);


        //获取手机号码
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = tm.getDeviceId();//获取智能设备唯一编号
        String te1 = tm.getLine1Number();//获取本机号码

        mActivityMainDisplayTxt.setText("apk path：" + mdmUtils.mPath + '\n' + AppUtils.getAppVersionCode() + '\n' + "手机号： " + te1);


        mActivityMainRemovePositionApp = (Button) findViewById(R.id.activity_main_remove_position_app);
        mActivityMainRemovePositionApp.setOnClickListener(this);
        mActivityMainStartTimeSetting = (Button) findViewById(R.id.activity_main_start_time_setting);
        mActivityMainStartTimeSetting.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_main_display_txt:
                break;
            case R.id.activity_main_uninstall_apk:
                showToastC("");
                mdmUtils.installApk(false, "track");
                break;
            case R.id.activity_main_install_apk:
                mdmUtils.installApk(true, "track");
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
            case R.id.activity_main_location_enabled:

                break;
            case R.id.activity_main_location_disabled:
                break;
            case R.id.activity_main_query_datebase:
                ActivityUtils.startActivity(QueryDateBaseActivity.class);
                break;
            case R.id.activity_main_exit_app:

                LattePreference.clear();
                AppManager.getInstance().finishAllActivity();
                finish();
//                ActivityUtils.startActivity(LoginVerActivity.class);

                break;
            case R.id.activity_main_close_update_system:
                mdmUtils.setSystemUpdateDisabled(false);
                break;
            case R.id.activity_main_close_factory_settings:
                mdmUtils.setRestoreFactoryDisabled(false);
                break;
            case R.id.activity_main_start_app_search:
                mdmUtils.setSearchIndexDisabled(false);

                break;
            case R.id.activity_main_start_notice:
                mdmUtils.setNotificationDisabled(false);

                break;
            case R.id.activity_main_remove_position_app:
                mdmUtils.removeDisabledDeactivateMdmPackages();
                break;
            case R.id.activity_main_start_time_setting:
                mdmUtils.setTimeAndDateSetDisabled(false);
                break;
        }
    }

    //定义处理接收方法
    @Subscribe
    public void onEventMainThread(EventBusBean event) {
        if (event.getType() == 1) {//激活
            //强制开启数据服务
//            mdmUtils.forceMobiledataOn();
            //保持某应用始终运行
            mdmUtils.addPersistentApp();
            //设置应用为信任应用
            mdmUtils.setSuperWhiteListForHwSystemManger();
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
