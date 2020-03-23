package com.yinfeng.yf_trajectory.moudle.utils;

import android.util.Log;

import com.caitiaobang.core.app.app.BaseApplication;
import com.caitiaobang.core.app.bean.GreendaoLocationBean;
import com.caitiaobang.core.app.storge.LattePreference;
import com.caitiaobang.core.greendao.gen.DaoSession;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.ConstantApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle.utils
 * 类  名：GreendaoUtils
 * 创建人：liuguodong
 * 创建时间：2019/9/9 11:49
 * ============================================
 **/
public class GreendaoUtils {
    public static List queryLoactionDate() {
        DaoSession daoSession = BaseApplication.getDaoInstant();
        try {
            return daoSession.queryBuilder(GreendaoLocationBean.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转化数据
     */
    public static JSONArray parseDate() {
        List<GreendaoLocationBean> mList = queryLoactionDate();
        JSONArray jsonArray = new JSONArray();
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                String queryLat = mList.get(i).getLat();
                String queryLng = mList.get(i).getLng();
                String queryTime = mList.get(i).getTime();
                String queryAddress = mList.get(i).getAddress();
                String queryAccuracy = mList.get(i).getAccuracy();
                String queryProvider = mList.get(i).getProvider();
                String querySpeed = mList.get(i).getSpeed();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("lat", queryLat);
                    jsonObject.put("lng", queryLng);
                    jsonObject.put("time", queryTime);
                    jsonObject.put("address", queryAddress);
                    jsonObject.put("accuracy", queryAccuracy);
                    jsonObject.put("provider", queryProvider);
                    jsonObject.put("speed", querySpeed);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.i("JSONException ：");
                }
                jsonArray.put(jsonObject);
            }
            return jsonArray;
        }
        return null;
    }


    /**
     * 数据库写入
     */
    public static void insertLoactionDate(String lat, String lng, String time, String address, String accuracy, String provider, String speed) {

//        LattePreference.saveKey(ConstantApi.HAWK_LAT, lat);
//        LattePreference.saveKey(ConstantApi.HAWK_LNG, lng);
        DaoSession daoSession = BaseApplication.getDaoInstant();
//        BaseApplication.getDaoInstant().startAsyncSession().runInTx(new Runnable() {
//            @Override
//            public void run() {
        GreendaoLocationBean bean = new GreendaoLocationBean();
        bean.setId(null);
        bean.setLat(lat);
        bean.setLng(lng);
        bean.setAddress(address);
        bean.setTime(time);
        bean.setAccuracy(accuracy);
        bean.setProvider(provider);
        bean.setSpeed(speed);
        daoSession.insert(bean);//插入或替换
//            }
//        });
    }

}
