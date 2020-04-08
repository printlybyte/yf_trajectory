package com.yinfeng.yf_trajectory.moudle.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.google.gson.Gson;
import com.maning.mndialoglibrary.MProgressDialog;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.activity.MapActivity;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean_string;
import com.yinfeng.yf_trajectory.moudle.bean.GetImsiFormPhoneBean;
import com.yinfeng.yf_trajectory.moudle.utils.ConmonUtils;
import com.yinfeng.yf_trajectory.moudle.utils.PermissionUtilsx;
import com.yinfeng.yf_trajectory.moudle.utils.SMSCore;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class ICCIDActivity extends BaseActivity implements View.OnClickListener {
    private TextView mActivitySmsTxt;
    private String iccid = "";

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_iccid;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        super.initView();

        BarUtils.setStatusBarLightMode(this, true);
        BarUtils.setStatusBarColor(ICCIDActivity.this, getResources().getColor(R.color.p_color_blue_tint));
        mActivitySmsTxt = (TextView) findViewById(R.id.activity_sms_txt);
        mActivitySmsTxt.setOnClickListener(this);

        if (!ConmonUtils.hasSimCard() || !isChinaTelecom()) {
            mActivitySmsTxt.setText("请检查集团手机卡是否就绪或查看权限是否关闭");
            checkNet("请检查集团手机卡是否就绪或查看权限是否关闭", 4);
            return;
        }

        if (!checkP()) {
            return;
        }


        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            iccid = tm.getSimSerialNumber();
            showProgress("检测网络中...");
            mHandler.sendEmptyMessage(404);

        } catch (Exception e) {
            checkNet("未获取的手机卡的识别码，请重试", 3);
        }

    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                case 404:
                    if (!NetworkUtils.isConnected()) {
                        Log.i("testre", "检测网络中...");
                        mHandler.sendEmptyMessageDelayed(404, 1000);
                    } else {
                        mHandler.removeMessages(404);
                        mHandler.sendEmptyMessageDelayed(201, 1000);
                    }
                    break;
                case 201:
                    Log.i("testre", "201登录...");
                    requestDate(iccid);
                    break;
            }
        }
    };

    /**
     * SIM卡是中国电信
     */
    private boolean isChinaTelecom() {
        String imsi = getSimOperator(ICCIDActivity.this);
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
        mHandler.removeMessages(404);
        mHandler.removeMessages(201);
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


    /**
     * type==1  网络无连接  type==2   type==3 imsi获取失败  type==4 无卡
     * 5 数据转换异常 type=6 发送短信  type ==7  添加手机号 type==8 登录失败 type==9 打开权限
     */
    private void checkNet(String msg, int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false).setIcon(R.mipmap.ic_app_start_icon).setTitle("提示")
                .setMessage("" + msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (type == 8) {
                            requestDate(iccid);
                        } else if (type == 9 || type == 4) {
                            gotoAppDetailIntent(ICCIDActivity.this);
                        } else if (type == 3) {
                            finish();
                            startActivity(getIntent());
                        } else {
                            finish();
                            startActivity(getIntent());
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        builder.create().show();
    }

    /**
     * 跳转到应用详情界面
     */
    public static void gotoAppDetailIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, 909);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 909) {
            if (!ConmonUtils.hasSimCard() || !isChinaTelecom()) {
                mActivitySmsTxt.setText("请检查集团手机卡是否就绪或查看权限是否关闭");
                checkNet("请检查集团手机卡是否就绪或查看权限是否关闭", 4);
                return;
            }
            if (!checkP()) {
                return;
            }
            ;
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                iccid = tm.getSimSerialNumber();
            } catch (Exception e) {
                checkNet("未获取的手机卡的识别号码，请重试", 3);
                return;
            }
            showProgress("检测网络中...");
            mHandler.sendEmptyMessage(404);
        }
    }

    /**
     * 通过imsi添加手机号
     */
    private boolean checkP() {
        if (!PermissionUtilsx.is_PHONE_STATUS()) {
            checkNet("银丰轨迹权限-电话权限已关闭，请及时打开", 9);
            return false;
        }
        if (!PermissionUtilsx.is_STORAGE()) {
            checkNet("银丰轨迹权限-存储权限已关闭，请及时打开", 9);
            return false;
        }
        if (!PermissionUtilsx.is_LOCATION()) {
            checkNet("银丰轨迹权限-位置信息权限已关闭，请及时打开", 9);
            return false;
        }

        if (!NetworkUtils.isConnected()) {
            checkNet("网络无连接，请检测网络是否可以正常上网", 1);
            return false;
        }
        return true;
    }

    private void requestDate(String iccid) {
        if (!checkP()) {
            return;
        }

        if (TextUtils.isEmpty(iccid)) {
            checkNet("未获取的手机卡的识别号码，请重试", 3);
            return;
        }
        showProgress("登录中...");
        Map<String, String> mMapValue = new LinkedHashMap<>();
        mMapValue.put("iccid", iccid);
        Log.i("testre", "API: " + Api.API_login + " par:" + GsonUtils.getInstance().toJson(mMapValue));
        OkHttpUtils
                .postString()
                .content(new Gson().toJson(mMapValue))
                .url(Api.API_login + "?appVersion=" + AppUtils.getAppVersionCode())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<ConmonBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dismisProgress();
                        checkNet("数据解析异常", 5);
                    }

                    @Override
                    public void onResponse(ConmonBean response, int id) {
                        dismisProgress();
                        mHandler.removeMessages(201);
                        mHandler.removeMessages(404);
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            Log.i("testre", " 网络结果：" + new Gson().toJson(response));
                            if (TextUtils.isEmpty(response.getData())) {
                                showToastC("登录失败 getData=null");
                                finish();
                                return;
                            }
                            LattePreference.saveKey(ConstantApi.HK_ICCID, iccid + "");
                            Log.i("testre", "登录 token : " + response.getData());
                            LattePreference.saveKey(ConstantApi.HK_ROMOTE_TOKEN, response.getData() + "");
                            Hawk.put(ConstantApi.HK_TOKEN, response.getData() + "");

                            showToastC(response.getMessage());
                            AppManager.getInstance().finishAllActivity();
                            ActivityUtils.startActivity(MapActivity.class);
                            finish();
                        } else {
                            checkNet("登录结果：" + response.getMessage(), 8);
                        }
                        Log.i("testre", "请求结果：" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


}
