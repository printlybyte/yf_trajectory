package com.caitiaobang.core.app.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MToast;


/**
 * ============================================
 * 描  述：
 * 包  名：com.wlkj.tanyanmerchants.module.activity.home
 * 类  名：BasePageFragment
 * 创建人：lgd
 * 创建时间：2018/11/13 9:27
 * ============================================
 **/
public abstract class BasePageFragment extends Fragment {

    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;
    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }



    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//
//    }

    /**
     * @param msg 提示信息
     * @ author mac  @times 2018/7/22  下午4:03
     */
    public void showToast(String msg) {
        MToast.makeTextShort(mContext, "" + msg);
    }

    /**
     * @ author mac  @times 2018/7/22  下午4:12
     * 默认提示数据是加载中
     */
    public void showProgress() {
        MProgressDialog.showProgress(mContext);
    }


    public void showToastC(String msg) {
        MToast.makeTextLong(mContext, msg);
    }


    /**
     * @param msg 自定义加载中信息
     * @ author mac  @times 2018/7/22  下午4:12
     */
    public void showProgress(String msg) {
        MProgressDialog.showProgress(mContext, msg + "");
    }

    /**
     * @ author mac  @times 2018/7/22  下午4:12
     * 取消加载
     */
    public void dismisProgress() {
        MProgressDialog.dismissProgress();
    }

}

