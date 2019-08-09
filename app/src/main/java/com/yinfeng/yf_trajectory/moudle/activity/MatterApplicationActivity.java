package com.yinfeng.yf_trajectory.moudle.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.MatterApplicationActivityBean;
import com.yinfeng.yf_trajectory.moudle.bean.MatterApplicationActivityStatusBean;
import com.yinfeng.yf_trajectory.moudle.bean.UserInfoBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * ============================================
 * 描  述：外出事项申请
 * 包  名：com.yinfeng.yf_trajectory.moudle.activity
 * 类  名：MatterApplicationActivity
 * 创建人：liuguodong
 * 创建时间：2019/8/6 15:55
 * ============================================
 **/
public class MatterApplicationActivity extends BaseActivity implements OnDateSetListener, View.OnClickListener {


    /**
     * 点击填写前往地点
     */
    private EditText mActivityMatterApplicationInputAddress;
    /**
     * 点击填写申请理由
     */
    private EditText mActivityMatterApplicationInputReson;
    /**
     * 点击选择开始时间
     */
    private TextView mActivityMatterApplicationStartTime;
    /**
     * 点击选择结束时间
     */
    private TextView mActivityMatterApplicationEndTime;
    private int mTimeType = 0;
    /**
     * 行程开始
     */
    private TextView mActivityMatterApplicationStokeChick;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_matter_application;
    }

    @Override
    protected void initData() {
        setTitle("外出事项");
        requestDate(1, "查询中...");
    }

    @Override
    protected void initView() {
        super.initView();
        initTimeDialog();
        mActivityMatterApplicationInputAddress = (EditText) findViewById(R.id.activity_matter_application_input_address);
        mActivityMatterApplicationInputReson = (EditText) findViewById(R.id.activity_matter_application_input_reson);
        mActivityMatterApplicationStartTime = (TextView) findViewById(R.id.activity_matter_application_start_time);
        mActivityMatterApplicationStartTime.setOnClickListener(this);
        mActivityMatterApplicationEndTime = (TextView) findViewById(R.id.activity_matter_application_end_time);
        mActivityMatterApplicationEndTime.setOnClickListener(this);
        mActivityMatterApplicationStokeChick = (TextView) findViewById(R.id.activity_matter_application_stoke_chick);
        mActivityMatterApplicationStokeChick.setOnClickListener(this);
    }


    private String mTimeStart = "";
    private String mTimeEnd = "";
    private TimePickerDialog mDialogYearMonthDay;

    private void initTimeDialog() {
        mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.ALL)
                .setTitleStringId("选择时间")
                .setCallBack(this)
                .setCyclic(false)
                .setThemeColor(R.color.p_color_blue)
                .setMinMillseconds(System.currentTimeMillis())
                .build();
    }


    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        String text = ConmonUtils.getDateToString(millseconds);
//        mTime = millseconds / 1000 + "";
        Log.i("", "");
        if (mTimeType == 1) {
            mTimeStart = text;
            mActivityMatterApplicationStartTime.setText(text);

        } else if (mTimeType == 2) {
            mTimeEnd = text;
            mActivityMatterApplicationEndTime.setText(text);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_matter_application_start_time:
                mTimeType = 1;
                mDialogYearMonthDay.show(getSupportFragmentManager(), "1");
                break;
            case R.id.activity_matter_application_end_time:
                mTimeType = 2;
                mDialogYearMonthDay.show(getSupportFragmentManager(), "2");

                break;
            case R.id.activity_matter_application_stoke_chick:
                checkInput();
                break;
        }
    }

    private void checkInput() {
        String mAddress = mActivityMatterApplicationInputAddress.getText().toString().trim();
        String mReson = mActivityMatterApplicationInputReson.getText().toString().trim();
        if (TextUtils.isEmpty(mAddress)) {
            showToastC("请输入前往地点");
            return;
        }
        if (TextUtils.isEmpty(mReson)) {
            showToastC("请输入申请原因");
            return;
        }

//        if (TextUtils.isEmpty(mTimeStart)) {
//            showToastC("请选择开始时间");
//            return;
//        }
//        if (TextUtils.isEmpty(mTimeEnd)) {
//            showToastC("请选择结束时间");
//            return;
//        }
        requestDateCommit(1, "处理中...", mReson, mAddress);
    }

    private void requestDateCommit(final int type, String msg, String reason, String address) {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        if (type == 1) {
            showProgress(msg);
        }
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("token = null ");
            return;
        }
        String mUrl = "";
        if (!TextUtils.isEmpty(mStatus)) {
            if (mStatus.equals("0")) {
                mUrl = Api.API_apply_applyBegin;
            } else if (mStatus.equals("1")) {
                mUrl = Api.API_apply_applyEnd;
            } else {
                mUrl = Api.API_apply_applyBegin;
            }
        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.clear();
        map.put("reason", reason);
        map.put("title", "标题： " + reason);
        map.put("address", address);
        Log.i(ConstantApi.LOG_I_NET, "API: " + mUrl + "发送json：" + new Gson().toJson(map));
        OkHttpUtils
                .postString()
                .addHeader("track-token", token)
                .content(new Gson().toJson(map))
                .url(mUrl)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<MatterApplicationActivityBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(MatterApplicationActivityBean response, int id) {
                        if (type == 1) {
                            dismisProgress();
                        }
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            if (mStatus.equals("0")) {
                                showToastC("记录成功");
                            } else {
                                showToastC("结束记录成功");
                            }
                            finish();
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i(ConstantApi.LOG_I_NET, "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }


    private String mStatus = "0";//返回0 代表按钮状态应为开始记录轨迹 ，返回1代表按钮状态应为结束记录轨迹

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
            showToastC("token = null ");
            return;
        }
        Log.i(ConstantApi.LOG_I_NET, "API: " + Api.API_apply_judge);
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
                .url(Api.API_apply_judge)
                .build()
                .execute(new GenericsCallback<MatterApplicationActivityStatusBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(MatterApplicationActivityStatusBean response, int id) {
                        if (type == 1) {
                            dismisProgress();
                        }
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            MatterApplicationActivityStatusBean.DataBean bean = response.getData();
                            String status = bean.getStatus();
                            if (!TextUtils.isEmpty(status)) {
                                mStatus = status;
                                if (mStatus.equals("0")) {
                                    mActivityMatterApplicationStokeChick.setText("行程开始");
                                } else if (mStatus.equals("1")) {
                                    setDate(bean);
                                } else {
                                    mActivityMatterApplicationStokeChick.setText("行程开始");
                                }
                            }
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i(ConstantApi.LOG_I_NET, "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }

    private void setDate(MatterApplicationActivityStatusBean.DataBean bean) {
        mActivityMatterApplicationInputAddress.setText(bean.getApply().getAddress());
        mActivityMatterApplicationInputReson.setText(bean.getApply().getReason());
        mActivityMatterApplicationStokeChick.setText("行程结束");

    }


}
