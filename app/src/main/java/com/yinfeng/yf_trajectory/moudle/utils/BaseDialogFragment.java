package com.yinfeng.yf_trajectory.moudle.utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yinfeng.yf_trajectory.R;


/**
 * ============================================
 * 描  述：
 * 包  名：com.lgd.lgd_core.ui.dialogFragment
 * 类  名：BaseDialogFragment
 * 创建人：liuguodong
 * 创建时间：2019/10/12 16:03
 * ============================================
 **/
public abstract class BaseDialogFragment extends DialogFragment {
    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
//        window.setBackgroundDrawableResource(R.color.white);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity =getContentGravity();
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加这句话去掉自带的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        View rootView = inflater.inflate(getContentLayoutId(), null);
        getDialog().setCanceledOnTouchOutside(false);
        initView(rootView);
        // 设置宽度为屏宽、靠近屏幕底部。
        return rootView;
    }

    protected abstract int getContentLayoutId();
    protected abstract int getContentGravity();

    protected abstract void initView(View view);

    public interface OnDialogListener {
        void onDialogClick(int type);
    }

    public void setOnDialogListener(OnDialogListener dialogListener) {
        this.mlistener = dialogListener;
    }

    public OnDialogListener mlistener;
}
