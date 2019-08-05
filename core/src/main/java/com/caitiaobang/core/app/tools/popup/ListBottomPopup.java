package com.caitiaobang.core.app.tools.popup;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.caitiaobang.core.R;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 从底部滑上来的popup
 */
public class ListBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    private ListView listView_main_userList;
    private Context mContext;
    private List<String> mList;

    public ListBottomPopup(Context context, List<String> mlist) {

        super(context);
        mList = mlist;
        mContext = context;
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
        return createPopupById(R.layout.popup_slide_from_bottom);
    }

    private void bindEvent() {

        findViewById(R.id.popup_slide_from_bottom_cencel).setOnClickListener(this);

        listView_main_userList = (ListView) findViewById(R.id.popup_slide_from_bottom_popup_list);
        MyAdapter myAdapter = new MyAdapter(mContext, mList);
        listView_main_userList.setAdapter(myAdapter);
        listView_main_userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnListPopupItemClickListener != null) {
                    mOnListPopupItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.popup_slide_from_bottom_cencel) {
//            ToastUtils.ToastMessage(getContext(), "click tx_1");
        }
    }


    class MyAdapter extends BaseAdapter {
        private Context context = null;
        private List<String> mList;

        public MyAdapter(Context context, List<String> mlist) {

            this.context = context;
            this.mList = mlist;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.item_popup_list, null, false);
                mHolder.text_item_listview_username = (TextView) convertView.findViewById(R.id.item_tx);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            String username = mList.get(position).toString();
            mHolder.text_item_listview_username.setText(username);
            return convertView;
        }

        class ViewHolder {
            private TextView text_item_listview_username;
        }
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
