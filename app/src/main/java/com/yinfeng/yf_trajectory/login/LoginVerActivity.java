package com.yinfeng.yf_trajectory.login;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.MapActivity;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.bean.ConmonBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class LoginVerActivity extends BaseActivity implements View.OnClickListener {


    /**
     * 手机号/邮箱
     */
    private EditText mActivityPasswordLoginPhone;
    /**
     * 密码
     */
    private EditText mActivityPasswordLoginPassword;
    /**
     * 登录
     */
    private Button mActivityLoginConfirm;
    private VerifyCodeManager mVerifyCodeManager;
    /**
     * 获取验证码
     */
    private Button mActivityPasswordLoginSendSmscode;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_password_login;
    }

    @Override
    protected void initData() {
        mTitletBtn.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        super.initView();
        BarUtils.setStatusBarColor(LoginVerActivity.this, getResources().getColor(R.color.p_color_blue_tint));
        mActivityPasswordLoginPhone = (EditText) findViewById(R.id.activity_password_login_phone);
        mActivityPasswordLoginPassword = (EditText) findViewById(R.id.activity_password_login_password);
        mActivityLoginConfirm = (Button) findViewById(R.id.activity_login_confirm);
        mActivityLoginConfirm.setOnClickListener(this);

        mActivityPasswordLoginSendSmscode = (Button) findViewById(R.id.activity_password_login_send_smscode);
        mActivityPasswordLoginSendSmscode.setOnClickListener(this);
        mVerifyCodeManager = new VerifyCodeManager(this, mActivityPasswordLoginPhone, mActivityPasswordLoginSendSmscode);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;

            case R.id.activity_login_confirm:
                String phone = mActivityPasswordLoginPhone.getText().toString().trim();
                String password = mActivityPasswordLoginPassword.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showToastC("请输入手机号");
                    return;
                }
                if (!RegexUtils.isMobileExact(phone)) {
                    showToastC("请输入正确手机号");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToastC("请输入验证码");
                    return;
                }
                requestDate(1, "登录中...", phone, password);
                break;
            case R.id.activity_password_login_send_smscode:
                mVerifyCodeManager.getVerifyCode(0);
                break;
        }
    }

    private void requestDate(final int type, String msg, String username, String msgCode) {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        if (type == 1) {
            showProgress(msg);
        }
        Map<String, String> mMapValue = new LinkedHashMap<>();
        mMapValue.put("username", username);
        mMapValue.put("msgCode", msgCode);
        Log.i(ConstantApi.LOG_I_NET, "API: " + Api.API_login + " par:" + GsonUtils.getInstance().toJson(mMapValue));
        OkHttpUtils
                .postString()
                .content(new Gson().toJson(mMapValue))
                .url(Api.API_login)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<ConmonBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(ConmonBean response, int id) {
                        if (type == 1) {
                            dismisProgress();
                        }
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            Log.i("testr", "网络结果：" + new Gson().toJson(response));
                            if (TextUtils.isEmpty(response.getData())) {
                                showToastC("登录失败");
                                return;
                            }
                            Hawk.put(ConstantApi.HK_TOKEN, response.getData() + "");
                            showToastC(response.getMessage());
                            Hawk.put("already_login_yfgj", "1");
//                            AppManager.getInstance().finishAllActivity();
                            ActivityUtils.startActivity(MapActivity.class);
                        } else {
                            showToastC(response.getMessage());
                        }

                        Log.i(ConstantApi.LOG_I_NET, "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }


}
