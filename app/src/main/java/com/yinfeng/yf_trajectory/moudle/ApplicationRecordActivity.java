package com.yinfeng.yf_trajectory.moudle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.caitiaobang.core.app.app.BaseActivity;
import com.yinfeng.yf_trajectory.R;

/**
 * ============================================
 * 描  述：申请记录
 * 包  名：com.yinfeng.yf_trajectory.moudle
 * 类  名：ApplicationRecordActivity
 * 创建人：liuguodong
 * 创建时间：2019/8/2 16:24
 * ============================================
 **/
public class ApplicationRecordActivity extends BaseActivity {


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_application_record;
    }

    @Override
    protected void initData() {
        setTitle("申请记录");
    }

    @Override
    protected void initView() {
        super.initView();

    }
}
