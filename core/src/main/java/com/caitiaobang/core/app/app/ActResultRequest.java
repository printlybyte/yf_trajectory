package com.caitiaobang.core.app.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;

/**
 * ============================================
 * 描  述：
 * 包  名：com.caitiaobang.core.app.app
 * 类  名：ActResultRequest
 * 创建人：lgd
 * 创建时间：2019/7/5 11:32
 * ============================================
 **/
public class ActResultRequest {

    private OnActResultEventDispatcherFragment fragment;

    public ActResultRequest(Activity activity) {
        fragment = getEventDispatchFragment(activity);
    }

    private OnActResultEventDispatcherFragment getEventDispatchFragment(Activity activity) {
        final FragmentManager fragmentManager = activity.getFragmentManager();


        OnActResultEventDispatcherFragment fragment = findEventDispatchFragment(fragmentManager);
        if (fragment == null) {
            fragment = new OnActResultEventDispatcherFragment();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, OnActResultEventDispatcherFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return fragment;
    }

    private OnActResultEventDispatcherFragment findEventDispatchFragment(FragmentManager manager) {
        return (OnActResultEventDispatcherFragment) manager.findFragmentByTag(OnActResultEventDispatcherFragment.TAG);
    }

    public void startForResult(Intent intent, Callback callback) {
        fragment.startForResult(intent, callback);
    }

    public interface Callback {

        void onActivityResult(int resultCode, Intent data);
    }
}

