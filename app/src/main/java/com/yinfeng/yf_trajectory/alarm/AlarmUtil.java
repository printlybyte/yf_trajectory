package com.yinfeng.yf_trajectory.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * ============================================
 * 描  述：
 * 包  名：com.lgd.test_app
 * 类  名：AlarmUtil
 * 创建人：liuguodong
 * 创建时间：2020/2/19 11:48
 * ============================================
 **/
public class AlarmUtil {
    private static final String TAG = "AlarmUtil";


    public static void controlAlarm(Context context, int startTimeHour, int startTimeMinute, long matchId, Intent nextIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) matchId, nextIntent,
                PendingIntent.FLAG_ONE_SHOT);

//        alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, pendingIntent);

        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, startTimeHour);
        calendar.set(Calendar.MINUTE, startTimeMinute);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                IntentConst.Time.OnceDay,
                pendingIntent);
    }

    public static void cancelAlarm(Context context, String action, long matchId) {
        Intent intent = new Intent(action);
        PendingIntent sender = PendingIntent.getBroadcast(
                context, (int) matchId, intent, 0);
        // And cancel the alarm.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }


}
