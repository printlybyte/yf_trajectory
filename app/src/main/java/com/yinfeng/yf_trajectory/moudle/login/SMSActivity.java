package com.yinfeng.yf_trajectory.moudle.login;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.app.BaseApplication;
import com.caitiaobang.core.app.bean.GreendaoLocationBean;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.caitiaobang.core.greendao.gen.DaoSession;
import com.google.gson.Gson;
import com.maning.mndialoglibrary.MProgressDialog;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.LocationService;
import com.yinfeng.yf_trajectory.LocationStatusManager;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.activity.MapActivity;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean;
import com.yinfeng.yf_trajectory.moudle.service.PlayerMusicService;
import com.yinfeng.yf_trajectory.moudle.utils.ConmonUtils;
import com.yinfeng.yf_trajectory.moudle.utils.SMSCore;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class SMSActivity extends BaseActivity implements View.OnClickListener {


    private TextView mActivitySmsTxt;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_sms;
    }

    @Override
    protected void initData() {

    }

    private SMS_Receiver smsReceiver;
    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    protected void initView() {
        super.initView();

        BarUtils.setStatusBarLightMode(this, true);
        BarUtils.setStatusBarColor(SMSActivity.this, getResources().getColor(R.color.p_color_blue_tint));
        mActivitySmsTxt = (TextView) findViewById(R.id.activity_sms_txt);
        mActivitySmsTxt.setOnClickListener(this);


        // 注册接收下行receiver
        smsReceiver = new SMS_Receiver();
        IntentFilter receiverFilter = new IntentFilter(ACTION_SMS_RECEIVER);
        registerReceiver(smsReceiver, receiverFilter);

        if (!isChinaTelecom()) {
            showToastC("请插入集团手机卡");
            mActivitySmsTxt.setText("请插入集团手机卡" + '\n' + "点击重试");
            Logger.v("请插入集团手机卡");
            return;

        }
        if (!ConmonUtils.hasSimCard()) {
            Logger.v("请检查集团手机卡是否就绪或者更换卡槽位置");
            showToastC("请检查集团手机卡是否就绪或者更换卡槽位置");
            mActivitySmsTxt.setText("请检查集团手机卡是否就绪或者更换卡槽位置" + '\n' + "点击重试");
            return;
        }


        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String te1 = tm.getLine1Number();//获取本机号码
        if (TextUtils.isEmpty(te1) || te1 == null) {
            mHandler.sendEmptyMessage(101);
            Logger.v("读入手机sim卡号");
        } else {
            if (te1.length() == 14) {
                Logger.v("读入手机sim卡号");
                String testPhone = te1.substring(3, 14).toString();
                SMSCore.PhoneNumber = testPhone;

            } else if (te1.length() == 11) {
                SMSCore.PhoneNumber = te1;
            } else {

                SMSCore.PhoneNumber = "";
                showToastC("手机卡无效");
                mActivitySmsTxt.setText("手机卡无效，请检查手机号是否正确" + '\n' + "点击重试");
                return;
            }
            if (!RegexUtils.isMobileExact(SMSCore.PhoneNumber)) {
                showToastC("手机卡无效");
                mActivitySmsTxt.setText("手机卡无效，请检查手机号是否正确" + '\n' + "点击重试");
                return;
            }

//            checkPhone();

            showProgress("检测网络中...");
            mHandler.sendEmptyMessage(404);

        }


    }

    int handleWhats = 101;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                case 404:
                    if (!NetworkUtils.isConnected()) {
                        Logger.v("检测网络中...");
                        mHandler.sendEmptyMessageDelayed(404, 1000);
                    } else {
                        dismisProgress();
                        mHandler.removeMessages(404);
                        mHandler.sendEmptyMessageDelayed(303, 1000);
                    }
                    break;
                case 303:
                    Logger.v("303登录...");
                    requestDate(SMSCore.PhoneNumber);
                    break;
                case 101:
                    if (MProgressDialog.isShowing()) {
                        MProgressDialog.dismissProgress();
                    }
                    SMSCore smscore = new SMSCore();
                    Logger.v("发送短信501");
                    smscore.SendSMS2("10001", "501", SMSActivity.this);
                    MProgressDialog.showProgress(SMSActivity.this, "加载中...");
                    mHandler.sendEmptyMessageDelayed(101, 60000);
                    break;
            }
        }
    };

    /**
     * SIM卡是中国电信
     */
    private boolean isChinaTelecom() {
        String imsi = getSimOperator(SMSActivity.this);
        if (imsi == null) return false;
        return imsi.startsWith("46003");
    }

    private String getSimOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
        mHandler.removeMessages(101);
        mHandler.removeMessages(404);
        mHandler.removeMessages(303);
        mHandler = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_sms_txt:
                finish();
                startActivity(getIntent());
                break;
        }
    }

    public void restartApplication(Context context) {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//与正常页面跳转一样可传递序列化数据,在Launch页面内获得
        intent.putExtra("REBOOT", "reboot");
        startActivity(intent);
    }

    public class SMS_Receiver extends BroadcastReceiver {
        final String GetNumberAddress = "10001";

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                //不知道为什么明明只有一条消息，传过来的却是数组，也许是为了处理同时同分同秒同毫秒收到多条短信
                //但这个概率有点小
                SmsMessage[] message = new SmsMessage[pdus.length];
                StringBuilder sb = new StringBuilder();
                System.out.println("pdus长度" + pdus.length);
                String address = "";
                for (int i = 0; i < pdus.length; i++) {
                    //虽然是循环，其实pdus长度一般都是1
                    message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sb.append("接收到短信来自:\n");
                    address = message[i].getDisplayOriginatingAddress();
                    sb.append(address + "\n");
                    sb.append("内容:" + message[i].getDisplayMessageBody());
                    Logger.v("短信"+"内容:" + message[i].getDisplayMessageBody());
                }
                System.out.println(sb.toString());
                if (SMSCore.PhoneNumber == "" && address.equals(GetNumberAddress)) {
                    SMSCore.PhoneNumber = SMSCore.GetPhoneNumberFromSMSText(sb.toString());
                }


//                checkPhone();
                mHandler.sendEmptyMessage(404);

            }
        }
    }

//    private void checkPhone() {
//        String localPhone = LattePreference.getValue(ConstantApi.HK_PHONE);
//        if (TextUtils.isEmpty(localPhone) || localPhone == null) {
//            LattePreference.saveKey(ConstantApi.HK_PHONE, SMSCore.PhoneNumber + "");
//            ActivityUtils.startActivity(LoginVerActivity.class);
//            Logger.v( "SMSActivity初始登录");
//            showToastC("SMSActivity初始登录");
//        } else {
//            if (TextUtils.isEmpty(SMSCore.PhoneNumber)) {
//                showToastC("手机号获取失败");
//                Logger.v( " SMSActivity手机号获取失败");
//                return;
//            } else {
//
//                if (localPhone.equals(SMSCore.PhoneNumber)) {
//                    Logger.v( "SMSActivity本地获取一致");
//                    ActivityUtils.startActivity(LoginVerActivity.class);
//                    showToastC("SMSActivity本地获取一致");
//                } else {
//                    Logger.v( " SMSActivity清除数据");
//                    showToastC("SMSActivity清除数据");
//                    //清除数据
//                    Hawk.deleteAll();
//                    LattePreference.clear();
//                    DaoSession daoSession = BaseApplication.getDaoInstant();
//                    daoSession.deleteAll(GreendaoLocationBean.class);
//                    LattePreference.saveKey(ConstantApi.HK_PHONE, SMSCore.PhoneNumber + "");
//                    ActivityUtils.startActivity(LoginVerActivity.class);
//                }
//            }
//        }
//        mHandler.removeMessages(handleWhats);
//        MProgressDialog.dismissProgress();
//        finish();
////        Log.i(ConstantApi.LOG_I_, "==" + sb.toString().trim());
//        Logger.v( "==" + SMSCore.PhoneNumber);
//
//    }

    private void showTwo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_app_start_icon).setTitle("提示")
                .setMessage("网络无连接").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(getIntent());
//                        ActivityUtils.startActivity(LoginVerActivity.class);
                        //ToDo: 你想做的事情
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        dialogInterface.dismiss();
                        SMSActivity.this.finish();
                    }
                });
        builder.create().show();
    }

    private void requestDate(String username) {
        showProgress("登录中...");
        Map<String, String> mMapValue = new LinkedHashMap<>();
        mMapValue.put("username", username);
        mMapValue.put("msgCode", "6666");
        Logger.v( "API: " + Api.API_login + " par:" + GsonUtils.getInstance().toJson(mMapValue));
        OkHttpUtils
                .postString()
                .content(new Gson().toJson(mMapValue))
                .url(Api.API_login+"?appVersion="+AppUtils.getAppVersionCode())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<ConmonBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        dismisProgress();
                        showTwo();
                    }

                    @Override
                    public void onResponse(ConmonBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            Log.i("testr", "网络结果：" + new Gson().toJson(response));
                            if (TextUtils.isEmpty(response.getData())) {
                                showToastC("登录失败");
                                finish();
                                return;
                            }
                            LattePreference.saveKey(ConstantApi.HK_PHONE, SMSCore.PhoneNumber + "");
                            Logger.v( "登录 token : " + response.getData());
                            LattePreference.saveKey(ConstantApi.HK_ROMOTE_TOKEN, response.getData() + "");
                            Hawk.put(ConstantApi.HK_TOKEN, response.getData() + "");
                            showToastC(response.getMessage());
                            LattePreference.saveKey(ConstantApi.HK_CHECK_LOGIN, "1");
                            AppManager.getInstance().finishAllActivity();
                            ActivityUtils.startActivity(MapActivity.class);
                        } else {
                            showToastC(response.getMessage());
//                            ActivityUtils.startActivity(LoginVerActivity.class);
                            finish();
                        }
                        Logger.v( "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                        finish();
                    }
                });
    }


}
