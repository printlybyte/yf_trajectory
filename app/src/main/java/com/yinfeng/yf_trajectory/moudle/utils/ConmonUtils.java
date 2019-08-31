package com.yinfeng.yf_trajectory.moudle.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.caitiaobang.core.app.app.Latte;

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



    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */

    public static boolean hasSimCard() {
        TelephonyManager telMgr = (TelephonyManager)
                Latte.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        Log.d("try", result ? "有SIM卡" : "无SIM卡");
        return result;
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
