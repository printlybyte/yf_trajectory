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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseApplication;
import com.caitiaobang.core.app.bean.GreendaoLocationBean;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.greendao.gen.DaoSession;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean_string;
import com.yinfeng.yf_trajectory.moudle.bean.UploadInfoBean;
//import com.yinfeng.yf_trajectory.moudle.login.LoginVerActivity;
import com.yinfeng.yf_trajectory.moudle.utils.LocationErrUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import timber.log.Timber;


public class LocationServiceTest extends Service {
    private final static String TAG = "LocationServiceTest";

    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        getUploadInfo();
        initTimePrompt();
        Log.d(TAG, TAG + "---->onCreate,启动服务");
        initNetworkConnect();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), LocationServiceTest.class);
        startService(intent);
        //解除网络广播监听
        unregisterReceiver(networkConnectChangedReceiver);
        unregisterReceiver(mTimeReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //定位  单独依赖于定位SDK=======================================================================
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    /**
     * 设置上传间隔
     */
    private void resetLocationUpLoad(boolean onlySensors) {
        initLocation(mGrap, onlySensors);
    }

    /**
     * onlySensors 无网络情况下 仅设备定位 false 高精度定位 true 仅设备定位
     */
    private void initLocation(int mInterval, boolean onlySensors) {
        //声明AMapLocationClient类对象
        //声明定位回调监听器
        //初始化定位
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        }
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }
        if (onlySensors) {
            //仅设备定位
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        } else {
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        }
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
                    String address = amapLocation.getAddress() + "";
                   Log.i("testre","定位SDK更新数据 " + "lat: " + lat + "lng: " + lng + "time: " + time + "address: " + address);
                    mTempNums++;
                    showToastC("" + mTempNums);
//                    LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));
                    insertLoactionDate(lat, lng, time, address);

                } else {
                    LocationErrUtils.getInstance().showErr(amapLocation.getErrorCode());
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                   Log.i("testre","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    /**
     * 数据库写入
     */
    private void insertLoactionDate(String lat, String lng, String time, String address) {
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
        daoSession.insert(bean);//插入或替换
//            }
//        });
    }

    /**
     * 数据库查询
     */

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
     * 整点报时
     */
    private void initTimePrompt() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, timeFilter);
    }


    public static void main(String[] args) {
        int min = 6;
        int upload = 3;
        System.out.println("================" + min % upload);
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

            if (mUplaod % 60 != 0) {
                mUplaod = 180;
                showToastC("上传时间不能被整除，自动修改为180s");
            }

            int mmUplaodMinute = mUplaod / 60;
           Log.i("testre"," 抓取频率: " + mGrap + " 上传间隔: " + mUplaod + " 计时器任务 整除 ？：" + min % mmUplaodMinute + " 当前时间：" + min);

            if (!NetworkUtils.isConnected()) {
                showToastC("网络无连接，仅设备模式定位");
                return;
            }

            if (min % mmUplaodMinute == 0) {
                if (parseDate() != null) {
                   Log.i("testre","查询数据 jsonArray ：" + parseDate());
                    commitLocationInfo(parseDate());
                } else {
                    showToastC("上传失败，数据转化" +
                            "异常");
                }
            }
        }
    };

    /**
     * 转化数据
     */
    private JSONArray parseDate() {
        List<GreendaoLocationBean> mList = queryLoactionDate();
        JSONArray jsonArray = new JSONArray();
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                String queryLat = mList.get(i).getLat();
                String queryLng = mList.get(i).getLng();
                String queryTime = mList.get(i).getTime();
                String queryAddress = mList.get(i).getAddress();
               Log.i("testre","查询数据 ：" + "lat: " + queryLat + "lng: " + queryLng + "time: " + queryTime + " mList:" + "  address :" + queryAddress + mList.size());
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("lat", queryLat);
                    jsonObject.put("lng", queryLng);
                    jsonObject.put("time", queryTime);
                    jsonObject.put("name", queryAddress);
                } catch (JSONException e) {
                    e.printStackTrace();
                   Log.i("testre","JSONException ：");
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
      Log.i("testre","  token: upload "+token);
        OkGo.<String>post(Api.API_point_insert)
                .tag(this)
                .headers("track-token", token)
                .upJson(jsonArray)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String data = response.body();//这个就是返回来的结果
                        Timber.i("success:%s", data);
                       Log.i("testre","onSuccess：" + data);
                        try {
                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
                               Log.i("testre","上传成功");
                                DaoSession daoSession = BaseApplication.getDaoInstant();
                                daoSession.deleteAll(GreendaoLocationBean.class);
                            } else if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_ERR_901) {
                                Toast.makeText(getBaseContext(), "账号在其他地方登陆，密码已泄露，建议重置并重新登录！", Toast.LENGTH_SHORT).show();
                               Log.i("testre","账号在其他地方登陆");
                                AppManager.getInstance().finishAllActivity();
                                stopSelf(-1);
//                                ActivityUtils.startActivity(LoginVerActivity.class);
                            } else {
                               Log.i("testre",bean.getMessage());
                                showToastC(bean.getMessage());
                            }
                        } catch (Exception e) {
                           Log.i("testre","转化失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                       Log.i("testre","onError：" + response.body());
                        try {
                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
                                Toast.makeText(getBaseContext(), "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            showToastC("转化失败");
                           Log.i("testre","Gson转化失败");

                        }
                    }
                });
    }


    /**
     * 获取上传信息
     */
    private void getUploadInfo() {
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("token = null ");
            return;
        }
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
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
                                resetLocationUpLoad(false);
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
                       Log.i("testre","请求结果：上传信息" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }

    private int mGrap = 10;
    private int mUplaod = 180;

    private void showToastC(String msg) {
        Toast.makeText(LocationServiceTest.this, "" + msg, Toast.LENGTH_SHORT).show();
    }


    NetworkConnectChangedReceiver networkConnectChangedReceiver;

    private void initNetworkConnect() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        registerReceiver(networkConnectChangedReceiver, filter);
    }

    public class NetworkConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

//            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
//                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//                Log.e("TAG", "wifiState:" + wifiState);
//                switch (wifiState) {
//                    case WifiManager.WIFI_STATE_DISABLED:
//                        break;
//                    case WifiManager.WIFI_STATE_DISABLING:
//                        break;
//                }
//            }
//            // 监听wifi的连接状态即是否连上了一个有效无线路由
//            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//                Parcelable parcelableExtra = intent
//                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//                if (null != parcelableExtra) {
//                    // 获取联网状态的NetWorkInfo对象
//                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                    //获取的State对象则代表着连接成功与否等状态
//                    NetworkInfo.State state = networkInfo.getState();
//                    //判断网络是否已经连接
//                    boolean isConnected = state == NetworkInfo.State.CONNECTED;
//                    Log.e("TAG", "isConnected:" + isConnected);
//                    if (isConnected) {
//                        Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
            // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                //获取联网状态的NetworkInfo对象
                NetworkInfo info = intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI
                                || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                           Log.i("testre","网络可用，精度定位");
                            if (mLocationClient == null) {
                                initLocation(mGrap, false);
                            } else {
                                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                                mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                                mLocationClient = null;
                                mLocationOption = null;
                                showToastC("精度定位");
                                resetLocationUpLoad(false);
                            }

                        }
                    } else {
                       Log.i("testre","网络不可用，GPS定位");
                        if (mLocationClient == null) {
                            initLocation(mGrap, true);
                        } else {
                            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                            mLocationClient = null;
                            mLocationOption = null;
                            showToastC("GPS定位");
                            resetLocationUpLoad(true);
                        }
                    }
                }
            }

        }
    }


}