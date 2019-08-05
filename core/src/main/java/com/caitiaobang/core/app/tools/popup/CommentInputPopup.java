package com.caitiaobang.core.app.tools.popup;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;


import com.caitiaobang.core.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2018/10/8. 评论
 */
public class CommentInputPopup extends BasePopupWindow {
    EditText mEditText;

    public CommentInputPopup(final Context context) {
        super(context);
        mEditText = findViewById(R.id.ed_input);
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.ToastMessage(getContext(), mEditText.getText().toString());
                if (chickListioner != null) {
                  String contetn=  mEditText.getText().toString();
                  if (TextUtils.isEmpty(contetn)){
                      Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
                      return;
                  }
                    chickListioner.ChickPos(contetn);
                }
                dismiss();
            }
        });
        setAutoShowInputMethod(mEditText, true);
        setPopupGravity(Gravity.BOTTOM);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 350);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 350);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_slide_from_bottom_with_input);
    }

    public void setOnChickListiner(ChickListioner c) {
        chickListioner = c;
    }

    ChickListioner chickListioner;

    public interface ChickListioner {
        void ChickPos(String content);
    }
}
