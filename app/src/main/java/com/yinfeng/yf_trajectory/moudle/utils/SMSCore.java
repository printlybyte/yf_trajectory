package com.yinfeng.yf_trajectory.moudle.utils;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：SMSCore
 * 创建人：liuguodong
 * 创建时间：2019/8/21 16:25
 * ============================================
 **/

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.view.View;
import android.view.View.OnClickListener;
import android.content.BroadcastReceiver;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.content.ContextWrapper;

public class SMSCore {

    public static String PhoneNumber="";
    //==============Get Phone Number ==============================

    //get the phone number from sms
    public static String GetPhoneNumberFromSMSText(String sms){

        List<String> list=GetNumberInString(sms);
        for(String str:list){
            if(str.length()==11)
                return str;
        }
        return "";
    }
    public static List<String> GetNumberInString(String str){
        List<String> list=new ArrayList<String>();
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(str);

        while (m.find()) {
            if (!"".equals(m.group()))
                list.add(m.group());
        }
        return list;
    }

    //===========================================================
    //
    //
    //=============Send SMS================
    public  void SendSMS(String number,String text,Context context){

        PendingIntent pi = PendingIntent.getActivity(context, 0,
                new Intent(context, context.getClass()), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, text, pi, null);

    }

    public void SendSMS2(String number,String text,Context context){
        String SENT = "sms_sent";
        String DELIVERED = "sms_delivered";
        PendingIntent sentPI = PendingIntent.getActivity(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getActivity(context, 0, new Intent(DELIVERED), 0);
//
//          //
//           registerReceiver(new BroadcastReceiver(){
//                  @Override
//                  public void onReceive(Context context, Intent intent){
//                      switch(getResultCode())
//                      {
//                          case Activity.RESULT_OK:
//                              Log.i("====>", "RESULT_OK");
//                              System.out.println("RESULT_OK");
//
//                              break;
//                          case Activity.RESULT_CANCELED:
//                              Log.i("=====>", "RESULT_CANCELED");
//                              break;
//                      }
//                  }
//              }, new IntentFilter(DELIVERED));
//            //

        SmsManager smsm = SmsManager.getDefault();
        smsm.sendTextMessage(number, null, text, sentPI, deliveredPI);
    }

    //=====================================
}