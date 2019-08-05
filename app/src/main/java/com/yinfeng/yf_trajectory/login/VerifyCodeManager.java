package com.yinfeng.yf_trajectory.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.maning.mndialoglibrary.MToast;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.bean.SendSmsBean;

import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class VerifyCodeManager {

    private Context mContext;
    private int recLen = 60;
    private Timer timer = new Timer();
    private Handler mHandler = new Handler();
    private String phone;

    private EditText phoneEdit;
    private Button getVerifiCodeButton;

    public VerifyCodeManager(Context context, EditText editText, Button btn) {
        this.mContext = context;
        this.phoneEdit = editText;
        this.getVerifiCodeButton = btn;
    }

    public void getVerifyCode(int type) {
        // 获取验证码之前先判断手机号
        phone = phoneEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            MToast.makeTextLong(mContext, "手机号码不能为空");
            return;
        } else if (phone.length() < 11) {
            MToast.makeTextLong(mContext, "手机号码不足11位");
            return;
        } else if (!RegexUtils.isMobileSimple(phone)) {
            MToast.makeTextLong(mContext, "手机号码格式不正确");
            return;
        }
        requst(type);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setButtonStatusOff();
                        if (recLen < 1) {
                            setButtonStatusOn();
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1000);


        //多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。




    }

    @SuppressLint("StringFormatMatches")
    private void setButtonStatusOff() {
        getVerifiCodeButton.setText("剩余" + String.format(
                mContext.getResources().getString(R.string.count_down), recLen--));
        getVerifiCodeButton.setClickable(false);
        getVerifiCodeButton.setTextColor(Color.parseColor("#cfd3e3"));
//        getVerifiCodeButton.setBackgroundColor(Color.parseColor("#b1b1b3"));
    }

    private void setButtonStatusOn() {
        timer.cancel();
        getVerifiCodeButton.setText("重新发送");
        getVerifiCodeButton.setTextColor(Color.parseColor("#b1b1b3"));
//        getVerifiCodeButton.setBackgroundColor(Color.parseColor("#f3f4f8"));
        recLen = 60;
        getVerifiCodeButton.setClickable(true);
    }

    /**
     * ============================================
     * 描  述：
     * 101: app注册; 102: app登录; 103 app修改密码; 104 app修改手机号
     * 返回类型：
     * 创建人：lgd
     * 创建时间：2018/11/23 13:56
     * ============================================
     **/
    private void requst(int type) {
        String mUrl = Api.API_SmsSendCode + phone;
        String mType = "";
        if (type == 101) {
            mType = "101";
        }
        if (type == 102) {
            mType = "102";
        }
        if (type == 103) {
            mType = "103";
        }
        if (type == 104) {
            mType = "104";
        }

        OkGo.<String>get(mUrl)
                .tag(this)
                .params("mobile", phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String data = response.body();//这个就是返回来的结果
                        Timber.i("success:%s", data);
                        Log.i("testd", "" + data);
                        SendSmsBean bean = new Gson().fromJson(data, SendSmsBean.class);
                        if (bean != null) {
                            if (bean.isSuccess()) {
                                MToast.makeTextLong(mContext, bean.getMessage());
                            } else {
                                MToast.makeTextLong(mContext, bean.getMessage());
                                setButtonStatusOn();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i("testd", "" + response);
                        SendSmsBean bean = new Gson().fromJson(response.body(), SendSmsBean.class);
                        if (bean != null) {
                            MToast.makeTextLong(mContext, bean.getMessage());
                            setButtonStatusOn();
                        }
                    }
                });
    }
}





