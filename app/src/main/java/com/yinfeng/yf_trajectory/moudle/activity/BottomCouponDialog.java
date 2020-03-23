package com.yinfeng.yf_trajectory.moudle.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.BottomCouponDialogBean;
import com.yinfeng.yf_trajectory.moudle.bean.LeaveHistoryActivityBean;
import com.yinfeng.yf_trajectory.moudle.utils.BaseDialogFragment;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import okhttp3.Call;


/**
 * ============================================
 * 描  述：优惠券底部弹出
 * 包  名：com.yinfeng.yfhx.ui.wheight
 * 类  名：BottomCouponDialog
 * 创建人：liuguodong
 * 创建时间：2019/10/21 17:00
 * ============================================
 **/
public class BottomCouponDialog extends BaseDialogFragment implements View.OnClickListener {
    private RecyclerView mIncludeRecyclerview;
    /**
     * 取消
     */
    private TextView mDfCouponDialogLayoutCencle;
    /**
     * 确认
     */
    private TextView mDfCouponDialogLayoutConfirm;


    @Override
    protected int getContentLayoutId() {
        return R.layout.df_coupon_dialog_layout;
    }

    @Override
    protected int getContentGravity() {
        return Gravity.CENTER_HORIZONTAL;
    }

    @Override
    protected void initView(View view) {
        initRecyclerview(view);
        getMatterInfo();
        mDfCouponDialogLayoutCencle = (TextView) view.findViewById(R.id.df_coupon_dialog_layout_cencle);
        mDfCouponDialogLayoutCencle.setOnClickListener(this);
        mDfCouponDialogLayoutConfirm = (TextView) view.findViewById(R.id.df_coupon_dialog_layout_confirm);
        mDfCouponDialogLayoutConfirm.setOnClickListener(this);
    }

    private GroupAdapter groupAdapter;

    private void initRecyclerview(View view) {
        mIncludeRecyclerview = (RecyclerView) view.findViewById(R.id.include_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mIncludeRecyclerview.setLayoutManager(layoutManager);
        mIncludeRecyclerview.setItemAnimator(new DefaultItemAnimator());
        //消除嵌套卡顿
//        mIncludeRecyclerview.setNestedScrollingEnabled(false);

    }

    private void setAdapter(LeaveHistoryActivityBean dataBean) {

        groupAdapter = new GroupAdapter(R.layout.ri_group_item, dataBean.getData());
        groupAdapter.openLoadAnimation();
        mIncludeRecyclerview.setAdapter(groupAdapter);
        groupAdapter.setOnItemChildClickListener(listener);
    }

    BaseQuickAdapter.OnItemChildClickListener listener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (view.getId()) {
                case R.id.ri_group_item_group:
                    List<BottomCouponDialogBean.DataBean> datas = adapter.getData();
                    BottomCouponDialogBean.DataBean bean = (BottomCouponDialogBean.DataBean) adapter.getData().get(position);
                    for (BottomCouponDialogBean.DataBean data : datas) {
                        data.setSelected(false);
                    }
                    bean.setSelected(true);
                    groupAdapter.notifyDataSetChanged();
                    break;
                default:
            }
            String mName = groupAdapter.getData().get(position).getName();
            showToastC(mName);
        }
    };

    private void showToastC(String msg) {
        Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
    }

    private void getMatterInfo() {

        OkHttpUtils
                .get()
//                .addHeader("track-token", token)
                .url(Api.commongetMatter)
                .build()
                .execute(new GenericsCallback<LeaveHistoryActivityBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试");
                    }

                    @Override
                    public void onResponse(LeaveHistoryActivityBean response, int id) {

                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            setAdapter(response);
                            showToastC(response.getMessage());
                        } else {
                            showToastC(response.getMessage());
                        }
                        Logger.i("请求结果：上传信息" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.df_coupon_dialog_layout_cencle:
                this.dismiss();
                break;
            case R.id.df_coupon_dialog_layout_confirm:
                this.dismiss();
                break;
        }
    }
}
