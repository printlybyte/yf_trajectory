package com.yinfeng.yf_trajectory.moudle.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.caitiaobang.core.app.app.Latte;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean;
import com.yinfeng.yf_trajectory.moudle.bean.expanded.ApplicationRecordActivityBean;
import com.yinfeng.yf_trajectory.moudle.login.ICCIDActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：PermissionUtilsx
 * 创建人：liuguodong
 * 创建时间：2019/9/9 18:20
 * ============================================
 **/
public class PermissionUtilsx {


    /**
     * 获取android系统版本
     */
    public static int  getSystemVersion() {
        Class<?> classType = null;
        String buildVersion = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            buildVersion = (String) getMethod.invoke(classType, new Object[]{"ro.build.version.emui"});

            buildVersion=  buildVersion.toString().replace("EmotionUI_","").toString().replace(".","");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.i("getSystemVersion: " + Float.parseFloat(buildVersion));
        return Integer.parseInt(buildVersion);
    }

    /**
     * 检测存储权限6.0之前
     */
    public static boolean is_STORAGE() {

        if (getSystemVersion() >= 1000) {
            return true;
        }


        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/1.txt";
        try {
            boolean isCreateStatus = FileUtils.createFileByDeleteOldFile(path);
            Logger.i("检测存储权限 : 结果" + isCreateStatus);
            return isCreateStatus;
        } catch (Exception e) {
            Logger.i("检测存储权限 : 异常" + e.getMessage());
            return false;
        }

    }

    /**
     * 检测电话权限6.0之前
     */
    public static boolean is_PHONE_STATUS() {


        if (getSystemVersion() >= 1000) {
            return true;
        }


        TelephonyManager tm = (TelephonyManager) Latte.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String iccid = tm.getSimSerialNumber();
            String imei = PhoneUtils.getIMEI();
            Logger.i("检测电话权限 : " + iccid + "  imei:" + imei);
            if (iccid == null || TextUtils.isEmpty(iccid) || imei == null || TextUtils.isEmpty(imei)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Logger.i("检测电话权限 : 异常 " + e.getMessage());
            return false;
        }
    }


    /**
     * 检测电话权限6.0之前
     */
//    public static boolean is_LOCATION() {
//
//
//        if (getSystemVersion() >= 1000) {
//            return true;
//        }
//
//
//        try {
//            TelephonyManager mTelephonyManager = (TelephonyManager) Latte.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//            List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();
//            Logger.i("检测定位权限 : 正常 ");
//            if (infos == null) {
//                return false;
//            } else {
//                return true;
//            }
//        } catch (Exception e) {
//            Logger.i("检测定位权限 : 异常" + e.getMessage());
//            return false;
//        }
//    }


    public static boolean checkP() {
        if (!PermissionUtilsx.is_PHONE_STATUS()) {
            checkNet("银丰轨迹权限-电话权限已关闭，请及时打开", 9);
            Logger.i("银丰轨迹权限-电话权限已关闭，请及时打开");
            requestDate("电话权限已关闭");
            return false;
        }
        if (!PermissionUtilsx.is_STORAGE()) {
            checkNet("银丰轨迹权限-存储权限已关闭，请及时打开", 9);
            Logger.i("银丰轨迹权限-存储权限已关闭，请及时打开");
            requestDate("存储权限已关闭");
            return false;
        }
//        if (!PermissionUtilsx.is_LOCATION()) {
//            checkNet("银丰轨迹权限-位置权限已关闭，请及时打开", 9);
//            Logger.i("权限已关闭，请及时打开");
//            requestDate("位置权限已关闭");
//            return false;
//        }

//        if (!NetworkUtils.isConnected()) {
//          Log.i("testre","网络无连接，请检测网络是否可以正常上网");
//            checkNet("网络无连接，请检测网络是否可以正常上网", 1);
//            return false;
//        }
        return true;
    }

    public static void checkNet(String msg, int type) {
        try {
            NotificationManagerUtils.startNotificationManager(msg, R.mipmap.ic_app_start_icon);
        } catch (Exception e) {
        }
    }


    static void requestDate(String remark) {
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            Logger.i("记录用户操作关闭 权限 token = null ");
            return;
        }
        String times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("startTime", times);
        map.put("endTime", times);
        map.put("matter", "关权限");
        map.put("remark", remark);
//        map.put("endTime", mTimeEnd);
        String mNetUrl = Api.commonrecordOperate;
        Logger.i("API: " + mNetUrl + "发送json：" + new Gson().toJson(map));
        OkHttpUtils
                .postString()
                .addHeader("track-token", token)
                .content(new Gson().toJson(map))
                .url(mNetUrl)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<ConmonBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("记录用户关闭权限 异常" + e.getMessage());
                    }

                    @Override
                    public void onResponse(ConmonBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            Logger.i("记录用户关闭权限 " + response.getMessage());
                        } else {
                            Logger.i("记录用户关闭权限 " + response.getMessage());
                        }
                        Logger.i("请求结果：记录用户关闭权限" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }
}
