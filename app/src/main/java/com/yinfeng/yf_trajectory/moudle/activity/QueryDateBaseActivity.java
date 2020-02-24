package com.yinfeng.yf_trajectory.moudle.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.app.BaseApplication;
import com.caitiaobang.core.app.bean.GreendaoLocationBean;
import com.caitiaobang.core.app.tools.MultipleStatusView;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.caitiaobang.core.greendao.gen.DaoSession;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.LoadingView;
import com.lcodecore.tkrefreshlayout.header.GoogleDotView;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.QueryDateBaseActivityBean;
import com.yinfeng.yf_trajectory.moudle.utils.TimeUtilsEmum;

import java.util.ArrayList;
import java.util.List;

public class QueryDateBaseActivity extends BaseActivity {


    private TextView mActivityQueryDateBaseTxt;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_query_date_base;
    }

    @Override
    protected void initData() {
        setTitle("数据库查看");
        requestDate(0);
    }

    @Override
    protected void initView() {
        super.initView();
        init();
        mActivityQueryDateBaseTxt = (TextView) findViewById(R.id.activity_query_date_base_txt);
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

    }


    private void setAdapter() {

        List<GreendaoLocationBean> mList = queryLoactionDate();
        List<QueryDateBaseActivityBean.DataBean> mListNew = new ArrayList<>();
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                QueryDateBaseActivityBean.DataBean bean = new QueryDateBaseActivityBean.DataBean();
                bean.setAddress(mList.get(i).getAddress() + "");
                bean.setLat(mList.get(i).getLat() + "");
                bean.setLng(mList.get(i).getLng() + "");
                bean.setTime(mList.get(i).getTime() + "");
                mListNew.add(bean);
            }

            mActivityQueryDateBaseTxt.setText("数据库数量 ：" + mListNew.size());
            homeAdapter = new HomeAdapter(R.layout.ai_query_datebase_layout, mListNew);
            homeAdapter.openLoadAnimation();
            homeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);

            mTestRecyclerview.setAdapter(homeAdapter);
            mTestMultipleStatusView.showContent();
        } else {
            mTestMultipleStatusView.showEmpty();
            return;
        }
        mTestRefreshLayout.finishRefreshing();
        mTestRefreshLayout.finishLoadmore();


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
    private class HomeAdapter extends BaseItemDraggableAdapter<QueryDateBaseActivityBean.DataBean, HomeAdapter.MyHoudle> {

        public HomeAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(MyHoudle helper, QueryDateBaseActivityBean.DataBean d) {

//            helper.addOnClickListener(R.id.ai_channnel_activity_item_g);
            helper.setText(R.id.ai_query_datebase_layout_txt, d.getLat() == "" ? "暂无数据" : "lat: " + d.getLat() + "lng: " + d.getLng() + "addr : " + d.getAddress() + "time: " + d.getTime()
                    +'\n'+" Accuracy: "+d.getAccuracy()+" Provider: "+d.getProvider()+" Speed: "+d.getSpeed());

            if (!TextUtils.isEmpty(d.getTime())) {
                String mTimers= TimeUtils.millis2String(Long.parseLong(d.getTime()));
                String min = ConmonUtils.CalculateTime(mTimers);
            helper.setText(R.id.ai_query_datebase_layout_time, d.getTime() == "" ? "暂无数据" : mTimers + "  " + min);
            }
//            Glide.with(mContext).load(Api.APP_IMG + d.getImgUrl()).transform(new CenterCrop(mContext), new GlideRoundTransform(mContext, 5)).placeholder(R.drawable.ic_glide_placeholder)
//                    .error(R.drawable.ic_glide_error).dontAnimate().into((ImageView) helper.convertView.findViewById(R.id.ai_channnel_activity_item_img));
        }

        class MyHoudle extends BaseViewHolder {
            public MyHoudle(View view) {
                super(view);
            }
        }
    }

    void requestDate(int type) {
        setAdapter();
    }

    /**
     * 数据库查询
     */

    private List queryLoactionDate() {
        DaoSession daoSession = BaseApplication.getDaoInstant();
        try {
            return daoSession.queryBuilder(GreendaoLocationBean.class).list();
        } catch (Exception e) {
           Log.i("testre","green err " + e.toString());
            showToast("green err " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
}
