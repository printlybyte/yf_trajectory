package com.caitiaobang.core.app.tools.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import com.caitiaobang.core.R;

import razerdp.basepopup.BasePopupWindow;


/**
 * Created by 大灯泡 on 2016/1/15.
 * 从底部滑上来的popup
 */
public class SlideFromBottomPopup_ios_4 extends BasePopupWindow implements View.OnClickListener {


    public SlideFromBottomPopup_ios_4(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        bindEvent();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_slide_from_bottom_ios_4);
    }

    private void bindEvent() {
        findViewById(R.id.popup_slide_from_bottom_btn1).setOnClickListener(this);
        findViewById(R.id.popup_slide_from_bottom_btn2).setOnClickListener(this);
        findViewById(R.id.popup_slide_from_bottom_btn3).setOnClickListener(this);
        findViewById(R.id.popup_slide_from_bottom_btn4).setOnClickListener(this);
        findViewById(R.id.popup_slide_from_bottom_cencel).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        int selectedPos = 0;
        if (i == R.id.popup_slide_from_bottom_btn1) {
            selectedPos = 1;
        } else if (i == R.id.popup_slide_from_bottom_btn2) {
            selectedPos = 2;
        } else if (i == R.id.popup_slide_from_bottom_btn3) {
            selectedPos = 3;
        } else if (i == R.id.popup_slide_from_bottom_btn4) {
            selectedPos = 4;
        } else if (i == R.id.popup_slide_from_bottom_cencel) {
            selectedPos = 5;
        }
        if (mOnListPopupItemClickListener != null) {
            mOnListPopupItemClickListener.onItemClick(selectedPos);
        }
        this.dismiss();
    }

    private OnListPopupItemClickListener mOnListPopupItemClickListener;

    public OnListPopupItemClickListener getOnListPopupItemClickListener() {
        return mOnListPopupItemClickListener;
    }

    public void setOnListPopupItemClickListener(OnListPopupItemClickListener onListPopupItemClickListener) {
        mOnListPopupItemClickListener = onListPopupItemClickListener;
    }

    public interface OnListPopupItemClickListener {
        void onItemClick(int what);
    }

}
