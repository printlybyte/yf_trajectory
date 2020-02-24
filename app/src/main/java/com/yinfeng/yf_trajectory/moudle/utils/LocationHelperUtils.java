//package com.yinfeng.yf_trajectory.moudle.utils;
//
//import android.content.Intent;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import com.caitiaobang.core.app.app.Latte;
//import com.caitiaobang.core.app.storge.LattePreference;
//import com.maning.mndialoglibrary.MToast;
//import com.yinfeng.yf_trajectory.ConstantApi;
//
///**
// * ============================================
// * 描  述：
// * 包  名：com.caitiaobang.pro.activity.utils
// * 类  名：UserBeanUtils
// * 创建人：liuguodong
// * 创建时间：2019/6/30 12:19
// * ============================================
// **/
//
//
//public class LocationHelperUtils {
//
//
//    public static void checkPhone() {
//        String localPhone = LattePreference.getValue(ConstantApi.HK_PHONE);
//        String line1Nums = ConmonUtils.getLineNumber();
//        String line1Numsxxxxxx = "";
//        if (!TextUtils.isEmpty(localPhone) && !TextUtils.isEmpty(line1Nums)) {
//            if (line1Nums.length() == 14) {
//                line1Numsxxxxxx = line1Nums.substring(3, 14).toString();
//            } else if (line1Nums.length() == 11) {
//                line1Numsxxxxxx = line1Nums;
//            } else {
//                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
//                mIntent.putExtra("result", ConstantApi.RECEVIER_NO_SIM_READY);
//                //发送广播
//                Latte.getApplicationContext().sendBroadcast(mIntent);
//                return;
//            }
//            if (!localPhone.equals(line1Numsxxxxxx)) {
//                Toast.makeText(Latte.getApplicationContext(), "手机号已被更换", Toast.LENGTH_SHORT).show();
//                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
//                mIntent.putExtra("result", ConstantApi.RECEVIER_NO_SIM_READY);
//                //发送广播
//                Latte.getApplicationContext().sendBroadcast(mIntent);
//                return;
//            }
//        }
//    }
//
//}
