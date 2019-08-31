package com.yinfeng.yf_trajectory.moudle.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.tools.MultipleStatusView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.GoogleDotView;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.MatterApplicationActivityBean;
import com.yinfeng.yf_trajectory.moudle.bean.expanded.ApplicationRecordActivityBean;
import com.yinfeng.yf_trajectory.moudle.bean.expanded.Level0Item;
import com.yinfeng.yf_trajectory.moudle.bean.expanded.Level1Item;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * ============================================
 * 描  述：申请记录
 * 包  名：com.yinfeng.yf_trajectory.moudle
 * 类  名：ApplicationRecordActivity
 * 创建人：liuguodong
 * 创建时间：2019/8/2 16:24
 * <p>
 * ============================================
 **/
public class ApplicationRecordActivity extends BaseActivity {
    private ArrayList<MultiItemEntity> list;
    private RecyclerView mTestRecyclerview;
    private MultipleStatusView mTestMultipleStatusView;
    private TwinklingRefreshLayout mTestRefreshLayout;

    private HomeAdapter homeAdapter;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_application_record;
    }

    @Override
    protected void initData() {
        setTitle("申请记录");
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        //设置布局管理器
        mTestRecyclerview.setLayoutManager(layoutManager);
        //设置增加或删除条目的动画
        mTestRecyclerview.setItemAnimator(new DefaultItemAnimator());
        //设置分隔线
//        mTestRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, OrientationHelper.VERTICAL));
        requestDate(1, "查询中...", mStartTimes, mEndTimes);
    }

    private String mStartTimes = "2019-07-14 13:39:43";
    private String mEndTimes = "2019-08-07 13:39:43";

    @Override
    protected void initView() {
        super.initView();
        mTestRecyclerview = (RecyclerView) findViewById(R.id.include_recyclerview);
        mTestMultipleStatusView = findViewById(R.id.include_multiple_status_view);
        mTestRefreshLayout = (TwinklingRefreshLayout) findViewById(R.id.include_refreshLayout);
        GoogleDotView headerView = new GoogleDotView(mContext);
//        headerView.setArrowResource(R.drawable.ic_costom_refalsh);
//        headerView.setTextColor(0xff745D5C);
        mTestRefreshLayout.setHeaderView(headerView);
        //底部
        LoadingView loadingView = new LoadingView(mContext);
        mTestRefreshLayout.setBottomView(loadingView);
        //设置重试视图点击事件
        mTestMultipleStatusView.setOnRetryClickListener(mRetryClickListener);
        if (!NetworkUtils.isConnected()) {
            mTestMultipleStatusView.showNoNetwork();
        }
        mTestRefreshLayout.setOnRefreshListener(refreshListenerAdapter);
    }


    private ArrayList<MultiItemEntity> generateData(ApplicationRecordActivityBean response) {


        ArrayList<MultiItemEntity> res = new ArrayList<>();
        for (int i = 0; i < response.getData().getList().size(); i++) {
            ApplicationRecordActivityBean.DataBean.ListBean bean = response.getData().getList().get(i);
            Level0Item lv0 = new Level0Item();
            lv0.setAddress("前往地点 " + bean.getAddress());
            lv0.setStatus(bean.getState());
            lv0.setStartTime("开始时间 ：" + bean.getStartTime());
            lv0.setEndTime("结束时间 ：" + bean.getEndTime());


            Level1Item lv1 = new Level1Item();
            lv1.setReson("申请原因 ：" + bean.getReason());
            lv1.setStartTime(bean.getStartTime()+"");
            lv1.setEndTime(  bean.getEndTime()+"");
            lv1.setAddress("前往地点 ：" + bean.getAddress());
            lv1.setStatus(bean.getState());

            lv0.addSubItem(lv1);
            res.add(lv0);
        }
//        res.add(new Level0Item("This is " + lv0Count + "th item in Level 0", "subtitle of " + lv0Count));
        return res;

    }

    private void setAdapter(ApplicationRecordActivityBean response) {
        list = generateData(response);
        homeAdapter = new HomeAdapter(list);
        homeAdapter.openLoadAnimation();
        mTestRecyclerview.setAdapter(homeAdapter);
        homeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
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
        requestDate(1, "查询中...", mStartTimes, mEndTimes);
    }

    RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {
        @Override
        public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
            if (!NetworkUtils.isConnected()) {
                showToastC("网络无链接,请稍后在试");
                refreshLayout.finishRefreshing();
                return;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.finishRefreshing();
                    requestDate(1, "查询中...", mStartTimes, mEndTimes);
                }
            }, 2000);
        }

        @Override
        public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
            loadMore(refreshLayout);
        }
    };

    /**
     * @datetime 2018/9/26 16:09
     * @author liuguodong
     * @parmes 加载更多
     */
    private void loadMore(final TwinklingRefreshLayout refreshLayout) {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }

        mCurrentPage++;
        if (mCurrentPage >= mPageSizeTotal) {
            mCurrentPage--;
            refreshLayout.finishLoadmore();
            showToastC("暂无更多数据");
            return;
        }

        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("token = null ");
            return;
        }


        Map<String, Object> map = new LinkedHashMap<>();
        map.clear();
//        map.put("startTime", mTimeStart);
//        map.put("endTime", mTimeEnd);
        String mNetUrl = Api.API_apply_query + "?pageNum=" + mCurrentPage + "&pageSize=" + pageSize;

        Logger.v( "API: " + Api.API_apply_query + "发送json：" + new Gson().toJson(map) + "mCurrentPage" + mCurrentPage);
        Logger.v( "token: " + token);
        OkHttpUtils
                .postString()
                .addHeader("track-token", token)
                .content(new Gson().toJson(map))
                .url(mNetUrl)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<ApplicationRecordActivityBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试");
                        mTestMultipleStatusView.showError();
                    }

                    @Override
                    public void onResponse(ApplicationRecordActivityBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            mTestMultipleStatusView.showContent();
                            homeAdapter.addData(generateData(response));
                            homeAdapter.notifyDataSetChanged();

                            mTestMultipleStatusView.showContent();
                        } else {
                            mTestMultipleStatusView.showEmpty();
                        }
                        refreshLayout.finishLoadmore();
                    }
                });
    }


    public class HomeAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
        public static final int TYPE_LEVEL_0 = 0;
        public static final int TYPE_LEVEL_1 = 1;

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        public HomeAdapter(List<MultiItemEntity> data) {
            super(data);
            addItemType(TYPE_LEVEL_0, R.layout.expand_request_recding_group);
            addItemType(TYPE_LEVEL_1, R.layout.expand_request_recding_child);
        }

        @Override
        protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
            switch (holder.getItemViewType()) {
                case TYPE_LEVEL_0:
                    final Level0Item lv0 = (Level0Item) item;
                    holder.setText(R.id.expand_request_recding_group_title, lv0.address)
                            .setText(R.id.expand_request_recding_group_sub_title, lv0.startTime + "  " + lv0.endTime)
                            .setImageResource(R.id.expand_request_recding_group_sub_title_img, lv0.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getAdapterPosition();
                            Log.d(TAG, "Level 0 item pos: " + pos);
                            if (lv0.isExpanded()) {
                                collapse(pos);
                            } else {
                                expand(pos);
                            }
                        }
                    });
                    break;
                case TYPE_LEVEL_1:
                    final Level1Item lv1 = (Level1Item) item;
                    holder.setText(R.id.expand_request_recding_child_start_time,"开始时间 ：" +  lv1.startTime);
                    holder.setText(R.id.expand_request_recding_child_reson, lv1.reson);
                    if (lv1.status.equals("0")) {
                        holder.setVisible(R.id.expand_request_recding_child_query, false);
                        String str1 = "结束时间 ：<font color='#FF0000'>进行中</font>";
                        holder.setText(R.id.expand_request_recding_child_end_time, Html.fromHtml(str1));
                    } else if (lv1.status.equals("1")) {
                        //正常显示轨迹
                        holder.setVisible(R.id.expand_request_recding_child_query, true);

                        holder.setText(R.id.expand_request_recding_child_end_time,"结束时间 ：" +  lv1.endTime);
                    } else {
                        holder.setVisible(R.id.expand_request_recding_child_query, false);

                        holder.setText(R.id.expand_request_recding_child_end_time, "未知状态");

                    }
                    holder.setText(R.id.expand_request_recding_child_address, lv1.address);

                    if (lv1.status.equals("1")) {
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = holder.getAdapterPosition();
                                Intent intent = new Intent(ApplicationRecordActivity.this, ViewTrackMapActivity.class);
                                intent.putExtra(ConstantApi.INTENT_FLAG, ConstantApi.query_info);
                                intent.putExtra(ConstantApi.INTENT_KEY, lv1.startTime);
                                intent.putExtra(ConstantApi.INTENT_KEY_TWO, lv1.endTime);
                                startActivity(intent);
                            }
                        });
                    }
                    break;
            }
        }
    }

    private int mCurrentPage = 1;
    private double mPageSizeTotal;//zongshu
    private int pageSize = 15;

    private void requestDate(final int type, String msg, String mTimeStart, String mTimeEnd) {
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
        Map<String, Object> map = new LinkedHashMap<>();
        map.clear();
//        map.put("startTime", mTimeStart);
//        map.put("endTime", mTimeEnd);
        String mNetUrl = Api.API_apply_query + "?pageNum=" + "1" + "&pageSize=" + pageSize;
        Logger.v( "API: " + mNetUrl + "发送json：" + new Gson().toJson(map));
        Logger.v( "Activity token :: " +token);


        OkHttpUtils
                .postString()
                .addHeader("track-token", token)
                .content(new Gson().toJson(map))
                .url(mNetUrl)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new GenericsCallback<ApplicationRecordActivityBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(ApplicationRecordActivityBean response, int id) {
                        if (type == 1) {
                            dismisProgress();
                        }
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            setAdapter(response);
                            mCurrentPage = response.getData().getPageNum();
                            mPageSizeTotal = (response.getData().getTotal() / 15.0 + 1.0);
                            Logger.v( "mPageSizeTotal:" + mPageSizeTotal);
                            mTestMultipleStatusView.showContent();
                        } else {
                            showToastC(response.getMessage());
                            mTestMultipleStatusView.showEmpty();
                        }
                        Logger.v( "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }
}
