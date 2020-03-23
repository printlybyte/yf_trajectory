package com.yinfeng.yf_trajectory;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.FileUtils;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.alarm.ContactWorkService;
import com.yinfeng.yf_trajectory.alarm.IntentConst;
import com.yinfeng.yf_trajectory.moudle.bean.GetWorkStatusBean;
import com.yinfeng.yf_trajectory.moudle.bean.IsLeaveStatusBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Calendar;

import okhttp3.Call;

import static com.yinfeng.yf_trajectory.moudle.utils.GreendaoUtils.insertLoactionDate;
import static com.yinfeng.yf_trajectory.moudle.utils.GreendaoUtils.parseDate;
import static com.yinfeng.yf_trajectory.moudle.utils.UploadLocationsUtils.commitLocationInfoToken;

/**
 * 包名： com.yinfeng.yf_trajectory
 * 1. 只有在由息屏造成的网络断开造成的定位失败时才点亮屏幕
 * 2. 利用notification机制增加进程优先级
 */
public class LocationService extends NotiService {
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    /**
     * 处理息屏关掉wifi的delegate类
     */
    private IWifiAutoCloseDelegate mWifiAutoCloseDelegate = new WifiAutoCloseDelegate();
    /**
     * 记录是否需要对息屏关掉wifi的情况进行处理
     */
    private boolean mIsWifiCloseable = false;


    private BroadcastReceiver mEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String mType = intent.getStringExtra("event");
                Message message = Message.obtain();
                message.what = 104;
                Bundle bundle = new Bundle();
                bundle.putString("event", mType);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle("");
        builder.setContentText("");
        builder.setWhen(System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pt = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentIntent(pt);
        Notification notification = builder.build();
        startForeground(0, notification);


        initBroadCastReceiver();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        applyNotiKeepMech(); //开启利用notification提高进程优先级的机制
        if (mWifiAutoCloseDelegate.isUseful(getApplicationContext())) {
            mIsWifiCloseable = true;
            mWifiAutoCloseDelegate.initOnServiceStarted(getApplicationContext());
        }
        initLocation();
        startLooperCycle();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {

        stopForeground(false);
        unregisterReceiver(mEventReceiver);
        unregisterReceiver(mTimeReceiver);
        unApplyNotiKeepMech();
        stopLooperCycle();
        stopLocation();
    }

    private void sendServiceMsg(String status) {
        Intent mIntent = new Intent(ConstantApi.activity_action);
        mIntent.putExtra("event", status);
        sendBroadcast(mIntent);
    }

    /**
     * 初始化定位
     */
    private int mGrapInt = 30;

    private void initLocation() {
        stopLocation();
        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }
        mLocationOption = new AMapLocationClientOption();
        // 使用连续
        mLocationOption.setOnceLocation(false);
        mLocationOption.setLocationCacheEnable(false);
        // 每10秒定位一次
        String mGrap = LattePreference.getValue("mGrap_KEY");
        if (!TextUtils.isEmpty(mGrap)) {
            mGrapInt = Integer.parseInt(mGrap);
        }
        mLocationOption.setInterval(mGrapInt * 1000);
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
        Logger.i("初始化定位组件....");
    }

    /**
     * 停止定位
     */
    void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            Logger.i("停止定位....");
            sendServiceMsg("stop");
        }
    }

    void startLocation() {
        Logger.i("启动定位....");
        if (null != mLocationClient) {
            mLocationClient.startLocation();
        } else {
            initLocation();
        }
        sendServiceMsg("start");
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {
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
    };

    private void sendLocationBroadcast(AMapLocation aMapLocation) {
        if (null == aMapLocation || aMapLocation.getErrorCode() != 0) {
            Logger.i("定位失败...." + aMapLocation.getLocationDetail());
            return;
        }
        dealWithDate(aMapLocation);
    }

    /**
     * 广播注册
     */
    private void initBroadCastReceiver() {
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, timeFilter);
        Logger.i("轮训器广播注册...");
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        registerReceiver(networkConnectChangedReceiver, filter);
        Logger.i("网络状态广播注册...");
        IntentFilter eventFilter = new IntentFilter();
        eventFilter.addAction(ConstantApi.service_action);
        registerReceiver(mEventReceiver, eventFilter);
        Logger.i("service事件广播注册...");

    }

    private void cleanLog(int day, int hour, int min) {

        //定时更新手机通讯录
        if ((hour == 10 && min == 1) || (hour == 14 && min == 1)) {
            Intent intentActon = new Intent(getBaseContext(), ContactWorkService.class);
            intentActon.setAction(IntentConst.Action.downloadcontact);
            startService(intentActon);
        }


        if ((day == 10 && hour == 10 && min == 10) || (day == 20 && hour == 20 && min == 20)) {
            try {
                FileUtils.deleteDir(Environment.getExternalStorageDirectory() + "/logger");
                Logger.i("清理日志文件中...");
            } catch (Exception e) {
                Logger.i("清理日志文件出错");
            }
        }
    }

    private BroadcastReceiver mTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int min = cal.get(Calendar.MINUTE);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            //获取星期
            int week = cal.get(Calendar.DAY_OF_WEEK);

//            Message message = Message.obtain();
//            message.what = 105;
//            Bundle bundle = new Bundle();
//            bundle.putInt("day", day);
//            bundle.putInt("min", min);
//            bundle.putInt("hour", hour);
//            bundle.putInt("week", week);
//            message.setData(bundle);
//            mHandler.sendMessage(message);


            Logger.i("当前时间：分钟" + min + " 当前时间：小时" + hour);
            checkWorkTime(hour, min, day);


        }
    };

    private synchronized void dealWithDate(AMapLocation aMapLocation) {
        String time = System.currentTimeMillis() + "";
        String lat = aMapLocation.getLatitude() + "";
        String lng = aMapLocation.getLongitude() + "";
        String address = aMapLocation.getAddress() + "";
        String accuracy = aMapLocation.getAccuracy() + "";
        String provider = aMapLocation.getProvider() + "";
        String speed = aMapLocation.getSpeed() + "";
//        Log.i("testre",Utils.getLocationStr(aMapLocation));
        if (!TextUtils.isEmpty(provider) && provider.equals("gps")) {
            insertLoactionDate(lat, lng, time + "", address, accuracy, provider, speed);
//            Logger.i("lat：" + lat + " lng: " + lng + " address:" + address);
        } else {
            Logger.i("非GPS数据  或者GPS为null  不写入数据库 ");
        }
    }


    //    public void wakeUpAndUnlock() {
//        //屏锁管理器
////        KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
////        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
////        //解锁
////        kl.disableKeyguard();
////        requestWakeLock();
//    }

    //    =====================================================================================================================
    //      handler 轮训器
    //    =====================================================================================================================

    private int mUplaodTime = 600;//秒
    private int mCheckLoopTime = 10;//分钟

    private Handler mHandler = new LoopHandler();

    private void startLooperCycle() {
        //102 检测抓取频率
        if (!mHandler.hasMessages(102)) {
            mHandler.sendEmptyMessage(102);
        }
        //103 上传坐标
        if (!mHandler.hasMessages(103)) {
            mHandler.sendEmptyMessage(103);
        }
    }

    private void stopLooperCycle() {
        if (mHandler.hasMessages(103)) {
            mHandler.removeMessages(103);
        }
        if (mHandler.hasMessages(102)) {
            mHandler.removeMessages(102);
        }
    }

    private class LoopHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 102) {
                mHandler.sendEmptyMessageDelayed(102, mCheckLoopTime * 60 * 1000);
                Logger.i("轮训器 102...：更新时间..." + mCheckLoopTime * 60 * 1000);


//                getUploadInfo();
//                getUpdateAndAliveTime();

                requestWakeLock();
                getCommonJudgeIsWork();
                getJudgeLeave();
                Utils.checkPer();

            }
            if (msg.what == 103) {
                mHandler.sendEmptyMessageDelayed(103, mUplaodTime * 1000);
                Logger.i("轮训器 103...：上传时间..." + mUplaodTime * 1000);
                if (parseDate() != null) {
                    Logger.i("数据上传中..." + parseDate());
                    commitLocationInfoToken(parseDate());
                } else {
                    Logger.i("数据库无数据");
                }
            }

            if (msg.what == 104) {
                Bundle bundle = msg.getData();
                String mType = bundle.getString("event");
                if (!TextUtils.isEmpty(mType)) {
                    if (mType.equals("start")) {
                        startLocation();
                        Logger.i("接收到广播启动定位....");
                    } else if (mType.equals("stop")) {
                        stopLocation();
                        Logger.i("接收到广播停止定位.....");
                    } else if (mType.equals("upload")) {
                        if (parseDate() != null) {
                            Logger.i("数据上传中..." + parseDate());
                            commitLocationInfoToken(parseDate());
                        } else {
                            Logger.i("数据库无数据");
                        }
                    } else {
                        Logger.i("接收到广播类型不明.....");
                    }

                }
            }

            if (msg.what == 105) {
                Bundle bundle = msg.getData();
                int day = bundle.getInt("day");
                Logger.i("service event: " + day);
                int hour = bundle.getInt("hour");
                int min = bundle.getInt("min");
                int week = bundle.getInt("week");
                abcd(day, hour, min);
            }
        }
    }


    private void abcd(int day, int hour, int min) {
        cleanLog(day, hour, min);
//        String mOvertime_status = LattePreference.getValue(ConstantApi.overtime_status);
//        if (!TextUtils.isEmpty(mOvertime_status) && mOvertime_status.equals("1")) {
//            //点击了开始  上班中  不可以点击
//            startLocation();
//            return;
//        }

//        Logger.i("week: " + week);
//        if (week == 7 || week == 1) {
//            Logger.i("周六日不定位.....");
//            stopLocation();
//            return;
//        }
//        requestWakeLock();
        //定时检测更新
//        checkUpadateApk(hour, min);
//            //判断是否是工作日
//        String isWorkingDay = LattePreference.getValue(ConstantApi.isWorkingDay);
//        if (!TextUtils.isEmpty(isWorkingDay) && !isWorkingDay.equals("1")) {
//            //强制停止
//            stopLocation();
//            Logger.i("非工作日 不定位....");
//            LattePreference.saveKey(ConstantApi.work_time_status, "0");
//            return;
//        }
        //获取请假的状态 1 为请假  0 为未请假
    }

    //    =====================================================================================================================
    //      网络监听器
    //    =====================================================================================================================

    private NetworkConnectChangedReceiver networkConnectChangedReceiver;

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
                        if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            Logger.i("网络可用....");
//                            NotificationManagerUtils.startNotificationManager("网络可用", R.mipmap.ic_app_start_icon);
                        }
                    } else {
                        Logger.i("网络不可用....");
//                        stopLooperLocation();
//                        NotificationManagerUtils.startNotificationManager("网络断开", R.mipmap.ic_app_start_icon);
                    }
                }
            }
        }
    }

    private int defaultStartHour = 7;
    private int defaultStartMinute = 1;
    private int defaultEndHour = 19;
    private int defaultEndMinute = 0;

    private void checkLeave() {
        String leave_time_status = LattePreference.getValue(ConstantApi.leave_time_status);
        if (!TextUtils.isEmpty(leave_time_status) && leave_time_status.equals("1")) {
            stopLocation();
            Logger.i("停止定位");
            return;
        }
//                String leave_time_start = LattePreference.getValue(ConstantApi.leave_time_start);
//                String leave_time_end = LattePreference.getValue(ConstantApi.leave_time_end);
//                if (!TextUtils.isEmpty(leave_time_status) && !TextUtils.isEmpty(leave_time_end)) {
//                    Long leave_startLong = Utils.getTimeLong(leave_time_start);
//                    Long leave_endLong = Utils.getTimeLong(leave_time_end);
//                    Long countTimeLong = System.currentTimeMillis();
//                    if (countTimeLong >= leave_startLong && countTimeLong <= leave_endLong) {
//                        Log.i("testre","请假中 不定位");
//                        LattePreference.saveKey(ConstantApi.work_time_status, "0");
//                        stopLocation();
//                        return;
//                    }else {
//                        Log.i("testre","状态为请假中  但时间不允许运行");
//
//                    }
//                }else {
//                    Log.i("testre","请假时间获取为null 请联系后台开发人员");
//                    Toast.makeText(this, "请假时间获取为null 请联系后台开发人员", Toast.LENGTH_SHORT).show();
//                }
//            }

    }

    private void checkWorkTime(int hour, int min, int day) {



        if (min % 10 == 0) {
            requestWakeLock();
        }

        String leave_time_status = LattePreference.getValue(ConstantApi.leave_time_status);
        if (!TextUtils.isEmpty(leave_time_status) && leave_time_status.equals("1")) {
            stopLocation();
            Logger.i("请假中...停止定位");
            return;
        }


        String work_day_isLocation = LattePreference.getValue(ConstantApi.work_day_isLocation_status);
        if (!TextUtils.isEmpty(work_day_isLocation) && work_day_isLocation.equals("2")) {
            stopLocation();
            Logger.i("非工作日..点击定位...停止定位");
            if (hour == 6 && (min > 1 && min < 10)) {
                LattePreference.saveKey(ConstantApi.work_day_isLocation_status, "1");
                Logger.i("非工作日..自动启动");
            }

            if (hour == 7 && (min > 1 && min < 10)) {
                LattePreference.saveKey(ConstantApi.work_day_isLocation_status, "1");
                Logger.i("非工作日..自动启动");
            }
            return;
        }


        String mTime_start = LattePreference.getValue(ConstantApi.work_time_start);
        String mTime_end = LattePreference.getValue(ConstantApi.work_time_end);
        if (!TextUtils.isEmpty(mTime_start)) {
            defaultStartHour = Integer.parseInt(mTime_start.split(":")[0]);
            defaultStartMinute = Integer.parseInt(mTime_start.split(":")[1]);
        }
        if (!TextUtils.isEmpty(mTime_end)) {
            defaultEndHour = Integer.parseInt(mTime_end.split(":")[0]);
            defaultEndMinute = Integer.parseInt(mTime_end.split(":")[1]);
        }
        String defaultStartHourStr = defaultStartHour + "";
        String defaultStartMinuteStr = defaultStartMinute + "";
        String defaultEndHourStr = defaultEndHour + "";
        String defaultEndMinuteStr = defaultEndMinute + "";
        int startLongI = Integer.parseInt(defaultStartHourStr + defaultStartMinuteStr);
        int endLongI = Integer.parseInt(defaultEndHourStr + defaultEndMinuteStr);
        String countHour = hour + "";
        String countMinute = "";
        if (min < 10) {
            countMinute = "0" + min + "";
        } else {
            countMinute = min + "";
        }
        int countLongI = Integer.parseInt(countHour + countMinute);
        Logger.i("countLongI: " + countLongI + " startLongI: " + startLongI + " endLongI" + endLongI);
        if (countLongI > startLongI && endLongI > countLongI) {
            //当前时间在区间范围就开始定位
            LattePreference.saveKey(ConstantApi.work_time_status, "1");
            Logger.i("在当前时间区间，自动清除状态启动定位");
        }
        String work_time_status = LattePreference.getValue(ConstantApi.work_time_status);
        if (!TextUtils.isEmpty(work_time_status) && work_time_status.equals("0")) {
            stopLocation();
            Logger.i("stopLocation.....");
        } else {
            Logger.i("startLocation......");
            startLocation();
        }

        cleanLog(day, hour, min);


    }

    /**
     * isWorkStatus==0 异常  ==1 工作日   ==2 非工作日
     */
    private void getCommonJudgeIsWork() {
        Logger.i("接口地址: " + Api.commonJudgeIsWork);
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            Logger.i("commonJudgeIsWork token =null ");
            return;
        }
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
                .url(Api.commonJudgeIsWork)
                .build()
                .execute(new GenericsCallback<GetWorkStatusBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Logger.i("getCommonJudgeIsWork：" + e.getMessage() + "");
//                        LattePreference.saveKey(ConstantApi.isWorkingDay, "0");
                    }

                    @Override
                    public void onResponse(GetWorkStatusBean response, int id) {

                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            Logger.i("请求结果：" + GsonUtils.getInstance().toJson(response));

                            if (response.getData().isIsWork()) {//工作日
                                LattePreference.saveKey(ConstantApi.work_day_isLocation_status, "1");
                            } else {
                                if (response.getData().isIsStartUp()) {  //非工作日启动
                                    //清除状态
                                    LattePreference.saveKey(ConstantApi.work_day_isLocation_status, "1");
                                }
                            }
                        } else {
                            Logger.i(response.getMessage());
                        }
                    }
                });
    }


    private void getJudgeLeave() {
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            Logger.i("getJudgeLeave token =null ");
            return;
        }
        OkGo.<String>get(Api.commonjudgeLeave)
                .headers("track-token", token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.i("接口地址：" + Api.commonjudgeLeave);
                        Logger.i("请求结果: 成功" + "响应码" + response.code() + "请求结果 " + response.body());
                        try {
                            IsLeaveStatusBean bean = new Gson().fromJson(response.body(), IsLeaveStatusBean.class);
                            if (bean != null && bean.getCode() == ConstantApi.API_REQUEST_SUCCESS && bean.isSuccess()) {
                                if (bean.getData().getState().equals("1")) {
                                    LattePreference.saveKey(ConstantApi.leave_time_status, "1");
                                    if (!TextUtils.isEmpty(bean.getData().getStartTime())) {
                                        LattePreference.saveKey(ConstantApi.leave_time_start, bean.getData().getStartTime() + "");
                                    }
                                    if (!TextUtils.isEmpty(bean.getData().getEndTime())) {
                                        LattePreference.saveKey(ConstantApi.leave_time_end, "" + bean.getData().getEndTime() + "");
                                    }
                                    sendServiceMsg("stop");
                                } else if (bean.getData().getState().equals("0")) {
                                    LattePreference.saveKey(ConstantApi.leave_time_status, "0");
                                }
                            }
                        } catch (Exception e) {
                            LattePreference.saveKey(ConstantApi.leave_time_status, "0");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Logger.i("请求结果: 失败" + '\n' + "响应码" + response.code() + '\n' + "请求结果 " + response.body());
                    }
                });
    }



    public void requestWakeLock() {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyApp::MyWakelockTag");
            wakeLock.acquire();

            wakeLock.release();
        Logger.i("唤醒设备，点亮屏幕");
            //释放
    }

}
