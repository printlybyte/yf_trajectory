package com.yinfeng.yf_trajectory;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseApplication;
import com.caitiaobang.core.app.app.Latte;
import com.caitiaobang.core.app.bean.GreendaoLocationBean;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.caitiaobang.core.greendao.gen.DaoSession;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.mdm.SharedPreferenceUtil;
import com.yinfeng.yf_trajectory.moudle.bean.ConmonBean_string;
import com.yinfeng.yf_trajectory.moudle.bean.UpdateAndAliveTimeBean;
import com.yinfeng.yf_trajectory.moudle.bean.UploadInfoBean;
//import com.yinfeng.yf_trajectory.moudle.login.LoginVerActivity;
import com.yinfeng.yf_trajectory.moudle.login.SMSActivity;
import com.yinfeng.yf_trajectory.moudle.utils.ACache;
import com.yinfeng.yf_trajectory.moudle.utils.ConmonUtils;
import com.yinfeng.yf_trajectory.moudle.utils.SMSCore;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import timber.log.Timber;

/**
 * 包名： com.yinfeng.yf_trajectory
 * <p>
 * 创建时间：2016/10/27
 * 项目名称：LocationServiceDemo
 *
 * @author guibao.ggb
 * @email guibao.ggb@alibaba-inc.com
 * <p>
 * 类说明：后台服务定位
 *
 * <p>
 * modeified by liangchao , on 2017/01/17
 * update:
 * 1. 只有在由息屏造成的网络断开造成的定位失败时才点亮屏幕
 * 2. 利用notification机制增加进程优先级
 * </p>
 */
public class LocationService extends NotiService {

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private int locationCount;

    /**
     * 处理息屏关掉wifi的delegate类
     */
    private IWifiAutoCloseDelegate mWifiAutoCloseDelegate = new WifiAutoCloseDelegate();

    /**
     * 记录是否需要对息屏关掉wifi的情况进行处理
     */
    private boolean mIsWifiCloseable = false;


    @Override
    public void onCreate() {
        super.onCreate();



        getUploadInfo();
        getUpdateAndAliveTime();
        initTimePrompt();
        initNetworkConnect();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        applyNotiKeepMech(); //开启利用notification提高进程优先级的机制

        if (mWifiAutoCloseDelegate.isUseful(getApplicationContext())) {
            mIsWifiCloseable = true;
            mWifiAutoCloseDelegate.initOnServiceStarted(getApplicationContext());
        }

        initLocation(mGrap);

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        unApplyNotiKeepMech();
        stopLocation();
//        Intent intent = new Intent(getApplicationContext(), com.yinfeng.yf_trajectory.moudle.service.LocationServiceTest.class);
//        startService(intent);
        //解除网络广播监听
        unregisterReceiver(networkConnectChangedReceiver);
        unregisterReceiver(mTimeReceiver);
        super.onDestroy();
    }

    /**
     * 启动定位
     */
    void initLocation(int mInterval) {
        stopLocation();

        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }

        mLocationOption = new AMapLocationClientOption();
        // 使用连续
        mLocationOption.setOnceLocation(false);
        mLocationOption.setLocationCacheEnable(false);
        // 每10秒定位一次
        mLocationOption.setInterval(mInterval * 1000);
        // 地址信息
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //发送结果的通知
            sendLocationBroadcast(aMapLocation);

            if (!mIsWifiCloseable) {
                return;
            }

            if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
                mWifiAutoCloseDelegate.onLocateSuccess(getApplicationContext(), PowerManagerUtil.getInstance().isScreenOn(getApplicationContext()), NetUtil.getInstance().isMobileAva(getApplicationContext()));
            } else {
                mWifiAutoCloseDelegate.onLocateFail(getApplicationContext(), aMapLocation.getErrorCode(), PowerManagerUtil.getInstance().isScreenOn(getApplicationContext()), NetUtil.getInstance().isWifiCon(getApplicationContext()));
            }

        }

        private void sendLocationBroadcast(AMapLocation aMapLocation) {
            String time = System.currentTimeMillis() + "";

            //记录信息并发送广播
            locationCount++;
            long callBackTime = System.currentTimeMillis();
            StringBuffer sb = new StringBuffer();
            sb.append("定位完成 第" + locationCount + "次\n");
            sb.append("回调时间: " + Utils.formatUTC(callBackTime, null) + "\n");
            if (null == aMapLocation) {
                sb.append("定位失败：location is null!!!!!!!");
            } else {
                sb.append(Utils.getLocationStr(aMapLocation));
                String lat = aMapLocation.getLatitude() + "";
                String lng = aMapLocation.getLongitude() + "";
                String address = aMapLocation.getAddress() + "";
                String accuracy = aMapLocation.getAccuracy() + "";
                String provider = aMapLocation.getProvider() + "";
                String speed = aMapLocation.getSpeed() + "";
                if (!TextUtils.isEmpty(provider) && provider.equals("gps")) {

                    String isLogin = LattePreference.getValue(ConstantApi.HK_CHECK_LOGIN);
                    if (TextUtils.isEmpty(isLogin) || isLogin == null) {
                         Logger.v( "已经退出登录，请及时登录，否则将无定位信息");
                        return;
                    }
                    insertLoactionDate(lat, lng, time + "", address, accuracy, provider, speed);
                } else {
                     Logger.v( "非GPS数据或者GPS为null  不写入数据库 " + sb.toString());
                }
            }



            Logger.v("定位SDK更新数据 " + aMapLocation.getLatitude() + "  " + aMapLocation.getLongitude() + " Accuracy: " + aMapLocation.getAccuracy() + "  provider :" + aMapLocation.getProvider()+"version :"+ AppUtils.getAppVersionCode());
            requestWakeLock();


        }
    };
    PowerManager powerManager;

    public void requestWakeLock() {
        if (powerManager == null) {
            if (PowerManagerUtil.getInstance().isScreenOn(getApplicationContext())) {
                return;
            }
            //针对熄屏后cpu休眠导致的无法联网、定位失败问题,通过定期点亮屏幕实现联网,本操作会导致cpu无法休眠耗电量增加,谨慎使用
            powerManager = (PowerManager) getApplication().getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag")
            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            wl.acquire();
            //点亮屏幕
            wl.release();
            //释放
        }
    }

    /**
     * mGrap 抓取频率   mUplaod 上传时间
     */
    private int mGrap = 10;
    private int mUplaod = 180;

    /**
     * 下载轨迹apk的时间与辅助拉活时间
     */

    private int DownloadApkHour = 5;
    private int DownloadApkMinute = 5;


    /**
     * 下载轨迹助手apk的时间与轨迹拉活时间
     */
    private int DownloadHelpApkHour = 6;
    private int DownloadHelpApkMinute = 5;

    private int DownloadHelpAliveApkHour = 6;
    private int DownloadHelpAliveApkMinute = 7;

    private void showToastC(String msg) {
        Toast.makeText(LocationService.this, "" + msg, Toast.LENGTH_SHORT).show();
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
                             Logger.v( "网络可用，精度定位");
                            getUploadInfo();

                        }
                    } else {
                         Logger.v( "网络不可用，GPS定位");
                    }
                }
            }

        }
    }

    private void resetLocation() {
        if (mLocationClient == null) {
            initLocation(mGrap);
        } else {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
            mLocationClient = null;
            mLocationOption = null;
            initLocation(mGrap);
        }
    }

    /**
     * 数据库写入
     */
    private void insertLoactionDate(String lat, String lng, String time, String address, String accuracy, String provider, String speed) {
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

    private void checkPhone() {
        String localPhone = LattePreference.getValue(ConstantApi.HK_PHONE);
        String line1Nums = ConmonUtils.getLineNumber();
        String line1Numsxxxxxx = "";
        if (!TextUtils.isEmpty(localPhone) && !TextUtils.isEmpty(line1Nums)) {
            if (line1Nums.length() == 14) {
                line1Numsxxxxxx = line1Nums.substring(3, 14).toString();
            } else if (line1Nums.length() == 11) {
                line1Numsxxxxxx = line1Nums;
            } else {
                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                mIntent.putExtra("result", ConstantApi.RECEVIER_NO_SIM_READY);
                //发送广播
                sendBroadcast(mIntent);
                return;
            }
            if (!localPhone.equals(line1Numsxxxxxx)) {
                showToastC("手机号已被更换");
                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                mIntent.putExtra("result", ConstantApi.RECEVIER_NO_SIM_READY);
                //发送广播
                sendBroadcast(mIntent);
                return;
            }
        }
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


            String localPhone = LattePreference.getValue(ConstantApi.HK_PHONE);
            checkPhone();

             Logger.v( "localPhone : " + localPhone + "LineNumber: " + ConmonUtils.getLineNumber());
            if (!NetworkUtils.isConnected()) {
                showToastC("网络无连接");
                return;
            }


            //下载轨迹apk
            if (hour == DownloadApkHour && min == DownloadApkMinute) {   //检测apk
                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION_DOWNLOAD_APK);
                mIntent.putExtra("result", "downlaod");
                //发送广播
                sendBroadcast(mIntent);
            }

            //下载轨迹助手apk
            if (hour == DownloadHelpApkHour && min == DownloadHelpApkMinute) {   //检测apk
                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION_DOWNLOAD_HELP_APK);
                mIntent.putExtra("result", "downlaod");
                //发送广播
                sendBroadcast(mIntent);
            }


            //拉活轨迹助手apk
            if (hour == DownloadHelpAliveApkHour && min == DownloadHelpAliveApkMinute) {   //检测apk
                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                mIntent.putExtra("result", ConstantApi.RECEIVER_ACTION_DOWNLOAD_ALIVE_HELP_APK);
                //发送广播
                sendBroadcast(mIntent);
            }

            //30分钟触发一次获取上传信息  wu tocken默认值
            if (min == 0 || min == 30) {
                getUploadInfo();
                getUpdateAndAliveTime();
            }

            if (mUplaod % 60 != 0) {
                mUplaod = 180;
                showToastC("上传时间不能被整除，自动修改为180s");
            }

            int mmUplaodMinute = mUplaod / 60;
             Logger.v( " 抓取频率: " + mGrap + " 上传间隔: " + mUplaod + " 计时器任务 整除 ？：" + min % mmUplaodMinute + " 当前时间：分钟" + min + " 当前时间：小时" + hour);

            if (min % mmUplaodMinute == 0) {
                if (parseDate() != null) {
                     Logger.v( "查询数据 jsonArray ：" + parseDate());
//                    String token = LattePreference.getValue(ConstantApi.HK_ROMOTE_TOKEN);
//                    if (!TextUtils.isEmpty(token)) {
//                        commitLocationInfoToken(parseDate());
//                    } else if (!TextUtils.isEmpty(ConmonUtils.getLineNumber())) {
//                        commitLocationInfoPhone(parseDate(), ConmonUtils.getLineNumber());
//                    } else {
//                        showToastC("无token 无手机号 ");
//                    }
                    if (ConmonUtils.hasSimCard()) {
//                        commitLocationInfoPhone(parseDate(), localPhone);

                        commitLocationInfoToken(parseDate());
                    } else {

                        if (!TextUtils.isEmpty(localPhone)) {
                            Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                            mIntent.putExtra("result", ConstantApi.RECEVIER_NO_SIM_READY);
                            //发送广播
                            sendBroadcast(mIntent);
                        } else {
                            showToastC("无卡无手机号");
                        }
                    }
                } else {
//                    showToastC("本地无数据");

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
                     Logger.v( "JSONException ：");
                }
                jsonArray.put(jsonObject);
            }
            return jsonArray;
        }
        return null;
    }



    /**
     * 提交token数据
     */
//    private void commitLocationInfoPhone(JSONArray jsonArray, String phone) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("pointList", jsonArray);
//            jsonObject.put("phone", phone);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        OkGo.<String>post(Api.API_point_insert)
//                .tag(this)
//                .upJson(jsonObject)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        String data = response.body();//这个就是返回来的结果
//                        Timber.i("success:%s", data);
//                         Logger.v( "onSuccess：" + data);
//                        try {
//                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
//                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
//                                 Logger.v( "上传成功");
//                                DaoSession daoSession = BaseApplication.getDaoInstant();
//                                daoSession.deleteAll(GreendaoLocationBean.class);
//                            } else if ( bean.getCode() == ConstantApi.API_REQUEST_ERR_901) {
////                                Toast.makeText(getBaseContext(), "账号在其他地方登陆，密码已泄露，建议重置并重新登录！", Toast.LENGTH_SHORT).show();
//                                 Logger.v( "账号在其他地方登陆");
//                                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
//                                mIntent.putExtra("result", ConstantApi.RECEVIER_901);
//                                //发送广播
//                                sendBroadcast(mIntent);
//                            } else {
//                                 Logger.v( bean.getMessage());
//                                showToastC(bean.getMessage());
//                            }
//                        } catch (Exception e) {
//                             Logger.v( "转化失败");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                         Logger.v( "onError：" + response.body());
//                        try {
//                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
//                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
//                                Toast.makeText(getBaseContext(), "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            showToastC("转化失败");
//                             Logger.v( "Gson转化失败");
//
//                        }
//                    }
//                });
//    }


    private void commitLocationInfoToken(JSONArray jsonArray) {
        String token = LattePreference.getValue(ConstantApi.HK_ROMOTE_TOKEN);
        if (TextUtils.isEmpty(token)) {
//            showToastC("token = null ");
            Logger.v( "token = null 上传数据");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pointList", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.API_point_insert)
                .tag(this)
                .headers("track-token", token)
                .upJson(jsonObject)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String data = response.body();//这个就是返回来的结果
                        Timber.i("success:%s", data);
                        Logger.v( "onSuccess：" + data);
                        try {
                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
                                Logger.v( "上传成功");
                                DaoSession daoSession = BaseApplication.getDaoInstant();
                                daoSession.deleteAll(GreendaoLocationBean.class);
                            } else if (  bean.getCode() == ConstantApi.API_REQUEST_ERR_901) {
//                                Toast.makeText(getBaseContext(), "账号在其他地方登陆，密码已泄露，建议重置并重新登录！", Toast.LENGTH_SHORT).show();
                                Logger.v( "账号在其他地方登陆");
                                Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                                mIntent.putExtra("result", ConstantApi.RECEVIER_901);
                                //发送广播
                                sendBroadcast(mIntent);

                            } else {
                                Logger.v( bean.getMessage());
                                showToastC(bean.getMessage());
                            }
                        } catch (Exception e) {
                            Logger.v( "转化失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Logger.v( "onError：" + response.body());
                        try {
                            ConmonBean_string bean = new Gson().fromJson(response.body(), ConmonBean_string.class);
                            if (bean.isSuccess() && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS) {
                                Toast.makeText(getBaseContext(), "" + bean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            showToastC("转化失败");
                            Logger.v( "Gson转化失败");

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
                                initLocation(mGrap);
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
                         Logger.v( "请求结果：上传信息" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    /**
     * 获取更新信息
     */


    private void getUpdateAndAliveTime() {
        OkHttpUtils
                .get()
                .url(Api.API_appVersion_getUpdateAndAliveTime)
                .build()
                .execute(new GenericsCallback<UpdateAndAliveTimeBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试");
                    }

                    @Override
                    public void onResponse(UpdateAndAliveTimeBean response, int id) {

                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            UpdateAndAliveTimeBean.DataBean bean = response.getData();

                            if (!TextUtils.isEmpty(bean.getUpdateHour())) {
                                DownloadApkHour = Integer.parseInt(bean.getUpdateHour());
                            }
                            if (!TextUtils.isEmpty(bean.getUpdateMinter())) {
                                DownloadApkMinute = Integer.parseInt(bean.getUpdateMinter());
                            }

                            if (!TextUtils.isEmpty(bean.getHelpUpdateHour())) {
                                DownloadHelpApkHour = Integer.parseInt(bean.getHelpUpdateHour());
                            }
                            if (!TextUtils.isEmpty(bean.getHelpUpdateMinter())) {
                                DownloadHelpApkMinute = Integer.parseInt(bean.getHelpUpdateMinter());
                            }
                            if (!TextUtils.isEmpty(bean.getHelpKeepAliveHour())) {
                                DownloadHelpAliveApkHour = Integer.parseInt(bean.getHelpKeepAliveHour());
                            }
                            if (!TextUtils.isEmpty(bean.getHelpKeepAliveMinter())) {
                                DownloadHelpAliveApkMinute = Integer.parseInt(bean.getHelpKeepAliveMinter());
                            }
                        } else {
                            showToastC(response.getMessage());
                        }
                         Logger.v( "请求结果：getUpdateAndAliveTime" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }
}
