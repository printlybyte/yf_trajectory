package com.caitiaobang.core.app.utils;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ============================================
 * 描  述：
 * 包  名：com.caitiaobang.core.app.utils
 * 类  名：ConmonUtils
 * 创建人：liuguodong
 * 创建时间：2019/8/5 14:03
 * ============================================
 **/
public class ConmonUtils {
    private ConmonUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //时间戳转字符串
    public static String LongTime2StingAll(long timestamp, String timeType) {
        SimpleDateFormat format = new SimpleDateFormat(timeType); //设置格式
        String timeText = format.format(timestamp * 1000);                                //获得带格式的字符串
        return timeText;
    }


    public static long StringDateToStamp(String timestamp, String timeType) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeType);
        Date date = simpleDateFormat.parse(timestamp);
        long ts = date.getTime() / 1000;
        return ts;

    }

    /**
     * 由过去的某一时间,计算距离当前的时间
     */
    public static String CalculateTime(String time) {
        long nowTime = System.currentTimeMillis(); // 获取当前时间的毫秒数
        String msg = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 指定时间格式
        Date setTime = null; // 指定时间
        try {
            setTime = sdf.parse(time); // 将字符串转换为指定的时间格式
        } catch (ParseException e) {

            e.printStackTrace();
        }
        long reset = setTime.getTime(); // 获取指定时间的毫秒数
        long dateDiff = nowTime - reset;
        if (dateDiff < 0) {
            msg = "输入的时间不对";
        } else {
            long dateTemp1 = dateDiff / 1000; // 秒
            long dateTemp2 = dateTemp1 / 60; // 分钟
            long dateTemp3 = dateTemp2 / 60; // 小时
            long dateTemp4 = dateTemp3 / 24; // 天数
            long dateTemp5 = dateTemp4 / 30; // 月数
            long dateTemp6 = dateTemp5 / 12; // 年数
            if (dateTemp6 > 0) {
                msg = dateTemp6 + "年前";
            } else if (dateTemp5 > 0) {
                msg = dateTemp5 + "个月前";
            } else if (dateTemp4 > 0) {
                msg = dateTemp4 + "天前";
            } else if (dateTemp3 > 0) {
                msg = dateTemp3 + "小时前";
            } else if (dateTemp2 > 0) {
                msg = dateTemp2 + "分钟前";
            } else if (dateTemp1 > 0) {
                msg = "刚刚";
            }
        }
        return msg;
    }

    /**
     * 1 man 2 girl 果是奇数性别为男，偶数则为女。
     */
    public static int isSex(String idCard) {
        if (!TextUtils.isEmpty(idCard) && idCard.length() == 18) {
            if (Integer.parseInt(idCard.substring(16, 17)) %2==0 ) {
                return 2;
            } else {
                return 1;
            }
        }
        return 0;
    }


    public static String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(time);
        return sf.format(d);
    }

}
