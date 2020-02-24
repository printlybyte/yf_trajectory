package com.yinfeng.yf_trajectory.moudle.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.caitiaobang.core.app.app.Latte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：ConmonUtils
 * 创建人：liuguodong
 * 创建时间：2019/8/16 18:03
 * ============================================
 **/
public final class ConmonUtils {


    public static int getHelpVersionCode() {
        PackageManager pckMan = Latte.getApplicationContext().getPackageManager();
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);
        for (PackageInfo pInfo : packageInfo) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("appimage", pInfo.applicationInfo.loadIcon(pckMan));
            item.put("packageName", pInfo.packageName);
            item.put("versionCode", pInfo.versionCode);
            item.put("versionName", pInfo.versionName);
            if (pInfo.packageName.equals("com.yinfeng.yf_trajectory_help")) {
                Log.i("TESTRE", "packageName: " + pInfo.packageName + pInfo.versionCode);
                return pInfo.versionCode;
            }
            item.put("appName", pInfo.applicationInfo.loadLabel(pckMan).toString());
            items.add(item);
        }
        return 0;
    }



    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */

    public static boolean hasSimCard() {
        TelephonyManager telMgr = (TelephonyManager)
                Latte.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String simSer = telMgr.getSimSerialNumber();
            if (simSer == null || simSer.equals("")) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private ConmonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getLineNumber() {
        TelephonyManager phoneMgr = (TelephonyManager) Latte.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String numbers = phoneMgr.getLine1Number();
        if (TextUtils.isEmpty(numbers) || numbers == null) {
            return "";
        } else {
            return numbers; //获取到手机号
        }
    }
}
