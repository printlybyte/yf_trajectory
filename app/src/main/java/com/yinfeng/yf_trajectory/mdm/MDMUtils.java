package com.yinfeng.yf_trajectory.mdm;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.app.Latte;
import com.huawei.android.app.admin.DeviceApplicationManager;
import com.huawei.android.app.admin.DeviceControlManager;
import com.huawei.android.app.admin.DeviceHwSystemManager;
import com.huawei.android.app.admin.DevicePackageManager;
import com.huawei.android.app.admin.DeviceSettingsManager;
import com.huawei.android.app.admin.DeviceRestrictionManager;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.moudle.utils.FileUtils;

import java.security.Permissions;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.DONT_KILL_APP;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.mdm
 * 类  名：MDMUtils
 * 创建人：liuguodong
 * 创建时间：2019/8/6 14:29
 * ============================================
 **/
public class MDMUtils {

    private Context mContent = Latte.getApplicationContext();

    public MDMUtils() {

    }

    public String mComponentName = "com.yinfeng.yf_trajectory.mdm.SampleDeviceReceiver";
    public String mPath = ConstantApi.CommonApkPath + "1.apk";
    public String mPathHelp = ConstantApi.CommonApkPath + "2.apk";
    /**
     * 卸载的包名
     */
    String mInstallPackageName = "com.yinfeng.yf_trajectory_help";

    /**
     * @ isInstall true 安装   false 卸载   installType  track 轨迹    help  轨迹助手
     */
    public void installApk(boolean isInstall, String installType) {
        try {
            String pageName = mContent.getPackageName();//获取应用
            DevicePackageManager devicePackageManager = new DevicePackageManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            if (isInstall) {
                if (installType.equals("track")) {
                    if (!FileUtils.isFileExists(mPath)) {
                        Toast.makeText(mContent, "apk文件不存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    devicePackageManager.installPackage(componentName, mPath);
                } else if (installType.equals("help")) {
                    if (!FileUtils.isFileExists(mPathHelp)) {
                        Toast.makeText(mContent, "apk文件不存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    devicePackageManager.installPackage(componentName, mPathHelp);
                } else {
                    Toast.makeText(mContent, "==================", Toast.LENGTH_SHORT).show();
                }
            } else {
                devicePackageManager.uninstallPackage(componentName, mInstallPackageName, true);
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "此apk未经设备管理激活\n" +
                    "或此apk没有\n" +
                    "com.huawei.permission.sec.MDM_APP_MANAGEMENT权限\n" +
                    "或此apk不属于当前用户\n", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "admin为null或packageName为无效包名", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 1 添加阻止某应用被卸载名单（EMUI4.1） addDisallowedUninstallPackages
     */

    public void setDisallowedUninstallPackages(boolean isAdd) {
        try {
            String pageName = mContent.getPackageName();//获取应用
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
            Toast.makeText(mContent, "此apk未经设备管理激活\n" +
                    "或此apk没有\n" +
                    "com.huawei.permission.sec.MDM_APP_MANAGEMENT权限\n" +
                    "或此apk不属于当前用户\n", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "当admin为null或packageNames为null或empty或packageNames包含无效包名或packageNames中包名数量超过200时", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 设置wifi true 开启   false 关闭
     * 启用 WLAN 后，并不会同时打开 WLAN 功能，需要手动在设置或状态栏中打
     */
    public void setWifiDisabled(boolean isWifiEnable) {
        String pageName = mContent.getPackageName();//获取应用
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
    public boolean isWifiDisabled() {
        String pageName = mContent.getPackageName();//获取应用
        DeviceRestrictionManager deviceRestrictionManager = new DeviceRestrictionManager();
        ComponentName componentName = new ComponentName(pageName, mComponentName);
        return deviceRestrictionManager.isWifiDisabled(componentName);
    }


    /**
     * 强制开启数据
     */
    public void forceMobiledataOn() {
        String pageName = mContent.getPackageName();//获取应用
        DeviceControlManager deviceControlManager = new DeviceControlManager();
        ComponentName componentName = new ComponentName(pageName, mComponentName);
        try {
            deviceControlManager.forceMobiledataOn(componentName);
        } catch (SecurityException i) {
            Toast.makeText(mContent, "此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.permission.sec.MDM_CONNECTIVI\n" +
                    "TY权限。", Toast.LENGTH_SHORT).show();
            Log.i("testre", "SecurityException forceMobiledataOn======" + i.toString());
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

    public void addPersistentApp() {
        try {
            String pageName = mContent.getPackageName();
            DeviceApplicationManager deviceApplicationManager = new DeviceApplicationManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            List<String> mPackageList = new ArrayList<>();
            mPackageList.add(pageName);
            deviceApplicationManager.addPersistentApp(componentName, mPackageList);

        } catch (SecurityException e) {
            Toast.makeText(mContent, "此apk未经设备管理激活\n" +
                    "或此apk没有\n" +
                    "com.huawei.permission.sec.MDM_APP_MANAGEMENT权限\n" +
                    "或此apk不属于当前用户\n", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "当admin为null\n" +
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

    public void setSuperWhiteListForHwSystemManger() {
        try {
            String pageName = mContent.getPackageName();
            DeviceHwSystemManager deviceHwSystemManager = new DeviceHwSystemManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            ArrayList<String> mPackageList = new ArrayList<>();
            mPackageList.add(pageName);
            boolean isSuccess = deviceHwSystemManager.setSuperWhiteListForHwSystemManger(componentName, mPackageList);
            if (isSuccess) {
                Toast.makeText(mContent, "信任应用名单添加成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContent, "信任应用名单添加失败", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.ACCESS_INTERFACE\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "输入的包名为 null。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 允许/禁止定位服务设置（EMUI8.0）
     * 设置允许、禁止所有定位服务。
     * 注意：需要申请 com.huawei.permission.sec.MDM_SETTINGS_RESTRICTION 权限才能调用此接口；
     * 通过该接口指定禁用还是启用定位服务
     * 用来指定禁用还是启用定位服务，true表示禁用，false表示启用
     * 返回值：true：配置成功；
     * false：配置失败。
     */
    public void setLocationServiceDisabled(boolean isDisabled) {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.setLocationServiceDisabled(componentName, isDisabled);
            if (isSuccess) {
                Toast.makeText(mContent, "定位服务配置成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContent, "定位服务配置失败", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 2.15.34允许/禁止定位模式设置（EMUI8.0）
     * 注意：需要申请 com.huawei.permission.sec.MDM_SETTINGS_RESTRICTION 权限
     * 才能调用此接口；通过该接口指定禁用还是启用定位模式
     */
    public boolean setLocationModeDisabled(boolean isDisabled) {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.setLocationModeDisabled(componentName, isDisabled);
            if (isSuccess) {
                Toast.makeText(mContent, "定位模式设置成功", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(mContent, "定位模式设置失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 打开/关闭 GPS（EMUI5.0）
     * 9.0 之前版本：打开/关闭使用 GPS 的位置信息定位模式，需要申请
     * com.huawei.permission.sec.MDM_LOCATION 权限才可以调用此接口
     * 9.0 之后版本：不支持此接口，该接口弃用
     * true:打开GPS功能
     * false:关闭GPS功能
     */
    public void turnOnGPS(boolean isTurnOnGPS) {
        try {
            String pageName = mContent.getPackageName();//获取应用
            DeviceControlManager deviceControlManager = new DeviceControlManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            deviceControlManager.turnOnGPS(componentName, isTurnOnGPS);
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.permission.sec.MDM_LOCATION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数admin为null时", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取 GPS 状态（EMUI5.0）
     * true/false 已经开启GPS/未开启GPS 0.1.2
     */
    public int isGPSTurnOn() {
        try {
            String pageName = mContent.getPackageName();//获取应用
            DeviceControlManager deviceControlManager = new DeviceControlManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isGPSTurnOn = deviceControlManager.isGPSTurnOn(componentName);
            if (isGPSTurnOn) {
                return 1;
            } else {
                return 2;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.permission.sec.MDM_LOCATION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数admin为null时", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }


    /**
     * setCustomSettingsMenu
     * 设置订制菜单
     */
    public void setCustomSettingsMenu() {
        try {
            String pageName = mContent.getPackageName();//获取应用
            DeviceControlManager deviceControlManager = new DeviceControlManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            ArrayList<String> mMenuList = new ArrayList<>();
            mMenuList.add("com.android.settings.Settings$AppAndNotificationDashboardActivity");
            deviceControlManager.setCustomSettingsMenu(componentName, mMenuList);
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.permission.sec..MDM_DEVICE_MAN\n" +
                    "AGER\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数admin为null时", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 禁止/允许应用通知消息（EMUI8.1）
     * setNotificationDisabled
     * true：禁用应用通知消息 isDisabled
     * false：启用应用通知消息 isDisabled
     * <p>
     * true：应用通知消息功能被禁用；false：应用通知消息功能未禁用
     */

    public boolean setNotificationDisabled(boolean isDisabled) {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.setNotificationDisabled(componentName, isDisabled);
            if (isSuccess) {
//                Toast.makeText(mContent, "应用通知消息功能被禁用", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(mContent, "应用通知消息功能未禁用", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 查询所有系统/三方应用通知消息禁用状态
     * true：应用通知已被禁用
     * false：应用通知未被禁用
     */
    public boolean isNotificationDisabled() {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.isNotificationDisabled(componentName);
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /**
     * 禁禁用/启用恢复出厂设置(EMUI5.1)
     * setRestoreFactoryDisabled
     * true：禁用恢复出厂设置false：恢复正常状态。
     * <p>
     * true：配置成功；false：配置失败。
     */

    public boolean setRestoreFactoryDisabled(boolean isDisabled) {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.setRestoreFactoryDisabled(componentName, isDisabled);
            if (isSuccess) {
                Toast.makeText(mContent, "出厂设置配置成功", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(mContent, "出厂设置配置失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /**
     * 系统升级 setSystemUpdateDisabled
     * 禁用/启用系统升级功能(EMUI5.1)
     * true：禁用系统升级功能。false：启用系统升级功能。 isDisabled
     * <p>
     * true：配置成功；false：配置失败。
     */
    public boolean setSystemUpdateDisabled(boolean isDisabled) {
        try {
            String pageName = mContent.getPackageName();//获取应用
            DeviceRestrictionManager deviceRestrictionManager = new DeviceRestrictionManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceRestrictionManager.setSystemUpdateDisabled(componentName, isDisabled);
            if (isSuccess) {
                Toast.makeText(mContent, "禁用系统升级成功", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(mContent, "禁用系统升级失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_UPDATESTATE_MANAGER\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    /**
     * 2.15.44禁止/允许设置搜索（EMUI8.2）
     * setSearchIndexDisabled
     * 版权所有©华为技术有限公司 第 279 页, 共 298 页禁用后设置搜索工具条（
     * 包含语音搜索按钮）不可见，启用后恢复正常，在桌面搜索中，无法搜索出设置条目
     * <p>
     * true：已禁用设置搜索；false：未禁用设置搜索；
     * return：true：设置搜索已禁用
     * false：设置搜索未禁用
     */


    public boolean setSearchIndexDisabled(boolean isDisabled) {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.setSearchIndexDisabled(componentName, isDisabled);
            if (isSuccess) {
                Toast.makeText(mContent, "设置搜索已禁用", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(mContent, "设置搜索未禁用", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /**
     * 1 添加禁止反激活名单（EMUI4.1）
     */

    public void addDisabledDeactivateMdmPackages() {
        try {
            String pageName = mContent.getPackageName();  //获取应用
            DevicePackageManager devicePackageManager = new DevicePackageManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            List<String> mPackageList = new ArrayList<>();
            mPackageList.add(pageName);
            devicePackageManager.addDisabledDeactivateMdmPackages(componentName, mPackageList);
        } catch (SecurityException e) {
            Toast.makeText(mContent, "此apk未经设备管理激活\n" +
                    "或此apk没有\n" +
                    "此 apk 没有\n" +
                    "com.huawei.permission.sec.MDM_APP_MANAGEMENT \n" +
                    "权限。\n" +
                    "或此apk不属于当前用户\n", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 1 移除禁止反激活名单（EMUI4.1）
     */

    public void removeDisabledDeactivateMdmPackages() {
        try {
            String pageName = mContent.getPackageName();  //获取应用
            DevicePackageManager devicePackageManager = new DevicePackageManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            List<String> mPackageList = new ArrayList<>();
            mPackageList.add(pageName);
            devicePackageManager.removeDisabledDeactivateMdmPackages(componentName, mPackageList);
        } catch (SecurityException e) {
            Toast.makeText(mContent, "此apk未经设备管理激活\n" +
                    "或此apk没有\n" +
                    "此 apk 没有\n" +
                    "com.huawei.permission.sec.MDM_APP_MANAGEMENT \n" +
                    "权限。\n" +
                    "或此apk不属于当前用户\n", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 禁用/启用添加多用户(EMUI5.1)
     * 设置多用户添加禁用状态。禁用时，“添加用户”、“添加访客”、“添加隐私空间”功能
     * 无法使用，“允许设备锁定下添加用户”开关关闭且无法被开启。
     * 注意：需要申请 com.huawei.permission.sec.MDM_SETTINGS_RESTRICTION 权限
     * 才能调用此接口。
     * <p>
     * true：禁用添加多用户；
     * false：恢复正常状态。
     * <p>
     * true：配置成功；
     * false：配置失败。
     */


    public boolean setAddUserDisabled(boolean isDisabled) {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.setAddUserDisabled(componentName, isDisabled);
            if (isSuccess) {
//                Toast.makeText(mContent, "配置成功AddUser", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(mContent, "配置失败AddUser", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "此 apk 没有\n" +
                    "com.huawei.permission.sec.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean isAddUserDisabled( ) {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.isAddUserDisabled(componentName );
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "此 apk 没有\n" +
                    "com.huawei.permission.sec.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public void setPackageManager() {
        String ComponentNamePermissionsName =
                "com.android.packageinstaller.permission.ui.ManagePermissionsActivity";
        try {
            String pageName = "com.android.packageinstaller";
            PackageManager packageManager = Latte.getApplicationContext().getPackageManager();
            ComponentName componentName = new ComponentName(pageName, ComponentNamePermissionsName);
            packageManager.setComponentEnabledSetting(componentName, COMPONENT_ENABLED_STATE_DISABLED, DONT_KILL_APP);
        } catch (SecurityException e) {

            Toast.makeText(mContent, " setPackageManager err" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "setPackageManager err Exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 禁止修改系统时间
     * true：禁用更改系统日期和时间设置策略激活；
     * false：禁用更改系统日期和时间设置策略未激活。
     */
    public boolean setTimeAndDateSetDisabled(boolean isDisabled) {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.setTimeAndDateSetDisabled(componentName, isDisabled);
            if (isSuccess) {
                Toast.makeText(mContent, "时间设置成功", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(mContent, "时间设置失败", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /**
     * 获取禁用更改系统日期和时间设置策略状态
     * true：禁用更改系统日期和时间设置策略激活；
     * false：禁用更改系统日期和时间设置策略未激活。
     */
    public boolean isTimeAndDateSetDisabled() {
        try {
            String pageName = mContent.getPackageName();
            DeviceSettingsManager deviceSettingsManager = new DeviceSettingsManager();
            ComponentName componentName = new ComponentName(pageName, mComponentName);
            boolean isSuccess = deviceSettingsManager.isTimeAndDateSetDisabled(componentName);
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (SecurityException e) {
            Toast.makeText(mContent, "\uF06C 此 apk 未经设备管理激活。\n" +
                    "\uF06C 此 apk 没有\n" +
                    "com.huawei.systemmanager.permission.MDM_SETTINGS_RESTRICTION\n" +
                    "权限。", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContent, "参数 admin 为 null。", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
