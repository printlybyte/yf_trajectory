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
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.LocationService;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.moudle.bean.ApkDownloadBean;
import com.yinfeng.yf_trajectory.moudle.bean.MatterApplicationActivityStatusBean;
import com.yinfeng.yf_trajectory.moudle.utils.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Call;


/**
 * 循环播放一段无声音频，以提升进程优先级
 * <p>
 * Created by jianddongguo on 2017/7/11.
 * http://blog.csdn.net/andrexpert
 */

public class PlayerMusicService extends Service {
    private final static String TAG = "PlayerMusicService";
    private MediaPlayer mMediaPlayer;
    public static final boolean DEBUG = true;

    public static final String PACKAGE_NAME = "com.jiangdg.keepappalive";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG)
            Log.d(TAG, TAG + "---->onCreate,启动服务");
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        mMediaPlayer.setLooping(true);


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantApi.RECEIVER_ACTION_DOWNLOAD_APK);
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

    private void startPlayMusic() {
        if (mMediaPlayer != null) {
            if (DEBUG)
                Log.d(TAG, "启动后台播放音乐");
            mMediaPlayer.start();
        }
    }

    private void stopPlayMusic() {
        if (mMediaPlayer != null) {
            if (DEBUG)
                Log.d(TAG, "关闭后台播放音乐");
            mMediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayMusic();
        if (DEBUG)
            Log.d(TAG, TAG + "---->onCreate,停止服务");
        // 重启自己
        Intent intent = new Intent(getApplicationContext(), PlayerMusicService.class);
        startService(intent);

        if (locationChangeBroadcastReceiver != null)
            unregisterReceiver(locationChangeBroadcastReceiver);

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
                    requestDate();
                }
            }
        }
    };

    private void requestDate() {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("token = null ");
            return;
        }
        Log.i(ConstantApi.LOG_I_NET, "API: " + Api.API_apply_judge);
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
                .url(Api.API_appVersion_judge)
                .build()
                .execute(new GenericsCallback<ApkDownloadBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        showToastC("网络异常，请稍后重试" + e.getMessage());
                    }

                    @Override
                    public void onResponse(ApkDownloadBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            if (!response.getData().isNewest()) { //有新版本
                                String DownLoadUrl = response.getData().getAppVersion().getDownLoadUrl();
                                if (!TextUtils.isEmpty(DownLoadUrl)) {
                                    downloadFile(response.getData().getAppVersion().getDownLoadUrl());
                                } else {
                                    Log.i(ConstantApi.LOG_I_NET, "DownLoadUrl=null");
                                }
                            } else {
                                Log.i(ConstantApi.LOG_I_NET, "无新版本");
                            }
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i(ConstantApi.LOG_I_NET, "请求结果：" + GsonUtils.getInstance().toJson(response));
                    }
                });
    }


    private void downloadFile(String mUrl) {
        String mPath;
        if (FileUtils.createOrExistsDir(ConstantApi.CommonApkPath)) {
//                    showToastC("");创建成功
            mPath = ConstantApi.CommonApkPath;
        } else {
            mPath = ConstantApi.CommonPath;
        }
        OkGo.<File>get(mUrl) //
                .execute(new FileCallback(mPath, ConstantApi.CommonApkName) {
                    @Override
                    public void onSuccess(Response<File> response) {
//                                e.onComplete();
                    }

                    @Override
                    public void onError(Response<File> response) {
//                                e.onError(response.getException());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
//                                e.onNext(progress);
                    }
                });
    }
}