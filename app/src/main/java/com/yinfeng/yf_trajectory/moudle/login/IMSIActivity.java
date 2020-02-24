//package com.yinfeng.yf_trajectory.moudle.login;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AlertDialog;
//import android.telephony.SmsMessage;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import com.blankj.utilcode.util.ActivityUtils;
//import com.blankj.utilcode.util.AppUtils;
//import com.blankj.utilcode.util.BarUtils;
//import com.blankj.utilcode.util.NetworkUtils;
//import com.blankj.utilcode.util.PhoneUtils;
//import com.blankj.utilcode.util.RegexUtils;
//import com.caitiaobang.core.app.app.AppManager;
//import com.caitiaobang.core.app.app.BaseActivity;
//import com.caitiaobang.core.app.net.GenericsCallback;
//import com.caitiaobang.core.app.net.JsonGenericsSerializator;
//import com.caitiaobang.core.app.storge.LattePreference;
//import com.google.gson.Gson;
//import com.maning.mndialoglibrary.MProgressDialog;
//import com.orhanobut.hawk.Hawk;
//import com.yinfeng.yf_trajectory.Api;
//import com.yinfeng.yf_trajectory.ConstantApi;
//import com.yinfeng.yf_trajectory.GsonUtils;
//import com.yinfeng.yf_trajectory.R;
//import com.yinfeng.yf_trajectory.moudle.activity.MapActivity;
//import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean;
//import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean_string;
//import com.yinfeng.yf_trajectory.moudle.bean.GetImsiFormPhoneBean;
//import com.yinfeng.yf_trajectory.moudle.utils.ConmonUtils;
//import com.yinfeng.yf_trajectory.moudle.utils.SMSCore;
//import com.zhy.http.okhttp.OkHttpUtils;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import okhttp3.Call;
//import okhttp3.MediaType;
//
//public class IMSIActivity extends BaseActivity implements View.OnClickListener {
//
//    private SMS_Receiver smsReceiver;
//    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";
//
//    private TextView mActivitySmsTxt;
//
//    private String loginPhone = "";
//
//    @Override
//    protected int getContentLayoutId() {
//        return R.layout.activity_imsi;
//    }
//
//    @Override
//    protected void initData() {
//
//    }
//
//
//    @Override
//    protected void initView() {
//        super.initView();
//
//        BarUtils.setStatusBarLightMode(this, true);
//        BarUtils.setStatusBarColor(IMSIActivity.this, getResources().getColor(R.color.p_color_blue_tint));
//        mActivitySmsTxt = (TextView) findViewById(R.id.activity_sms_txt);
//        mActivitySmsTxt.setOnClickListener(this);
//
//
//        if (mRxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            checkNet("请打开存储权限", 9);
//            return;
//        }
//
//
//        if (mRxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
//            checkNet("请打开位置信息权限", 9);
//            return;
//        }
//
//
//        if (mRxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE)) {
//            checkNet("请打开电话权限", 9);
//            return;
//        }
//
//        if (mRxPermissions.isGranted(Manifest.permission.SEND_SMS)) {
//            checkNet("请打开信息权限", 9);
//            return;
//        }
//
//        if (!ConmonUtils.hasSimCard() || !isChinaTelecom()) {
//            mActivitySmsTxt.setText("请检查集团手机卡是否就绪或者更换卡槽位置" + '\n' + "点击重试");
//            checkNet("请检查集团手机卡是否就绪或者更换卡槽位置", 4);
//            return;
//        }
//        if (!NetworkUtils.isConnected()) {
//            checkNet("网络无连接", 1);
//            return;
//        }
//
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        String iccid = tm.getSimSerialNumber();
//
//       Log.i("testre",iccid);
//
//        registerSMS_Receiver();
//        requestGetPhone();
//
//
////        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
////        String te1 = tm.getLine1Number();//获取本机号码
////        if (TextUtils.isEmpty(te1) || te1 == null) {
////            mHandler.sendEmptyMessage(101);
////           Log.i("testre","读入手机sim卡号");
////        } else {
////            if (te1.length() == 14) {
////               Log.i("testre","读入手机sim卡号");
////                String testPhone = te1.substring(3, 14).toString();
////                SMSCore.PhoneNumber = testPhone;
////
////            } else if (te1.length() == 11) {
////                SMSCore.PhoneNumber = te1;
////            } else {
////
////                SMSCore.PhoneNumber = "";
////                showToastC("手机卡无效");
////                mActivitySmsTxt.setText("手机卡无效，请检查手机号是否正确" + '\n' + "点击重试");
////                return;
////            }
////            if (!RegexUtils.isMobileExact(SMSCore.PhoneNumber)) {
////                showToastC("手机卡无效");
////                mActivitySmsTxt.setText("手机卡无效，请检查手机号是否正确" + '\n' + "点击重试");
////                return;
////            }
////
//////            checkPhone();
////
////            showProgress("检测网络中...");
////            mHandler.sendEmptyMessage(404);
////
////        }
//
//
//    }
//
//    int handleWhats = 101;
//    @SuppressLint("HandlerLeak")
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                default:
//                case 404:
//                    if (!NetworkUtils.isConnected()) {
//                       Log.i("testre","检测网络中...");
//                        mHandler.sendEmptyMessageDelayed(404, 1000);
//                    } else {
//                        mHandler.removeMessages(404);
//                        mHandler.sendEmptyMessageDelayed(303, 1000);
//                    }
//                    break;
//                case 303:
//                   Log.i("testre","303登录...");
//                    requestDate();
//                    break;
//                case 101:
//                    if (MProgressDialog.isShowing()) {
//                        MProgressDialog.dismissProgress();
//                    }
//                    SMSCore smscore = new SMSCore();
//                   Log.i("testre","发送短信501");
//                    smscore.SendSMS2("10001", "501", IMSIActivity.this);
//                    MProgressDialog.showProgress(IMSIActivity.this, "发送中...");
//                    mHandler.sendEmptyMessageDelayed(101, 60000);
//                    break;
//            }
//        }
//    };
//
//    /**
//     * SIM卡是中国电信
//     */
//    private boolean isChinaTelecom() {
//        String imsi = getSimOperator(IMSIActivity.this);
//        if (imsi == null) return false;
//        return imsi.startsWith("46003");
//    }
//
//    private String getSimOperator(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        return tm.getSubscriberId();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (smsReceiver != null) {
//            unregisterReceiver(smsReceiver);
//        }
//        mHandler.removeMessages(101);
//        mHandler.removeMessages(404);
//        mHandler.removeMessages(303);
//        mHandler = null;
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            default:
//                break;
//            case R.id.activity_sms_txt:
//                finish();
//                startActivity(getIntent());
//                break;
//        }
//    }
//
//    public void restartApplication(Context context) {
//        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////与正常页面跳转一样可传递序列化数据,在Launch页面内获得
//        intent.putExtra("REBOOT", "reboot");
//        startActivity(intent);
//    }
//
//    /**
//     * type==1  网络无连接  type==2   type==3 imsi获取失败  type==4 无卡
//     * 5 数据转换异常 type=6 发送短信  type ==7  添加手机号 type==8 登录失败 type==9 打开权限
//     */
//    private void checkNet(String msg, int type) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false).setIcon(R.mipmap.ic_app_start_icon).setTitle("提示")
//                .setMessage("" + msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (type == 1) {
//                            finish();
//                            startActivity(getIntent());
//                        } else if (type == 3) {
//                            requestGetPhone();
//                        } else if (type == 4) {
//                            finish();
//                            startActivity(getIntent());
//                        } else if (type == 5) {
//                            requestGetPhone();
//                        } else if (type == 6) {
//                            mHandler.sendEmptyMessage(101);
//                        } else if (type == 7) {
//                            requestAddPhone();
//                        } else if (type == 8) {
//                            requestDate();
//                        } else if (type == 9) {
//                            gotoAppDetailIntent(IMSIActivity.this);
//                        } else {
//                            requestGetPhone();
//                        }
//                    }
//                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (type == 1) {
//                            dialogInterface.dismiss();
//                            IMSIActivity.this.finish();
//                        } else if (type == 3) {
//                            finish();
//                        } else if (type == 4) {
//                            finish();
//                        } else if (type == 5) {
//                            finish();
//                        } else if (type == 6) {
//                            finish();
//                        } else if (type == 7) {
//                            finish();
//                        } else if (type == 8) {
//                            finish();
//                        } else if (type == 9) {
//                            finish();
//                        } else {
//                            requestGetPhone();
//                        }
//                    }
//                });
//        builder.create().show();
//    }
//
//    /**
//     * 跳转到应用详情界面
//     */
//    public static void gotoAppDetailIntent(Activity activity) {
//        Intent intent = new Intent();
//        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + activity.getPackageName()));
//        activity.startActivity(intent);
//    }
//
//    /**
//     * 通过imsi获取手机号
//     */
//    private void requestGetPhone() {
//        String mImsi = PhoneUtils.getIMSI();
//        if (TextUtils.isEmpty(mImsi)) {
//            checkNet("imsi 获取失败", 3);
//            return;
//        }
//       Log.i("testre","mImsi： " + mImsi);
//        showProgress("请求中...");
//        Map<String, String> mMapValue = new LinkedHashMap<>();
//        mMapValue.put("simCode", mImsi);
//        OkHttpUtils
//                .postString()
//                .content(new Gson().toJson(mMapValue))
//                .url(Api.commonGetPhoneByCode)
//                .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                .build()
//                .execute(new GenericsCallback<GetImsiFormPhoneBean>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        showToastC("网络异常，请稍后重试" + e.getMessage());
//                        dismisProgress();
//                        checkNet("数据解析异常", 5);
//                    }
//
//                    @Override
//                    public void onResponse(GetImsiFormPhoneBean response, int id) {
//                        dismisProgress();
//                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
////                            requestDate(response.getData().getPhone() + "");
//
//                            String mPhone = response.getData().getPhone();
//                            if (TextUtils.isEmpty(mPhone)) {
//                                mHandler.sendEmptyMessage(101);
//                                showToastC("没有检测到手机号，通过发送短信的方式获取手机号");
////                                checkNet("将通过发送短信的方式获取手机号码,是否继续？", 6);
//                            } else {
//                                loginPhone = mPhone;
//                                showProgress("检测网络中...");
//                                mHandler.sendEmptyMessage(404);
//                            }
//
//                        } else {
//                            checkNet(response.getMessage(), 2);
//                        }
//                       Log.i("testre","请求结果：" + GsonUtils.getInstance().toJson(response));
//
//
//                    }
//                });
//    }
//
//
//    /**
//     * 通过imsi添加手机号
//     */
//    private void requestAddPhone() {
//        String mImsi = PhoneUtils.getIMSI();
//        if (TextUtils.isEmpty(mImsi)) {
//            checkNet("imsi 获取失败", 3);
//            return;
//        }
//       Log.i("testre","mImsi： " + mImsi);
//        showProgress("请求中...");
//        Map<String, String> mMapValue = new LinkedHashMap<>();
//        mMapValue.put("simCode", mImsi);
//        mMapValue.put("phone", "" + SMSCore.PhoneNumber);
//        OkHttpUtils
//                .postString()
//                .content(new Gson().toJson(mMapValue))
//                .url(Api.commonAddPhone)
//                .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                .build()
//                .execute(new GenericsCallback<ConmonBean_string>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        showToastC("网络异常，请稍后重试" + e.getMessage());
//                        dismisProgress();
//                        checkNet("数据解析异常", 5);
//                    }
//
//                    @Override
//                    public void onResponse(ConmonBean_string response, int id) {
//                        dismisProgress();
//                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
//
//                            requestGetPhone();
//                        } else {
//                            checkNet(response.getMessage(), 7);
//                        }
//                       Log.i("testre","请求结果：" + GsonUtils.getInstance().toJson(response));
//                    }
//                });
//    }
//
//    private void requestDate() {
//        if (TextUtils.isEmpty(loginPhone)) {
//            mHandler.sendEmptyMessage(101);
//            showToastC("没有检测到手机号，通过发送短信的方式获取手机号");
//            return;
//        }
//        showProgress("登录中...");
//        Map<String, String> mMapValue = new LinkedHashMap<>();
//        mMapValue.put("username", loginPhone);
//        mMapValue.put("msgCode", "6666");
//       Log.i("testre","API: " + Api.API_login + " par:" + GsonUtils.getInstance().toJson(mMapValue));
//        OkHttpUtils
//                .postString()
//                .content(new Gson().toJson(mMapValue))
//                .url(Api.API_login + "?appVersion=" + AppUtils.getAppVersionCode())
//                .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                .build()
//                .execute(new GenericsCallback<ConmonBean>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        dismisProgress();
//                        checkNet("数据解析异常", 5);
//                    }
//
//                    @Override
//                    public void onResponse(ConmonBean response, int id) {
//
//                        dismisProgress();
//                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
//                           Log.i("testre"," 网络结果：" + new Gson().toJson(response));
//                            if (TextUtils.isEmpty(response.getData())) {
//                                showToastC("登录失败");
//                                finish();
//                                return;
//                            }
//                            LattePreference.saveKey(ConstantApi.HK_PHONE, SMSCore.PhoneNumber + "");
//                           Log.i("testre","登录 token : " + response.getData());
//                            LattePreference.saveKey(ConstantApi.HK_ROMOTE_TOKEN, response.getData() + "");
//                            Hawk.put(ConstantApi.HK_TOKEN, response.getData() + "");
//                            showToastC(response.getMessage());
//                            LattePreference.saveKey(ConstantApi.HK_CHECK_LOGIN, "1");
//                            AppManager.getInstance().finishAllActivity();
//                            ActivityUtils.startActivity(MapActivity.class);
//                            finish();
//                        } else {
//                            checkNet(response.getMessage(), 8);
////                            showToastC(response.getMessage());
//                        }
//                       Log.i("testre","请求结果：" + GsonUtils.getInstance().toJson(response));
//                        mHandler.removeMessages(101);
//                        mHandler.removeMessages(404);
//                        mHandler.removeMessages(303);
//
//                    }
//                });
//    }
//
//
//    private void registerSMS_Receiver() {
//        // 注册接收下行receiver
//        smsReceiver = new SMS_Receiver();
//        IntentFilter receiverFilter = new IntentFilter(ACTION_SMS_RECEIVER);
//        registerReceiver(smsReceiver, receiverFilter);
//    }
//
//    public class SMS_Receiver extends BroadcastReceiver {
//        final String GetNumberAddress = "10001";
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // TODO Auto-generated method stub
//            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
//                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
//                //不知道为什么明明只有一条消息，传过来的却是数组，也许是为了处理同时同分同秒同毫秒收到多条短信
//                //但这个概率有点小
//                SmsMessage[] message = new SmsMessage[pdus.length];
//                StringBuilder sb = new StringBuilder();
//                System.out.println("pdus长度" + pdus.length);
//                String address = "";
//                for (int i = 0; i < pdus.length; i++) {
//                    //虽然是循环，其实pdus长度一般都是1
//                    message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    sb.append("接收到短信来自:\n");
//                    address = message[i].getDisplayOriginatingAddress();
//                    sb.append(address + "\n");
//                    sb.append("内容:" + message[i].getDisplayMessageBody());
//                   Log.i("testre","短信" + "内容:" + message[i].getDisplayMessageBody());
//                }
//                System.out.println(sb.toString());
//                if (SMSCore.PhoneNumber == "" && address.equals(GetNumberAddress)) {
//                    SMSCore.PhoneNumber = SMSCore.GetPhoneNumberFromSMSText(sb.toString());
//                }
//
//                requestAddPhone();
////                checkPhone();
//
//            }
//        }
//    }
//
//}
