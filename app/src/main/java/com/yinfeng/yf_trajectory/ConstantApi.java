package com.yinfeng.yf_trajectory;

import android.os.Environment;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yinfengtrajectory
 * 类  名：ConstantApi
 * 创建人：liuguodong
 * 创建时间：2019/8/1 16:55
 * ============================================
 **/
public interface ConstantApi {
    String LOG_ERR = "LOG_ERR";
    String LOG_I = "LOG_I";
    String LOG_I_ = "LOG_I_";
    String LOG_I_NET = "LOG_I_NET";
    String HAWK_LAT = "HAWK_LAT";
    String HAWK_LNG = "HAWK_LNG";


    int API_REQUEST_SUCCESS = 200;
    int API_REQUEST_ERR_901 = 901;


    // FINAL HK CONSTANS
    String HK_TOKEN = "HK_TOKEN";
    String HK_ROMOTE_TOKEN = "HK_ROMOTE_TOKEN";
    String HK_ICCID = "HK_ICCID";
    String HK_USER_BEAN = "HK_USER_BEAN";
    String INTENT_FLAG = "INTENT_FLAG";
    String INTENT_KEY = "INTENT_KEY";
    String INTENT_KEY_TWO = "INTENT_KEY_TWO";
    String INTENT_KEY_THREE = "INTENT_KEY_THREE";


    //搜索跳转
    String query_search = "query_search";
    //信息查看
    String query_info = "query_info";


    long time_dialog_min_time = Long.parseLong("1564647419000");
    long time_dialog_max_time = Long.parseLong("1880266619000");

    /**
     * mapactivity通用广播
     */
    String RECEIVER_ACTION = "location_in_background";

    String RECEVIER_901 = "RECEVIER_901";
    String RECEVIER_NO_SIM_READY = "RECEVIER_NO_SIM_READY";
    String RECEVIER_DOWNLOAD_APK = "RECEVIER_DOWNLOAD_APK";
    String RECEVIER_DOWNLOAD_HELP_APK = "RECEVIER_DOWNLOAD_HELP_APK";


    /**
     * playservice  检测应用升级的广播
     */
    //轨迹
    String RECEIVER_ACTION_DOWNLOAD_APK = "RECEIVER_ACTION_DOWNLOAD_APK";

    //轨迹助手
    String RECEIVER_ACTION_DOWNLOAD_HELP_APK = "RECEIVER_ACTION_DOWNLOAD_HELP_APK";
    String RECEIVER_ACTION_DOWNLOAD_ALIVE_HELP_APK = "RECEIVER_ACTION_DOWNLOAD_ALIVE_HELP_APK";

    String CommonApkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/yf_trajectory/";
    String CommonPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    String CommonApkName = "1.apk";
    String CommonHelpApkName = "2.apk";


    String isActivation = "isActivation";

//String EVENT_TAG_ACTION="EVENT_TAG_ACTION";
    /**
     * 标记外出报备的状态  0=无外出报备  1  有状态
     */
    String isMatterStatus = "isMatterStatus";


    String isWorkingDay = "isWorkingDay";


    String work_time_start = "work_time_start";
    String work_time_end = "work_time_end";


    String leave_time_start = "leave_time_start";
    String leave_time_end = "leave_time_end";
    String leave_time_status = "leave_time_status";


    String work_time_status = "work_time_status";


    /**
    * isWorkStatus==0 异常  ==1 状态开始   ==2 状态停止
    */
    String work_day_isLocation_status = "work_day_isLocation_status";
//    String overtime_status = "overtime_status";


    String service_action="service_action_event";
    String activity_action="activity_action_event";

}
