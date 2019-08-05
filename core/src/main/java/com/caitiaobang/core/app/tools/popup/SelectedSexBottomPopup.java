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
public class SelectedSexBottomPopup extends BasePopupWindow implements View.OnClickListener {


    public SelectedSexBottomPopup(Context context) {
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
        return createPopupById(R.layout.popup_selected_sex_bottom);
    }

    private void bindEvent() {
        findViewById(R.id.popup_selected_sex_bottom_boy).setOnClickListener(this);
        findViewById(R.id.popup_selected_sex_bottom_girl).setOnClickListener(this);
        findViewById(R.id.popup_selected_sex_bottom_confirm).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        int selectedPos = 0;
        if (i == R.id.popup_selected_sex_bottom_boy) {
            selectedPos = 1;
        } else if (i == R.id.popup_selected_sex_bottom_girl) {
            selectedPos = 2;
        }  else if (i == R.id.popup_slide_from_bottom_cencel) {
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
