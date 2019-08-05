package com.caitiaobang.core.app.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MToast;
import com.tbruyelle.rxpermissions2.RxPermissions;


public abstract class BaseFragment extends android.support.v4.app.Fragment {
    protected View mRoot;
    protected PlaceHolderView mPlaceHolderView;
    // 标示是否第一次初始化数据
    protected boolean mIsFirstInitData = true;
    public Context mContext;
    public RxPermissions rxPermissions;
    public ActResultRequest actResultRequest;

    public void setIntentFinsh(String msg, int resultCode) {
        Intent intent = new Intent();
        intent.putExtra("JUMP_INTENT_KEY", msg);
        getActivity().setResult(resultCode, intent);
        getActivity().finish();
    }

    public void setIntentFinsh(int msg, int resultCode) {
        Intent intent = new Intent();
        intent.putExtra("JUMP_INTENT_KEY", msg);
        getActivity().setResult(resultCode, intent);
        getActivity().finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 初始化参数
        initArgs(getArguments());
        mContext = getActivity();
        rxPermissions = new RxPermissions(this);
        actResultRequest = new ActResultRequest(getActivity());

    }

    public final RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前的跟布局，但是不在创建时就添加到container里边
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            initWidget(root,savedInstanceState);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                // 把当前Root从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    /**
     * 通用findViewById,减少重复的类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) mRoot.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData) {
            // 触发一次以后就不会触发
            mIsFirstInitData = false;
            // 触发
            onFirstInit();
        }

        // 当View创建完成后初始化数据
        initData();
    }

    /**
     * 初始化相关参数
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    @LayoutRes
    protected abstract int getContentLayoutId();

//    public RequestOptions mGlideOptions;

    /**
     * 初始化控件
     */

    protected void initWidget(View root) {

    }

    protected void initWidget(View root, Bundle savedInstanceState) {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 当首次初始化数据的时候会调用的方法
     */
    protected void onFirstInit() {

    }

    /**
     * 返回按键触发时调用
     *
     * @return 返回True代表我已处理返回逻辑，Activity不用自己finish。
     * 返回False代表我没有处理逻辑，Activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }

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

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }


}