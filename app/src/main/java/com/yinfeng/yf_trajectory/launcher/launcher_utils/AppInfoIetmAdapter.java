package com.yinfeng.yf_trajectory.launcher.launcher_utils;

import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.activity.LeaveHistoryActivity;
import com.yinfeng.yf_trajectory.moudle.bean.LeaveHistoryActivityBean;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.launcher.launcher_utils
 * 类  名：AppInfoIetmAdapter
 * 创建人：liuguodong
 * 创建时间：2020/3/24 15:58
 * ============================================
 **/
public class AppInfoIetmAdapter extends BaseQuickAdapter<AppUtils.AppInfo, AppInfoIetmAdapter.MyHoudle> {

    public AppInfoIetmAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(AppInfoIetmAdapter.MyHoudle helper, AppUtils.AppInfo d) {
        if (d.getIcon() != null) {
            helper.setImageDrawable(R.id.app_info_item_img, d.getIcon());
            helper.addOnClickListener(R.id.app_info_item_group);
            helper.setText(R.id.app_info_item_name, d.getName());
        }
    }

    class MyHoudle extends BaseViewHolder {
        public MyHoudle(View view) {
            super(view);
        }
    }
}
