package com.yinfeng.yf_trajectory.moudle.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.UserInfoBean;

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

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_me_info;
    }

    @Override
    protected void initData() {
        setTitle("个人中心");

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfoBean.DataBean bean = Hawk.get(ConstantApi.HK_USER_BEAN);
        if (bean != null) {
            Log.i("testr", "实体：me info ： " + new Gson().toJson(bean));
            mActivityMeInfoIdCardNum.setText(bean.getIdCard() == "" ? "无数据" : bean.getIdCard());
            mActivityMeInfoPhone.setText(bean.getPhone() == "" ? "无数据" : bean.getPhone());
            mActivityMeInfoCompany.setText(bean.getCompany() == "" ? "无数据" : bean.getCompany());
            mActivityMeInfoName.setText(bean.getName() == "" ? "无数据" : bean.getName());
            mActivityMeInfoDepartment.setText(bean.getDepartment() == "" ? "无数据" : bean.getDepartment());
            mActivityMeInfoJob.setText(bean.getJob() == "" ? "无数据" : bean.getJob());

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_me_info_request:

                ActivityUtils.startActivity(ApplicationRecordActivity.class);
                break;

        }
    }


}
