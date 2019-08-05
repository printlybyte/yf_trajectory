package com.caitiaobang.core.app.tools.loadingAnim;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.caitiaobang.core.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * ============================================
 * 描  述：
 * 包  名：com.caitiaobang.pro.activity.utils
 * 类  名：BaseAvLoadingAnim
 * 创建人：liuguodong
 * 创建时间：2019/7/13 16:44
 * ============================================
 **/
public class BaseAvLoadingAnim extends DialogFragment {


    private AVLoadingIndicatorView mAvLoadingLayoutAvi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加这句话去掉自带的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        View rootView = inflater.inflate(R.layout.av_loading_layout, null);
        mAvLoadingLayoutAvi = (AVLoadingIndicatorView) rootView.findViewById(R.id.av_loading_layout_avi);
        Log.i("TESTRE","onCreateViewonCreateViewonCreateView");
        // 设置宽度为屏宽、靠近屏幕底部。
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
//        window.setBackgroundDrawableResource(R.color.white);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

    }


    public void startAnim() {
        mAvLoadingLayoutAvi.show();
        // or avi.smoothToShow();
    }

    public void stopAnim() {
        mAvLoadingLayoutAvi.hide();
        // or avi.smoothToHide();
    }


    public interface OnDialogListener {
        void onDialogClick(String person);
    }

    public void setOnDialogListener(OnDialogListener dialogListener) {
        this.mlistener = dialogListener;
    }

    private OnDialogListener mlistener;

    private static final String[] INDICATORS = new String[]{
            "BallPulseIndicator",
            "BallGridPulseIndicator",
            "BallClipRotateIndicator",
            "BallClipRotatePulseIndicator",
            "SquareSpinIndicator",
            "BallClipRotateMultipleIndicator",
            "BallPulseRiseIndicator",
            "BallRotateIndicator",
            "CubeTransitionIndicator",
            "BallZigZagIndicator",
            "BallZigZagDeflectIndicator",
            "BallTrianglePathIndicator",
            "BallScaleIndicator",
            "LineScaleIndicator",
            "LineScalePartyIndicator",
            "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator",
            "BallBeatIndicator",
            "LineScalePulseOutIndicator",
            "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator",
            "BallScaleRippleMultipleIndicator",
            "BallSpinFadeLoaderIndicator",
            "LineSpinFadeLoaderIndicator",
            "TriangleSkewSpinIndicator",
            "PacmanIndicator",
            "BallGridBeatIndicator",
            "SemiCircleSpinIndicator",
            "com.wang.avi.sample.MyCustomIndicator"
    };


}
