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

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.PowerManagerUtil;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.ApkDownloadBean;
import com.yinfeng.yf_trajectory.moudle.utils.FileUtils;
import com.yinfeng.yf_trajectory.moudle.utils.NotificationManagerUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import okhttp3.Call;

import static com.yinfeng.yf_trajectory.moudle.utils.ConmonUtils.getHelpVersionCode;


/**
 * 循环播放一段无声音频，以提升进程优先级
 * <p>
 * Created by jianddongguo on 2017/7/11.
 * http://blog.csdn.net/andrexpert
 */

public class PlayerMusicService extends Service {
    private final static String TAG = "PlayerMusicService";
    //    private MediaPlayer mMediaPlayer;
    public static final boolean DEBUG = true;

    public static final String PACKAGE_NAME = "com.jiangdg.keepappalive";


    private int mGrap = 30;//秒


    private int DownloadApkHour = 2;
    private int DownloadApkMinute = 1;


    private int DownloadHelpApkHour = 1;
    private int DownloadHelpApkMinute = 1;


    private int HelpAliveApkHour = 1;
    private int HelpAliveApkMinute = 3;

    private boolean isNightOrDay = false;
    private boolean isPermission = true;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    MediaPlayer mMediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();


        if (DEBUG)
            Log.d(TAG, TAG + "---->onCreate,启动服务");
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        mMediaPlayer.setLooping(true);


        initEvent();


    }

    /**
     * 时间轮训器 注册
     */
    private void initEvent() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantApi.RECEIVER_ACTION_DOWNLOAD_APK);
        intentFilter.addAction(ConstantApi.RECEIVER_ACTION_DOWNLOAD_HELP_APK);
        registerReceiver(locationChangeBroadcastReceiver, intentFilter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();
        return START_STICKY;
    }

    private void startPlayMusic(){
        if(mMediaPlayer != null){
            mMediaPlayer.start();
        }
    }

    private void stopPlayMusic(){
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG)
            Log.d(TAG, TAG + "---->onCreate,停止服务");
        stopPlayMusic();
        Intent intent = new Intent(getApplicationContext(), PlayerMusicService.class);
        startService(intent);

        try {
            if (locationChangeBroadcastReceiver != null)
                unregisterReceiver(locationChangeBroadcastReceiver);
//            if (mTimeReceiver != null) {
//                getApplicationContext().unregisterReceiver(mTimeReceiver);
//            }
        } catch (Exception e) {
        }
        super.onDestroy();
    }




    private void checkLogin() {
        String isLogin = LattePreference.getValue(ConstantApi.HK_ICCID);
        if (TextUtils.isEmpty(isLogin) || isLogin == null) {
            NotificationManagerUtils.startNotificationManager("已经退出登录，请及时登录，否则将无定位信息", R.mipmap.ic_app_start_icon);
            Logger.i("已经退出登录，请及时登录，否则将无定位信息");
            Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
            mIntent.putExtra("result", ConstantApi.RECEVIER_NO_SIM_READY);

            sendBroadcast(mIntent);
            return;
        }
    }

    private void showToastC(String msg) {
        Toast.makeText(PlayerMusicService.this, "" + msg, Toast.LENGTH_SHORT).show();
    }


    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConstantApi.RECEIVER_ACTION_DOWNLOAD_APK)) {
                String locationResult = intent.getStringExtra("result");
                if (null != locationResult && !locationResult.trim().equals("")) {
                    if ("downlaod".equals(locationResult)) {
//                        wakeUpAndUnlock();
                        requestDate();
                    }

                }
            } else if (action.equals(ConstantApi.RECEIVER_ACTION_DOWNLOAD_HELP_APK)) {
                String locationResult = intent.getStringExtra("result");
                if ("downlaod".equals(locationResult)) {
                    requestDateHelpApk();
                }
            }
        }
    };


    public void wakeUpAndUnlock() {
        //屏锁管理器
        KeyguardManager km = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        requestWakeLock();
    }

    /**
     * downloadType 1= 轨迹 track   2 助手 help
     */
    private void requestDate() {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        Logger.i("API: " + Api.appVersionGetNewVersion);
        OkHttpUtils
                .get()
                .url(Api.appVersionGetNewVersion)
                .addParams("type", "track")
                .build()
                .execute(new GenericsCallback<ApkDownloadBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                    }

                    @Override
                    public void onResponse(ApkDownloadBean response, int id) {
                        if (response != null && response.isSuccess() && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            String DownLoadUrl = response.getData().getDownLoadUrl();
                            String VersionCode = response.getData().getVersionCode();
                            int onLineVersionCode = Integer.parseInt(VersionCode);
                            if (TextUtils.isEmpty(response.getData().getDownLoadUrl())) {
                                showToastC("下载地址为空，请联系管理员");
                                return;
                            }
                            int onLocalVersionCode = AppUtils.getAppVersionCode();
                            if (onLineVersionCode > onLocalVersionCode) {
                                NotificationManagerUtils.startNotificationManager("检测到银丰轨迹新版本，下载中...", R.mipmap.ic_app_start_icon);
                                downloadFile(DownLoadUrl);

                            } else {
                                Logger.i("无新版本");
                            }
                        } else {
                            showToastC(response.getMessage());
                        }
                        Logger.i("请求结果：检测新版本 " + GsonUtils.getInstance().toJson(response));
                    }
                });
    }

    private void requestDateHelpApk() {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        OkHttpUtils
                .get()
                .url(Api.appVersionGetNewVersion)
                .addParams("type", "help")
                .build()
                .execute(new GenericsCallback<ApkDownloadBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                    }

                    @Override
                    public void onResponse(ApkDownloadBean response, int id) {
                        if (response != null && response.isSuccess() && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            String DownLoadUrl = response.getData().getDownLoadUrl();
                            String VersionCode = response.getData().getVersionCode();
                            int onLineVersionCode = Integer.parseInt(VersionCode);
                            if (TextUtils.isEmpty(response.getData().getDownLoadUrl())) {
                                showToastC("下载地址为空，请联系管理员");
                                return;
                            }

                            int onLocalVersionCode = getHelpVersionCode();
                            if (getHelpVersionCode() == 0) {
                                NotificationManagerUtils.startNotificationManager("未安装银丰轨迹助手，下载中...", R.mipmap.ic_app_start_icon);
                                return;
                            }
                            if (onLineVersionCode > onLocalVersionCode) {
                                NotificationManagerUtils.startNotificationManager("检测到银丰助手新版本，下载中...", R.mipmap.ic_app_start_icon);
                                downloadFileHelpApk(DownLoadUrl);
                            } else {
                                Logger.i("无新版本");
                            }
                        } else {
                            showToastC(response.getMessage());
                        }
                        Logger.i("请求结果：检测新版本助手：" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    private void downloadFile(String mUrl) {
        String mPath;
        if (FileUtils.createOrExistsDir(ConstantApi.CommonApkPath)) {
            mPath = ConstantApi.CommonApkPath;
        } else {
            mPath = ConstantApi.CommonPath;
        }
        String downLoadApkName = "";
        downLoadApkName = ConstantApi.CommonApkName;
        OkGo.<File>get(mUrl) //
                .retryCount(3)
                .execute(new FileCallback(mPath, downLoadApkName) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Logger.i("下载成功....");
                        Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                        mIntent.putExtra("result", ConstantApi.RECEVIER_DOWNLOAD_APK);
                        sendBroadcast(mIntent);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        Logger.i("下载失败,重试中....");
                        showToastC("下载失败,重试中....");
                        NotificationManagerUtils.startNotificationManager("银丰轨迹下载失败，重新下载中...", R.mipmap.ic_app_start_icon);
                        requestDate();

                    }


                    @Override
                    public void downloadProgress(Progress progress) {
                        float prgressx = progress.fraction * 100;
                        Logger.i("下载进度 ：" + prgressx);
                        if (prgressx == 100) {

                        }
                    }
                });
    }


    private void downloadFileHelpApk(String mUrl) {
        String mPath;
        if (FileUtils.createOrExistsDir(ConstantApi.CommonApkPath)) {
            mPath = ConstantApi.CommonApkPath;
        } else {
            mPath = ConstantApi.CommonPath;
        }
        String downLoadApkName = ConstantApi.CommonHelpApkName;
        OkGo.<File>get(mUrl)
                .retryCount(3)
                .execute(new FileCallback(mPath, downLoadApkName) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Logger.i("下载成功....");
                        Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
                        mIntent.putExtra("result", ConstantApi.RECEVIER_DOWNLOAD_HELP_APK);
                        sendBroadcast(mIntent);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        Logger.i("下载失败,重试中....");
                        showToastC("下载失败,重试中....");
                        NotificationManagerUtils.startNotificationManager("轨迹助手下载失败，重新下载中...", R.mipmap.ic_app_start_icon);
                        requestDateHelpApk();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        float prgressx = progress.fraction * 100;
                        Logger.i("下载进度 ：" + prgressx);
                        if (prgressx == 100) {
                        }
                    }
                });
    }



    private void checkUpadateApk(int hour, int min) {
        if (hour == DownloadApkHour && min == DownloadApkMinute) {   //检测apk
//            wakeUpAndUnlock();
            Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION_DOWNLOAD_APK);
            mIntent.putExtra("result", "downlaod");
            sendBroadcast(mIntent);
        }
        if (hour == DownloadHelpApkHour && min == DownloadHelpApkMinute) {   //检测apk
//            wakeUpAndUnlock();
            Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION_DOWNLOAD_HELP_APK);
            mIntent.putExtra("result", "downlaod");
            sendBroadcast(mIntent);
        }
        //拉活轨迹助手apk
        if (hour == HelpAliveApkHour && min == HelpAliveApkMinute) {   //检测apk
//            wakeUpAndUnlock();
            Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION);
            mIntent.putExtra("result", ConstantApi.RECEIVER_ACTION_DOWNLOAD_ALIVE_HELP_APK);
            sendBroadcast(mIntent);
        }
    }


    /**
     * 获取上传信息
     */
//    private void getUploadInfo() {
//        Logger.i("接口名称： " + Api.API_point_getFrequency + " ");
//        OkHttpUtils
//                .get()
//                .url(Api.API_point_getFrequency)
//                .build()
//                .execute(new GenericsCallback<UploadInfoBean>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        showToastC("获取上传信息数据解析异常");
//                    }
//
//                    @Override
//                    public void onResponse(UploadInfoBean response, int id) {
//                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
//                            UploadInfoBean.DataBean bean = response.getData();
//                            Logger.i("请求结果： " + GsonUtils.getInstance().toJson(response));
//                            if (!TextUtils.isEmpty(bean.getGrap())) {
//                                int mNetGrap = (Integer.parseInt(bean.getGrap()));
//                                if (mGrap != mNetGrap) {
//                                    mGrap = mNetGrap;
//                                    Logger.i("检测到后台下发抓取频率与运行不一致，重新初始化定位组件");
//                                    LattePreference.saveKey("mGrap_KEY", "" + mGrap);
////                                    startLocationService();
//                                }
//                            } else {
//                                showToastC("没有获取到后台设置的默认抓取频率");
//                            }
//                            if (!TextUtils.isEmpty(bean.getUpload())) {
//                                mUplaodTime = (Integer.parseInt(bean.getUpload()));
//                            } else {
//                                showToastC("没有获取到后台设置的默认上传频率");
//                            }
//                        } else {
//                            showToastC(response.getMessage());
//                        }
//                        Logger.i("抓取频率：" + mGrap + " 上传时间 ：" + mUplaodTime);
//                    }
//                });
//    }

    /**
     * 获取更新信息
     */
//    private void getUpdateAndAliveTime() {
//        Logger.i("接口地址: " + Api.API_appVersion_getUpdateAndAliveTime);
//
//        OkHttpUtils
//                .get()
//                .url(Api.API_appVersion_getUpdateAndAliveTime)
//                .build()
//                .execute(new GenericsCallback<UpdateAndAliveTimeBean>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        showToastC("获取升级信息数据转化异常");
//                    }
//
//                    @Override
//                    public void onResponse(UpdateAndAliveTimeBean response, int id) {
//
//                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
//                            UpdateAndAliveTimeBean.DataBean bean = response.getData();
//                            Logger.i("请求结果： " + GsonUtils.getInstance().toJson(response));
//
//                            if (!TextUtils.isEmpty(bean.getUpdateHour())) {
//                                DownloadApkHour = Integer.parseInt(bean.getUpdateHour());
//                            }
//                            if (!TextUtils.isEmpty(bean.getUpdateMinter())) {
//                                DownloadApkMinute = Integer.parseInt(bean.getUpdateMinter());
//                            }
//
//                            if (!TextUtils.isEmpty(bean.getHelpKeepAliveHour())) {
//                                HelpAliveApkHour = Integer.parseInt(bean.getHelpKeepAliveHour());
//                            }
//                            if (!TextUtils.isEmpty(bean.getHelpKeepAliveMinter())) {
//                                HelpAliveApkMinute = Integer.parseInt(bean.getHelpKeepAliveMinter());
//                            }
//
//                            if (!TextUtils.isEmpty(bean.getHelpUpdateHour())) {
//                                DownloadHelpApkHour = Integer.parseInt(bean.getHelpUpdateHour());
//                            }
//                            if (!TextUtils.isEmpty(bean.getHelpUpdateMinter())) {
//                                DownloadHelpApkMinute = Integer.parseInt(bean.getHelpUpdateMinter());
//                            }
//                        } else {
//                            showToastC(response.getMessage());
//                        }
//                        Logger.i("获取更新时间： DownloadApkHour" + DownloadApkHour + " DownloadApkMinute:" + DownloadApkMinute);
//                        Logger.i("请求结果：升级apk json" + GsonUtils.getInstance().toJson(response));
//                    }
//                });
//    }

    /**
     * 激活系统cpu
     */
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


//



}