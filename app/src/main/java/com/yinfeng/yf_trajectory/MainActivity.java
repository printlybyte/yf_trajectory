package com.yinfeng.yf_trajectory;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.huawei.android.app.admin.DeviceApplicationManager;
import com.huawei.android.app.admin.DeviceControlManager;
import com.huawei.android.app.admin.DeviceHwSystemManager;
import com.huawei.android.app.admin.DevicePackageManager;
import com.huawei.android.app.admin.DeviceRestrictionManager;
import com.yinfeng.yf_trajectory.mdm.SampleDeviceReceiver;
import com.yinfeng.yf_trajectory.mdm.SampleEula;
import com.yinfeng.yf_trajectory.utils.FileUtils;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mActivityMainDisplayTxt = (TextView) findViewById(R.id.activity_main_display_txt);
        mActivityMainDisplayTxt.setText("apk path：" + mPath + '\n' + AppUtils.getAppVersionCode());
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


        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, SampleDeviceReceiver.class);

        SampleEula sampleEula = new SampleEula(this, mDevicePolicyManager, mAdminName);
        sampleEula.activeProcessApp();

    }





    String mComponentName = "com.yinfeng.yf_trajectory.mdm.SampleDeviceReceiver";
    String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yinfeng_apk/yfpz.apk";
    /**
     * 卸载的包名
     */
    String mInstallPackageName = "com.yinfeng.wypzh";

    /**
     * @ isInstall true 安装   false 卸载
     */
    private void installApk(boolean isInstall) {
        try {
            String pageName = getPackageName();//获取应用
            DevicePackageManager devicePackageManager = new DevicePackageManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            if (isInstall) {
                if (!FileUtils.isFileExists(mPath)) {
                    Toast.makeText(this, "apk文件不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                devicePackageManager.installPackage(componentName, mPath);
            } else {
                devicePackageManager.uninstallPackage(componentName, mInstallPackageName, true);
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "此apk未经设备管理激活\n" +
                    "或此apk没有\n" +
                    "com.huawei.permission.sec.MDM_APP_MANAGEMENT权限\n" +
                    "或此apk不属于当前用户\n", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "admin为null或packageName为无效包名", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 1 添加阻止某应用被卸载名单（EMUI4.1） addDisallowedUninstallPackages
     */

    private void setDisallowedUninstallPackages(boolean isAdd) {
        try {
            String pageName = getPackageName();//获取应用
            DevicePackageManager devicePackageManager = new DevicePackageManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            List<String> mPackageList = new ArrayList<>();
            mPackageList.add(pageName);
            if (isAdd) {
                devicePackageManager.addDisallowedUninstallPackages(componentName, mPackageList);
            } else {
                devicePackageManager.removeDisallowedUninstallPackages(componentName, mPackageList);
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "此apk未经设备管理激活\n" +
                    "或此apk没有\n" +
                    "com.huawei.permission.sec.MDM_APP_MANAGEMENT权限\n" +
                    "或此apk不属于当前用户\n", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "当admin为null或packageNames为null或empty或packageNames包含无效包名或packageNames中包名数量超过200时", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 设置wifi true 开启   false 关闭
     * 启用 WLAN 后，并不会同时打开 WLAN 功能，需要手动在设置或状态栏中打
     */
    private void setWifiDisabled(boolean isWifiEnable) {
        String pageName = getPackageName();//获取应用
        DeviceRestrictionManager deviceRestrictionManager = new DeviceRestrictionManager();
        ComponentName componentName = new ComponentName(pageName, mComponentName);
        if (isWifiEnable) {
            deviceRestrictionManager.setWifiDisabled(componentName, true);
        } else {
            deviceRestrictionManager.setWifiDisabled(componentName, false);
        }
    }

    /**
     * 查询 WLAN 是否被禁用（EMUI4.1）
     * true/false WLAN功能被禁止/未被禁止
     */
    private boolean isWifiDisabled() {
        String pageName = getPackageName();//获取应用
        DeviceRestrictionManager deviceRestrictionManager = new DeviceRestrictionManager();
        ComponentName componentName = new ComponentName(pageName, mComponentName);
        return deviceRestrictionManager.isWifiDisabled(componentName);
    }


    /**
     * 强制开启数据
     */
    private void forceMobiledataOn() {
        String pageName = getPackageName();//获取应用
        DeviceControlManager deviceControlManager = new DeviceControlManager();
        ComponentName componentName = new ComponentName(pageName, mComponentName);
        try {
            deviceControlManager.forceMobiledataOn(componentName);
        } catch (SecurityException i) {
            Toast.makeText(this, "此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.permission.sec.MDM_CONNECTIVI\n" +
                    "TY权限。", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 添加保持某应用始终运行名单（EMUI4.1） addPersistentApp
     * 注意：
     * 1）当前 apk 设置 packageNames 累计数量总和不能超过 3，否则抛出
     * IllegalArgumentException 异常；
     * 2）packageNames 不能包含系统应用包名
     * 本接口对应开发指导书“保持某应用始终运行”功能，添加后，
     * 保活单中的应用不能被任何方式停止运行。Android DOZE模式和自启动、关联启动不在此范围内。
     */

    private void addPersistentApp() {
        try {
            String pageName = getPackageName();
            DeviceApplicationManager deviceApplicationManager = new DeviceApplicationManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            List<String> mPackageList = new ArrayList<>();
            mPackageList.add(pageName);
            deviceApplicationManager.addPersistentApp(componentName, mPackageList);

        } catch (SecurityException e) {
            Toast.makeText(this, "此apk未经设备管理激活\n" +
                    "或此apk没有\n" +
                    "com.huawei.permission.sec.MDM_APP_MANAGEMENT权限\n" +
                    "或此apk不属于当前用户\n", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "当admin为null\n" +
                    "或packageNames为null或empty", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * setSuperWhiteListForHwSystemManger
     * 设置应用为信任应用，允许应用自启动、关联启动，
     * 默认开启忽略电池优化，允许后台运行，从而达到保活目的
     * 设置可信任应用列表。
     *  1、默认允许显示该应用的通知，用户不可修改；
     *  2、禁止用户修改 APK 的联网权限（包括数据业务和 WLAN）；*
     *  3、设置应用为信任应用，允许开启悬浮窗，所申请权限不需要用户确认，禁止用户取消/修改 APK 的相关权限*
     *  4、接口不具备保活功能
     *  5、默认允许开机自启；允许关联启动；用户不可修改*
     *  6、添加白名单后将不允许电池优化*
     * 注意：
     * 1. 需要申请 com.huawei.systemmanager.permission.ACCESS_INTERFACE 权
     * 限才能调用此接口。
     * 2. 功能 2,3 从 EMUI9.0.1 开始支持
     * 3. 功能 5,6 从 EMUI9.0 开始支持
     */

    private void setSuperWhiteListForHwSystemManger() {
        try {
            String pageName = getPackageName();
            DeviceHwSystemManager deviceHwSystemManager = new DeviceHwSystemManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            ArrayList<String> mPackageList = new ArrayList<>();
            mPackageList.add(pageName);
            boolean isSuccess = deviceHwSystemManager.setSuperWhiteListForHwSystemManger(componentName, mPackageList);
            if (isSuccess) {
                Toast.makeText(this, "信任应用名单添加成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "信任应用名单添加失败", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.ACCESS_INTERFACE\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "输入的包名为 null。", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_main_display_txt:
                break;
            case R.id.activity_main_uninstall_apk:
                installApk(false);
                break;
            case R.id.activity_main_install_apk:
                installApk(true);
                break;
            case R.id.activity_main_wifi_start:
                setWifiDisabled(false);
                break;
            case R.id.activity_main_wifi_stop:
                setWifiDisabled(true);
                break;
            case R.id.activity_main_wifi_is_check:
                //                    强制开启数据服务
                forceMobiledataOn();
                //保持某应用始终运行
                addPersistentApp();
                //设置应用为信任应用
//                    setSuperWhiteListForHwSystemManger();

                Toast.makeText(this, "wifi 功能 ：" + !isWifiDisabled(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.activity_main_open_uninstall:
                setDisallowedUninstallPackages(true);
                break;
            case R.id.activity_main_close_uninstall:
                setDisallowedUninstallPackages(false);
                break;
        }
    }




}
