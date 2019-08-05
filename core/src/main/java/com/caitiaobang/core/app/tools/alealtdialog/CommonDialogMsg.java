package com.caitiaobang.core.app.tools.alealtdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caitiaobang.core.R;


/**
 * Created by LiuGuoDong on 2017/8/4.
 *
 * @author LiuGuoDong
 */

public class CommonDialogMsg {

    public static class Builder {

        private Context mContext;
        private Dialog mDialog;
        private ViewHolder mViewHolder;

        private View mView;
        private boolean hasPos = false, hasNeg = false;

        public Builder(Activity context) {
            mContext = context;
            initView();
        }

        public Builder setTitle(CharSequence msg1,CharSequence msg2,CharSequence msg3) {
            mViewHolder.tvMessage1.setText(msg1);
            mViewHolder.tvMessage2.setText(msg2);
            mViewHolder.tvMessage3.setText(msg3);
            return this;
        }

        public Builder setTitle(CharSequence msg1,CharSequence msg2,CharSequence msg3, int color) {

            mViewHolder.tvMessage1.setText(msg1);
            mViewHolder.tvMessage2.setText(msg2);
            mViewHolder.tvMessage3.setText(msg3);
            mViewHolder.tvMessage1.setTextColor(ContextCompat.getColor(mContext, color));
            mViewHolder.tvMessage2.setTextColor(ContextCompat.getColor(mContext, color));
            mViewHolder.tvMessage3.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setTitle(int resid) {
            mViewHolder.tvMessage1.setText(resid);
            mViewHolder.tvMessage2.setText(resid);
            mViewHolder.tvMessage3.setText(resid);
            return this;
        }

        public Builder setTitle(int resid, int color) {
            mViewHolder.tvMessage1.setText(resid);
            mViewHolder.tvMessage2.setText(resid);
            mViewHolder.tvMessage3.setText(resid);
            mViewHolder.tvMessage1.setTextColor(ContextCompat.getColor(mContext, color));
            mViewHolder.tvMessage2.setTextColor(ContextCompat.getColor(mContext, color));
            mViewHolder.tvMessage3.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setOnClickListenerPositions( final View.OnClickListener listener) {
            mViewHolder.tvPositiveButton.setVisibility(View.VISIBLE);
            hasPos = true;

            mViewHolder.tvMessage1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
            return this;
        }
        public Builder setOnClickListenerPositions2( final View.OnClickListener listener) {
            mViewHolder.tvPositiveButton.setVisibility(View.VISIBLE);
            hasPos = true;

            mViewHolder.tvMessage2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
            return this;
        }
        public Builder setOnClickListenerPositions3( final View.OnClickListener listener) {
            mViewHolder.tvPositiveButton.setVisibility(View.VISIBLE);
            hasPos = true;

            mViewHolder.tvMessage3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
            mViewHolder.tvPositiveButton.setVisibility(View.VISIBLE);
            hasPos = true;
            mViewHolder.tvPositiveButton.setText(text);
            mViewHolder.tvPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final View.OnClickListener listener, int color) {
            mViewHolder.tvPositiveButton.setVisibility(View.VISIBLE);
            hasPos = true;
            mViewHolder.tvPositiveButton.setText(text);
            mViewHolder.tvPositiveButton.setTextColor(ContextCompat.getColor(mContext, color));
            mViewHolder.tvPositiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    if (listener != null) {
                        listener.onClick(view);
                    }
                }
            });
            return this;
        }


        public Builder setCancelable(boolean flag) {
            mDialog.setCancelable(flag);
            return this;
        }

        public Builder setOnListener(boolean flag) {
            mDialog.setCancelable(flag);
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean flag) {
            mDialog.setCanceledOnTouchOutside(flag);
            return this;
        }

        public Builder setFouceable(boolean flag) {
            mDialog.setCanceledOnTouchOutside(flag);
            return this;
        }

        public Dialog create() {
            return mDialog;
        }

        public void show(boolean is) {
            if (mDialog != null) {
                if (hasPos || hasNeg) {
//                    mViewHolder.line1.setVisibility(View.VISIBLE);
                }
                if (hasPos && hasNeg) {
//                    mViewHolder.line2.setVisibility(View.VISIBLE);
                }

                mDialog.show();
            }
        }

        public void dismiss() {
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }

        private void initView() {
            mDialog = new Dialog(mContext, DialogUtils.getStyle());
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_easy_dialog_msg, null);
            mViewHolder = new ViewHolder(mView);
            mDialog.setContentView(mView);

            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = (int) (dm.widthPixels * 0.8);
            mDialog.getWindow().setAttributes(lp);
        }


        class ViewHolder {

            TextView tvMessage1;
            TextView tvMessage2;
            TextView tvMessage3;
            TextView tvPositiveButton ;
            LinearLayout vgLayout;


            public ViewHolder(View view) {

                tvMessage1 = view.findViewById(R.id.conmon_dialog_msg_txt1);
                tvMessage2 = view.findViewById(R.id.conmon_dialog_msg_txt2);
                tvMessage3 = view.findViewById(R.id.conmon_dialog_msg_txt3);
                tvPositiveButton = view.findViewById(R.id.dialog_positive_msg);

                vgLayout = view.findViewById(R.id.dialog_layout_msg);

            }
        }

    }

}
