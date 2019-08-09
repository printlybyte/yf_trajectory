package com.yinfeng.yf_trajectory.moudle.service;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.moudle
 * 类  名：PlayerMusicService
 * 创建人：liuguodong
 * 创建时间：2019/8/2 21:01
 * ============================================
 **/

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseApplication;
import com.caitiaobang.core.app.bean.GreendaoLocation;
import com.caitiaobang.core.app.bean.GreendaoProvinceBean;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.greendao.gen.DaoSession;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.activity.MapActivity;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean_string;
import com.yinfeng.yf_trajectory.moudle.bean.MatterApplicationActivityStatusBean;
import com.yinfeng.yf_trajectory.moudle.bean.UploadInfoBean;
import com.yinfeng.yf_trajectory.moudle.bean.UploadLocationInfoBean;
import com.yinfeng.yf_trajectory.moudle.bean.expanded.ApplicationRecordActivityBean;
import com.yinfeng.yf_trajectory.moudle.login.LoginVerActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import timber.log.Timber;


public class LocationService extends Service {
    private final static String TAG = "LocationService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLocation(mGrap);
        initTimePrompt();
        Log.d(TAG, TAG + "---->onCreate,启动服务");


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        startService(intent);
    }


    //定位  单独依赖于定位SDK=======================================================================
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    /**
     * 设置上传间隔
     */
    private void resetLocationUpLoad() {
        initLocation(mGrap);
    }

    private void initLocation(int mInterval) {
        //声明AMapLocationClient类对象
        //声明定位回调监听器
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());

        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(mInterval * 1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.setLocationListener(mAMapLocationListener);
        mLocationClient.startLocation();
        //设置场景模式
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        //设置定位回调监听

    }

    private int mTempNums = 0;

    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    String lat = amapLocation.getLatitude() + "";
                    String lng = amapLocation.getLongitude() + "";
                    String time = System.currentTimeMillis() + "";
                    Log.i(ConstantApi.LOG_I, "定位SDK更新数据 " + "lat: " + lat + "lng: " + lng + "time: " + time);
                    mTempNums++;
                    Toast.makeText(getApplicationContext(), "" + mTempNums, Toast.LENGTH_SHORT).show();
//
//                    LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

                    insertLoactionDate(lat, lng, time);


                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.i(ConstantApi.LOG_I, "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    /**
     * 数据库写入
     */
    private void insertLoactionDate(String lat, String lng, String time) {
        DaoSession daoSession = BaseApplication.getDaoInstant();
        BaseApplication.getDaoInstant().startAsyncSession().runInTx(new Runnable() {
            @Override
            public void run() {
                GreendaoLocation bean = new GreendaoLocation();
                bean.setId(null);
                bean.setLat(lat);
                bean.setLng(lng);
                bean.setTime(time);
                daoSession.insert(bean);//插入或替换
            }
        });
    }

    /**
     * 数据库查询
     */

    public static List queryLoactionDate() {
        DaoSession daoSession = BaseApplication.getDaoInstant();
        try {
            return daoSession.queryBuilder(GreendaoLocation.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 整点报时
     */
    private void initTimePrompt() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, timeFilter);
    }

    /**
     * 分钟计时器
     */
    private BroadcastReceiver mTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);

            //30分钟触发一次获取上传信息
            if (min == 0 || min == 30) {
                getUploadInfo();
            }

            int mmUplaodMinute = mUplaod / 60;
            if (min % mmUplaodMinute == 0) {
                if (parseDate() != null) {
                    Log.i(ConstantApi.LOG_I, "查询数据 jsonArray ：" + parseDate());
                    commitLocationInfo(parseDate());
                } else {
                    showToastC("上传失败，数据转化异常");
                }
            } else {
                showToastC("上传时间不能被整除，请联系管理员修改");
            }


        }
    };

    /**
     * 转化数据
     */
    private JSONArray parseDate() {
        List<GreendaoLocation> mList = queryLoactionDate();
        JSONArray jsonArray = new JSONArray();
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                String queryLat = mList.get(i).getLat();
                String queryLng = mList.get(i).getLng();
                String queryTime = mList.get(i).getTime();
                Log.i(ConstantApi.LOG_I, "查询数据 ：" + "lat: " + queryLat + "lng: " + queryLng + "time: " + queryTime + " mList:" + mList.size());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("lat", queryLat);
                    jsonObject.put("lng", queryLng);
                    jsonObject.put("time", queryTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(ConstantApi.LOG_I, "JSONException ：");
                }
                jsonArray.put(jsonObject);

            }
            return jsonArray;
        }
        return null;
    }


    /**
     * 提交点数据
     */
    private void commitLocationInfo(JSONArray jsonArray) {
//        OkGo.<String>post(Api.API_point_insert)
        // http://192.168.1.137:8111/admin/point/insert
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        OkGo.<String>post(Api.API_point_insert)
                .tag(this)
                .headers("track-token", token)
                .upJson(jsonArray)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String data = response.body();//这个就是返回来的结果
                        Timber.i("success:%s", data);
                        Log.i(ConstantApi.LOG_I, "onSuccess：" + data);
                        try {
                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
                                showToastC("上传成功");
                                DaoSession daoSession = BaseApplication.getDaoInstant();
                                daoSession.deleteAll(GreendaoLocation.class);
                            } else if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_ERR_901) {
                                Toast.makeText(getBaseContext(), "账号在其他地方登陆，密码已泄露，建议重置并重新登录！", Toast.LENGTH_SHORT).show();
//                                AppManager.getInstance().finishAllActivity();
//                                stopSelf(-1);
//                                ActivityUtils.startActivity(LoginVerActivity.class);
                            } else {
                                showToastC(bean.getMessage());
                            }
                        } catch (Exception e) {
                            showToastC("转化失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.i(ConstantApi.LOG_I, "onError：" + response.body());
                        try {
                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
                                Toast.makeText(getBaseContext(), "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            showToastC("转化失败");
                        }
                    }
                });
    }


    /**
     * 获取上传信息
     */
    private void getUploadInfo() {
        OkHttpUtils
                .get()
                .url(Api.API_point_getFrequency)
                .build()
                .execute(new GenericsCallback<UploadInfoBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试");
                    }

                    @Override
                    public void onResponse(UploadInfoBean response, int id) {

                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            UploadInfoBean.DataBean bean = response.getData();

                            if (!TextUtils.isEmpty(bean.getGrap())) {
                                mGrap = Integer.parseInt(bean.getGrap());
                                resetLocationUpLoad();
                            } else {
                                showToastC("没有获取到后台设置的默认抓取频率");
                            }
                            if (!TextUtils.isEmpty(bean.getUpload())) {
                                mUplaod = Integer.parseInt(bean.getUpload());
                            } else {
                                showToastC("没有获取到后台设置的默认上传频率");
                            }

                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i(ConstantApi.LOG_I_NET, "请求结果：" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }

    private int mGrap = 10;
    private int mUplaod = 180;

    private void showToastC(String msg) {
        Toast.makeText(LocationService.this, "" + msg, Toast.LENGTH_SHORT).show();
    }
}