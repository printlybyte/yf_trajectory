package com.yinfeng.yf_trajectory.moudle.eventbus;

import android.app.Activity;

import org.greenrobot.eventbus.EventBus;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.eventbus
 * 类  名：EventBusUtils
 * 创建人：liuguodong
 * 创建时间：2019/8/6 11:48
 * ============================================
 **/
public class EventBusUtils {

    public void EventBusUtils() {
    }

    public void send(int type) {
        EventBusBean eventBusBean = new EventBusBean();
        eventBusBean.setType(type);
        EventBus.getDefault().post(eventBusBean);
    }

    public void register(Activity activity) {
        if (!EventBus.getDefault().isRegistered(activity)) {
            EventBus.getDefault().register(activity);
        }
    }

    public void unregister(Activity activity) {
        if (EventBus.getDefault().isRegistered(activity))//
            EventBus.getDefault().unregister(activity);
    }
}
