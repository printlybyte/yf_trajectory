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

public class CommonDialogTxt {


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

        public Builder setTitle(CharSequence title) {
            mViewHolder.tvTitle.setText(title);
            return this;
        }

        public Builder setTitle(CharSequence title, int color) {
            mViewHolder.tvTitle.setText(title);
            mViewHolder.tvTitle.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setTitle(int resid) {
            mViewHolder.tvTitle.setText(resid);
            return this;
        }

        public Builder setTitle(int resid, int color) {
            mViewHolder.tvTitle.setText(resid);
            mViewHolder.tvTitle.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setMessage(CharSequence title) {
            mViewHolder.tvMessage.setText(title);
            return this;
        }

        public Builder setMessage(CharSequence title, int color) {
            mViewHolder.tvMessage.setText(title);
            mViewHolder.tvMessage.setTextColor(ContextCompat.getColor(mContext, color));
            return this;
        }

        public Builder setMessage(int resid) {
            mViewHolder.tvMessage.setText(resid);
            return this;
        }

        public Builder setMessage(int resid, int color) {
            mViewHolder.tvMessage.setText(resid);
            mViewHolder.tvMessage.setTextColor(ContextCompat.getColor(mContext, color));
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

        public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
            mViewHolder.tvNegativeButton.setVisibility(View.VISIBLE);
            hasNeg = true;
            mViewHolder.tvNegativeButton.setText(text);
            mViewHolder.tvNegativeButton.setOnClickListener(new View.OnClickListener() {
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

        public Builder setNegativeButton(CharSequence text, final View.OnClickListener listener, int color) {
            mViewHolder.tvNegativeButton.setVisibility(View.VISIBLE);
            hasNeg = true;
            mViewHolder.tvNegativeButton.setText(text);
            mViewHolder.tvNegativeButton.setTextColor(ContextCompat.getColor(mContext, color));
            mViewHolder.tvNegativeButton.setOnClickListener(new View.OnClickListener() {
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
                if (is) {
                    mViewHolder.tvNegativeButton.setVisibility(View.VISIBLE);
                    mViewHolder.viewline.setVisibility(View.VISIBLE);
                } else {
                    mViewHolder.tvNegativeButton.setVisibility(View.GONE);
                    mViewHolder.viewline.setVisibility(View.GONE);
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
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_easy_txt_dialog, null);
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

            TextView tvTitle;
            TextView tvMessage;
            TextView tvPositiveButton, tvNegativeButton;
            LinearLayout vgLayout;

            View viewline;


            public ViewHolder(View view) {
viewline=view.findViewById(R.id.layout_easy_txt_dialog_line);
                tvTitle = view.findViewById(R.id.dialog_title);
                tvMessage = view.findViewById(R.id.dialog_message);
                tvPositiveButton = view.findViewById(R.id.dialog_positive);
                tvNegativeButton = view.findViewById(R.id.dialog_negative);
                vgLayout = view.findViewById(R.id.dialog_layout);

            }
        }

    }

}
