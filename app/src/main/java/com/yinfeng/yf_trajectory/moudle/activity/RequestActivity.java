package com.yinfeng.yf_trajectory.moudle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.caitiaobang.core.app.tools.nice_spinner.NiceSpinner;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.google.gson.Gson;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.kongzue.dialog.v3.WaitDialog;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.LocationService;
import com.yinfeng.yf_trajectory.LocationStatusManager;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.BottomCouponDialogBean;
import com.yinfeng.yf_trajectory.moudle.bean.ChanelLeaveBean;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean;
import com.yinfeng.yf_trajectory.moudle.bean.GetWorkStatusBean;
import com.yinfeng.yf_trajectory.moudle.bean.IsLeaveStatusBean;
import com.yinfeng.yf_trajectory.moudle.utils.WorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class RequestActivity extends BaseActivity implements View.OnClickListener, OnDateSetListener {

    private NiceSpinner mActivityRequestSpiner;
    /**
     * 点击选择开始时间
     */
    private TextView mActivityRequestStartTime;
    /**
     * 点击选择结束时间
     */
    private TextView mActivityRequestEndTime;
    /**
     * 提交
     */
    private TextView mActivityRequestSave;
    /**
     * 选填
     */
    private EditText mActivityRequestReson;
    /**
     * 2019 9月
     */
    private TextView mActivityRequestDisplayTime;
    private LinearLayout mActivityRequestDisplayGroup;
    /**
     * 请假中...
     */
    private TextView mActivityRequestDisplayTitle;
    private ImageButton mActivityRequestBack;
    /**
     * 取消请假
     */
    private TextView mActivityRequestChanelLeave;

    @Override

    protected int getContentLayoutId() {
        return R.layout.activity_request;
    }

    @Override
    protected void initData() {
        setTitle("请假申请");
        getMatterInfo();
    }

    @Override
    protected void initView() {
        super.initView();

        mActivityRequestSpiner = (NiceSpinner) findViewById(R.id.activity_request_spiner);
        mActivityRequestStartTime = (TextView) findViewById(R.id.activity_request_start_time);
        mActivityRequestStartTime.setOnClickListener(this);
        mActivityRequestEndTime = (TextView) findViewById(R.id.activity_request_end_time);
        mActivityRequestEndTime.setOnClickListener(this);

        initTimeDialog();
        mActivityRequestSave = (TextView) findViewById(R.id.activity_request_save);
        mActivityRequestSave.setOnClickListener(this);
        mActivityRequestReson = (EditText) findViewById(R.id.activity_request_reson);
        mActivityRequestDisplayTime = (TextView) findViewById(R.id.activity_request_display_time);
        mActivityRequestDisplayGroup = (LinearLayout) findViewById(R.id.activity_request_display_group);
        mActivityRequestDisplayTitle = (TextView) findViewById(R.id.activity_request_display_title);
        mActivityRequestBack = (ImageButton) findViewById(R.id.activity_request_back);
        mActivityRequestBack.setOnClickListener(this);
        mActivityRequestChanelLeave = (TextView) findViewById(R.id.activity_request_chanel_leave);
        mActivityRequestChanelLeave.setOnClickListener(this);
    }

    private void getMatterInfo() {

        OkHttpUtils
                .get()
//                .addHeader("track-token", token)
                .url(Api.commongetMatter)
                .build()
                .execute(new GenericsCallback<BottomCouponDialogBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试");
                    }

                    @Override
                    public void onResponse(BottomCouponDialogBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            mActiveTypeName = new LinkedList<>();
                            for (int i = 0; i < response.getData().size(); i++) {
                                mActiveTypeName.add(response.getData().get(i).getName());
                            }
                            mActivityRequestSpiner.attachDataSource(mActiveTypeName);
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i("testre", "请求结果：上传信息" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }

    private LinkedList mActiveTypeName = null;
    private int mTimeType = 0;
    private String resonStr = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_request_start_time:
                mTimeType = 1;
                mDialogYearMonthDay.show(getSupportFragmentManager(), "1");
                break;
            case R.id.activity_request_end_time:
                mTimeType = 2;
                mDialogYearMonthDay.show(getSupportFragmentManager(), "2");

                break;
            case R.id.activity_request_save:
                String matterStr = (String) mActiveTypeName.get(mActivityRequestSpiner.getSelectedIndex());

                resonStr = mActivityRequestReson.getText().toString().trim();
                if (TextUtils.isEmpty(matterStr)) {
                    showToastC("请选择申请类别");
                    return;
                }
                if (TextUtils.isEmpty(mTimeStart)) {
                    showToastC("请选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(mTimeEnd)) {
                    showToastC("请选择结束时间");
                    return;
                }
                if (TextUtils.isEmpty(resonStr)) {
                    showToastC("请选择请假原因");
                    return;
                }
                long comparisonMinute = (mTimeEndL - mTimeStartL) / 1000 / 60;
                Log.i("TESTRE", "mTimeEndL: " + mTimeEndL + " mTimeStartL: " + mTimeStartL + " comparisonMinute: " + comparisonMinute);
                if (comparisonMinute < 30) {
                    showToastC("申请时间不得少于30分钟,请重新选择开始时间");
                    return;
                }
                if (comparisonMinute > 43200) {
                    showToastC("申请时间不得大于30天,请重新选择结束时间");
                    return;
                }
                requestDate(mTimeStart, mTimeEnd, matterStr, resonStr);

                break;
            case R.id.activity_request_back:
                finish();
                break;
            case R.id.activity_request_chanel_leave:
                getCancelLeave();
                break;
        }
    }

    private void requestDate(String startTime, String endTime, String matter, String resonStr) {
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("token = null ");
            return;
        }
        showProgress("请求中...");
//        String times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("matter", matter);
        map.put("remark", resonStr);
        String mNetUrl = Api.commonrecordOperate;
        Log.i("testre", "API: " + mNetUrl + "发送json：" + new Gson().toJson(map));
        OkHttpUtils
                .postString()
                .addHeader("track-token", token)
                .content(new Gson().toJson(map))
                .url(mNetUrl)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<ConmonBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC(e.getMessage());
                        dismisProgress();
                    }

                    @Override
                    public void onResponse(ConmonBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            showToastC(response.getMessage());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LattePreference.saveKey(ConstantApi.leave_time_status, "1");
//                                    LattePreference.saveKey(ConstantApi.leave_time_start, startTime + "");
//                                    LattePreference.saveKey(ConstantApi.leave_time_end, endTime + "");
                                    String leave_time_status = LattePreference.getValue(ConstantApi.leave_time_status);
                                    Log.i("testre", "leave_time_status: " + leave_time_status + "  " + startTime + "   " + endTime);
                                    finish();
                                }
                            });
                        } else {
                            showToastC(response.getMessage());
                        }
                        dismisProgress();
                        Log.i("testre", "请求结果 暂停事项" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }

    private String mTimeStart = "";
    private long mTimeStartL;
    private String mTimeEnd = "";
    private long mTimeEndL;
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
        Log.i("testre", "");
        if (mTimeType == 1) {
            mTimeStart = text;
            mTimeStartL = millseconds;
            mActivityRequestStartTime.setText(text);
        } else if (mTimeType == 2) {
            mTimeEnd = text;
            mTimeEndL = millseconds;
            mActivityRequestEndTime.setText(text);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommonjudgeLeave();

//        WorkUtils.getInstance().getJudgeLeave();
//        WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
//            @Override
//            public void successful(int status, WorkUtils.IBundleBean bundleBean) {
//                if (bundleBean.getIsLeaveing() == 1) {
//                    //请假中...
//
//                } else {
//
//
//                }
//            }
//
//            @Override
//            public void failure(String msg) {
//
//            }
//        });

    }

    private void getCommonjudgeLeave() {
        WaitDialog.show(RequestActivity.this, "请稍后...");

        Log.i("testre", "API: " + Api.commonjudgeLeave + " par:");
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("commonjudgeLeave token =null ");
            return;
        }
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
                .url(Api.commonjudgeLeave)
                .build()
                .execute(new GenericsCallback<IsLeaveStatusBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("commonjudgeLeave：" + e.getMessage() + "");
                        mActivityRequestDisplayGroup.setVisibility(View.VISIBLE);

                        mActivityRequestDisplayTitle.setText("异常");
                    }

                    @Override
                    public void onResponse(IsLeaveStatusBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            IsLeaveStatusBean.DataBean bean = response.getData();
                            Log.i("testre", "请求结果：上下班时间" + GsonUtils.getInstance().toJson(response));

                            if (!TextUtils.isEmpty(bean.getState())) {
                                if (bean.getState().equals("1")) {
                                    mActivityRequestDisplayTitle.setText("请假中...");
                                    mActivityRequestDisplayGroup.setVisibility(View.VISIBLE);
                                    mActivityRequestDisplayTime.setText(bean.getStartTime() + " -- " + bean.getEndTime());
                                } else if (bean.getState().equals("0")) {

                                    mActivityRequestDisplayGroup.setVisibility(View.GONE);
                                } else {
                                    mActivityRequestDisplayGroup.setVisibility(View.VISIBLE);
                                    mActivityRequestDisplayTitle.setText("异常");

                                    mActivityRequestDisplayTime.setText("获取到的status :" + bean.getState() + " 请联系后台人员");
                                }
                            } else {
                                mActivityRequestDisplayGroup.setVisibility(View.VISIBLE);
                                mActivityRequestDisplayTitle.setText("异常");
                                mActivityRequestDisplayTime.setText("获取到的status :" + bean.getState() + " 请联系后台人员");
                            }

                        } else {

                            mActivityRequestDisplayGroup.setVisibility(View.VISIBLE);
                            mActivityRequestDisplayTitle.setText("异常");
                            showToastC(response.getMessage());
                        }
                        Log.i("testre", "请求结果：请假状态" + GsonUtils.getInstance().toJson(response));

                    }
                });
        WaitDialog.dismiss();
    }


    private void getCancelLeave() {
        Log.i("testre", "API: " + Api.commonCancelLeave + " par:");
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("getCancelLeave token =null ");
            return;
        }
        OkHttpUtils
                .post()
                .addHeader("track-token", token)
                .url(Api.commonCancelLeave)
                .build()
                .execute(new GenericsCallback<ChanelLeaveBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("getCancelLeave：" + e.getMessage() + "");
                    }

                    @Override
                    public void onResponse(ChanelLeaveBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            showToastC("取消请假成功");
                            LattePreference.saveKey(ConstantApi.work_time_status, "1");
                            LattePreference.saveKey(ConstantApi.leave_time_status, "0");
                            sendServiceMsg("start");
                            finish();
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i("testre", "请求结果：是否是工作日" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }

    private void sendServiceMsg(String status) {
        Intent mIntent = new Intent(ConstantApi.service_action);
        mIntent.putExtra("event", status);
        sendBroadcast(mIntent);
    }
}
