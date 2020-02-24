package com.yinfeng.yf_trajectory.moudle.activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;

import com.caitiaobang.core.app.app.Latte;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.BottomCouponDialogBean;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yfhx.fragments.adapter
 * 类  名：NavAdapter
 * 创建人：liuguodong
 * 创建时间：2019/9/24 17:42
 * ============================================
 **/
public class GroupAdapter extends BaseQuickAdapter<BottomCouponDialogBean.DataBean, GroupAdapter.MyHolder> {

    public GroupAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull MyHolder helper, BottomCouponDialogBean.DataBean item) {

//        GlideUS.loadPhoto(item.getPic(), helper.imageView);
        String tipStr = "异常";
        helper.setText(R.id.ri_group_item_name, item.getName() == "" ? tipStr : item.getName());
        helper.addOnClickListener(R.id.ri_group_item_group);

        if (item.isSelected()) {
            helper.setBackgroundColor(R.id.ri_group_item_name, Color.parseColor("#ffffff"));
            helper.setTextColor(R.id.ri_group_item_name, Color.parseColor("#ec2587"));
        } else {
            helper.setBackgroundColor(R.id.ri_group_item_name, Color.parseColor("#fafafa"));
            helper.setTextColor(R.id.ri_group_item_name, Color.parseColor("#000000"));
        }
    }

    class MyHolder extends BaseViewHolder {
        public MyHolder(View view) {
            super(view);
        }
    }
}
