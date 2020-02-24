package com.yinfeng.yf_trajectory.moudle.activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.caitiaobang.core.app.tools.MultipleStatusView;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.GoogleDotView;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.ChanelLeaveBean;
import com.yinfeng.yf_trajectory.moudle.bean.LeaveHistoryActivityBean;
import com.yinfeng.yf_trajectory.moudle.bean.QueryDateBaseActivityBean;
import com.yinfeng.yf_trajectory.moudle.bean.expanded.ApplicationRecordActivityBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.activity
 * 类  名：LeaveHistoryActivity
 * 创建人：liuguodong
 * 创建时间：2019/11/27 14:54
 * ============================================
 **/
public class LeaveHistoryActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_query_leave_layout;
    }

    @Override
    protected void initData() {
        setTitle("请假记录");
        requestDate(0);
    }

    @Override
    protected void initView() {
        super.initView();
        init();
    }

    private void requestDate(final int type) {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }

        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("token = null ");
            return;
        }
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
                .url(Api.commonleaveList)
                .build()
                .execute(new GenericsCallback<LeaveHistoryActivityBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(LeaveHistoryActivityBean response, int id) {
                        if (type == 1) {
                            dismisProgress();
                        }
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.getData().size() > 0) {
                            setAdapter(response);
                            mTestMultipleStatusView.showContent();
                        } else {
//                            showToastC(response.getMessage());
                            mTestMultipleStatusView.showEmpty();
                        }
                        Log.i("testre", "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }

    private void setAdapter(LeaveHistoryActivityBean response) {

        homeAdapter = new HomeAdapter(R.layout.ri_leaveing_list_item, response.getData());
        homeAdapter.openLoadAnimation();
        mTestRecyclerview.setAdapter(homeAdapter);
        homeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.ri_leaveing_list_item_btn:
                        ;
                        MessageDialog.show(LeaveHistoryActivity.this, "提示", "取消请假？", "是", "否", "")
                                .setButtonOrientation(LinearLayout.VERTICAL).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                getCancelLeave();

                                return false;
                            }
                        });
                        break;
                }
            }
        });
    }

    private RecyclerView mTestRecyclerview;
    private MultipleStatusView mTestMultipleStatusView;
    private TwinklingRefreshLayout mTestRefreshLayout;
    private HomeAdapter homeAdapter;

    private void init() {
        mTestRecyclerview = (RecyclerView) findViewById(R.id.include_recyclerview);
        mTestMultipleStatusView = findViewById(R.id.include_multiple_status_view);
        mTestRefreshLayout = (TwinklingRefreshLayout) findViewById(R.id.include_refreshLayout);
//        CostomRefreshView headerView = new CostomRefreshView(mContext);
        GoogleDotView headerView = new GoogleDotView(mContext);
//        headerView.setArrowResource(R.drawable.ic_costom_refalsh);
//        headerView.setTextColor(0xff745D5C);
        mTestRefreshLayout.setHeaderView(headerView);
        //底部
        LoadingView loadingView = new LoadingView(mContext);
        mTestRefreshLayout.setBottomView(loadingView);
        //设置重试视图点击事件
        mTestMultipleStatusView.setOnRetryClickListener(mRetryClickListener);

        mTestRefreshLayout.setOnRefreshListener(refreshListenerAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        //设置布局管理器
        mTestRecyclerview.setLayoutManager(layoutManager);
        //设置增加或删除条目的动画
        mTestRecyclerview.setItemAnimator(new DefaultItemAnimator());
        mTestRefreshLayout.setEnableRefresh(false);
        mTestRefreshLayout.setEnableLoadmore(false);
    }

    final View.OnClickListener mRetryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loading();
        }
    };

    void loading() {
        mTestMultipleStatusView.showLoading();
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后重试");
            mTestMultipleStatusView.showNoNetwork();
            return;
        }

        requestDate(1);

    }

    RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
            if (!NetworkUtils.isConnected()) {
                showToastC("网络无链接,请稍后在试");
                refreshLayout.finishRefreshing();
                return;
            }
            requestDate(0);
        }

        @Override
        public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
            refreshLayout.finishLoadmore();
        }
    };


    //适配器
    private class HomeAdapter extends BaseItemDraggableAdapter<LeaveHistoryActivityBean.DataBean, HomeAdapter.MyHoudle> {

        public HomeAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(HomeAdapter.MyHoudle helper, LeaveHistoryActivityBean.DataBean d) {

            helper.addOnClickListener(R.id.ri_leaveing_list_item_btn);
            helper.setText(R.id.ri_leaveing_list_item_matter, d.getMatter() == "" ? "暂无数据" : "申请原因：" + d.getMatter());

            helper.setText(R.id.ri_leaveing_list_item_start_time, d.getStartTime() == "" ? "暂无数据" : "开始时间：" + d.getStartTime());
            helper.setText(R.id.ri_leaveing_list_item_end_time, d.getStartTime() == "" ? "暂无数据" : "结束时间：" + d.getEndTime());


            String status = d.getState();
            String statusValue = "";
            if (!TextUtils.isEmpty(status)) {
                if (status.equals("2")) {
                    statusValue = "请假中";
                } else if (status.equals("1")) {
                    statusValue = "未开始";
                } else if (status.equals("0")) {
                    statusValue = "已结束";
                    helper.setGone(R.id.ri_leaveing_list_item_btn, false);
                }else if (status.equals("3")) {
                    statusValue = "已取消";
                    helper.setGone(R.id.ri_leaveing_list_item_btn, false);
                }

            } else {
                statusValue = "接口异常";
            }
            helper.setText(R.id.ri_leaveing_list_item_statusValue, statusValue);


        }

        class MyHoudle extends BaseViewHolder {
            public MyHoudle(View view) {
                super(view);
            }
        }
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
                            requestDate(0);
                            LattePreference.saveKey(ConstantApi.leave_time_status, "0");
                            LattePreference.saveKey(ConstantApi.work_time_status, "1");
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i("testre", "请求结果：是否是工作日" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }

}
