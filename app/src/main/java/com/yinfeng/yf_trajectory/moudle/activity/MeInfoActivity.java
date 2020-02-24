package com.yinfeng.yf_trajectory.moudle.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.LeaveHistoryActivityBean;
import com.yinfeng.yf_trajectory.moudle.bean.UserInfoBean;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

public class MeInfoActivity extends BaseActivity implements View.OnClickListener {


    private TextView mActivityMeInfoPhone;


    private TextView mActivityMeInfoIdCardNum;
    private LinearLayout mActivityMeInfoRequest;
    /**
     *
     */
    private TextView mActivityMeInfoName;
    /**
     *
     */
    private TextView mActivityMeInfoCompany;
    /**
     *
     */
    private TextView mActivityMeInfoDepartment;
    /**
     *
     */
    private TextView mActivityMeInfoJob;
    private TextView mActivityMeInfoHintDouble;
    /**
     *
     */
    private TextView mActivityMeInfoVersion;
    private LinearLayout mActivityMeInfoLeave;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_me_info;
    }

    @Override
    protected void initData() {
        setTitle("个人中心");

        requestDate(1, "查询数据中");

    }

    @Override
    protected void initView() {
        super.initView();

        mActivityMeInfoPhone = (TextView) findViewById(R.id.activity_me_info_phone);
        mActivityMeInfoIdCardNum = (TextView) findViewById(R.id.activity_me_info_id_card_num);
        mActivityMeInfoRequest = (LinearLayout) findViewById(R.id.activity_me_info_request);
        mActivityMeInfoRequest.setOnClickListener(this);
        mActivityMeInfoName = (TextView) findViewById(R.id.activity_me_info_name);
        mActivityMeInfoCompany = (TextView) findViewById(R.id.activity_me_info_company);
        mActivityMeInfoDepartment = (TextView) findViewById(R.id.activity_me_info_department);
        mActivityMeInfoJob = (TextView) findViewById(R.id.activity_me_info_job);
        mActivityMeInfoHintDouble = (TextView) findViewById(R.id.activity_me_info_hint_double);
        mActivityMeInfoHintDouble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ActivityUtils.startActivity(MainActivity.class);
                return false;
            }
        });
        mActivityMeInfoVersion = (TextView) findViewById(R.id.activity_me_info_version);
        mActivityMeInfoLeave = (LinearLayout) findViewById(R.id.activity_me_info_leave);
        mActivityMeInfoLeave.setOnClickListener(this);
    }


    private void requestDate(final int type, String msg) {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        if (type == 1) {
            showProgress(msg);
        }
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("个人信息查询失败");
            return;
        }
        Log.i("testre", "API: " + Api.API_user_info + " par: token");

        OkHttpUtils
                .post()
                .addHeader("track-token", token)
                .url(Api.API_user_info)
                .build()
                .execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(UserInfoBean response, int id) {
                        if (type == 1) {
                            dismisProgress();
                        }
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            UserInfoBean.DataBean bean = response.getData();
                            mActivityMeInfoIdCardNum.setText(bean.getIdCard() == "" ? "无数据" : bean.getIdCard());
                            mActivityMeInfoPhone.setText(bean.getPhone() == "" ? "无数据" : bean.getPhone());
                            mActivityMeInfoCompany.setText(bean.getCompany() == "" ? "无数据" : bean.getCompany());
                            mActivityMeInfoName.setText(bean.getName() == "" ? "无数据" : bean.getName());
                            mActivityMeInfoDepartment.setText(bean.getDepartment() == "" ? "无数据" : bean.getDepartment());
                            mActivityMeInfoJob.setText(bean.getJob() == "" ? "无数据" : bean.getJob());

                            mActivityMeInfoVersion.setText("v: " + AppUtils.getAppVersionName());
                        } else if (response.getCode() == ConstantApi.API_REQUEST_ERR_901) {
                            LattePreference.clear();
                            showTwo(response.getMessage());

                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i("testre", "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }

    /**
     * 提交手机号数据
     */
    private void showTwo(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_app_start_icon).setTitle("提示")
                .setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginErr();
//                        ActivityUtils.startActivity(LoginVerActivity.class);
                        //ToDo: 你想做的事情
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        loginErr();
                    }
                });
        builder.create().show();
    }

    private void loginErr() {
        Toast.makeText(MeInfoActivity.this, "您的账号在其他地方登陆！", Toast.LENGTH_SHORT).show();
        Log.i("testre", "账号在其他地方登陆");
        LattePreference.clear();
        AppManager.getInstance().finishAllActivity();
        finish();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_me_info_request:

                ActivityUtils.startActivity(ApplicationRecordActivity.class);
                break;

            case R.id.activity_me_info_leave:

                ActivityUtils.startActivity(LeaveHistoryActivity.class);

                break;
        }
    }



}
