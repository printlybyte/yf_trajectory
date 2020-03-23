package com.yinfeng.yf_trajectory.moudle.utils;

import com.caitiaobang.core.app.app.Latte;
import com.maning.mndialoglibrary.MToast;

/**
 * ============================================
 * 描  述：
 * 包  名：com.caitiaobang.pro.activity.utils
 * 类  名：UserBeanUtils
 * 创建人：liuguodong
 * 创建时间：2019/6/30 12:19
 * ============================================
 **/


public class LocationErrUtils {
    private LocationErrUtils() {
    }

    private static class getInstance {
        private static LocationErrUtils INSTANCE = new LocationErrUtils();
    }

    public static LocationErrUtils getInstance() {
        return getInstance.INSTANCE;
    }

    public String showErr(int errCode) {
        String msg = "";
        switch (errCode) {
            case 1:
                msg = "context = null";
                break;
            case 2:
                msg = "定位失败，由于仅扫描到单个wifi，且没有基站信息。";
                break;
            case 3:
                msg = "获取到的请求参数为空，可能获取过程中出现异常。";
                break;
            case 4:
                msg = "请求服务器过程中的异常，多为网络情况差，链路不通导致";
                break;
            case 5:
                msg = "请求被恶意劫持，定位结果解析失败。";
                break;
            case 6:
                msg = "定位服务返回定位失败。";
                break;
            case 7:
                msg = "KEY鉴权失败。";
                break;
            case 8:
                msg = "Android exception常规错误";
                break;
            case 9:
                msg = "定位初始化时出现异常。";
                break;
            case 10:
                msg = "定位客户端启动失败。";
                break;
            case 11:
                msg = "定位时的基站信息错误。";
                break;
            case 12:
                msg = "缺少定位权限。";
                break;
            case 13:
                msg = "定位失败，由于未获得WIFI列表和基站信息，且GPS当前不可用。";
                break;
            case 14:
                msg = "GPS 定位失败，由于设备当前 GPS 状态差";
                break;
            case 15:
                msg = "定位结果被模拟导致定位失败";
                break;

            case 16:
                msg = "当前POI检索条件、行政区划检索条件下，无可用地理围栏";
                break;
            case 18:
                msg = "定位失败，由于手机WIFI功能被关闭同时设置为飞行模式";
                break;
            case 19:
                msg = "定位失败，由于手机没插sim卡且WIFI功能被关闭";
                break;

        }

        return msg;
//        MToast.makeTextShort(Latte.getApplicationContext(), msg);
    }


}
