package com.caitiaobang.core.app.tools.recyclerview;

/**
 * ============================================
 * 描  述：
 * 包  名：com.lgd.conmoncore.tools.recycler
 * 类  名：OnViewPagerListener
 * 创建人：lgd
 * 创建时间：2019/5/13 15:16
 * ============================================
 **/
public interface OnViewPagerListener {

    /*初始化完成*/
    void onInitComplete();

    /*释放的监听*/
    void onPageRelease(boolean isNext, int position);

    /*选中的监听以及判断是否滑动到底部*/
    void onPageSelected(int position, boolean isBottom);


}