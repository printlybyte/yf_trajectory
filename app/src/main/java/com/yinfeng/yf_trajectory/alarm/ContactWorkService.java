package com.yinfeng.yf_trajectory.alarm;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.LocationService;
import com.yinfeng.yf_trajectory.LocationStatusManager;
import com.yinfeng.yf_trajectory.moudle.bean.ContactWorkServiceBean;
import com.yinfeng.yf_trajectory.moudle.bean.LeaveHistoryActivityBean;
import com.yinfeng.yf_trajectory.moudle.utils.WorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;

/**
 * ============================================
 * 描  述：
 * 包  名：com.yinfeng.yf_trajectory.alarm
 * 类  名：ContactWorkService
 * 创建人：liuguodong
 * 创建时间：2020/2/19 15:50
 * ============================================
 **/
public class ContactWorkService extends IntentService {

    public ContactWorkService() {
        super("ContactWorkService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        final String action = intent.getAction();
        Logger.i("通讯录服务 onHandleIntent" + "  action:" + action);
        if (!TextUtils.isEmpty(action)) {
            if (action.equals(IntentConst.Action.downloadcontact)) {
                requestDate();
            } else {

            }
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.i("通讯录服务 onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("通讯录服务 onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

//        sendServiceMsg("start");
        Logger.i("onDestroy");
        Logger.i("通讯录服务 完成 重启定位服务");
        startLocationService();
        super.onDestroy();

    }

    private void startLocationService() {
        getApplicationContext().startService(new Intent(this, LocationService.class));
        LocationStatusManager.getInstance().resetToInit(getApplicationContext());
    }


    private void sendServiceMsg(String status) {
        Intent mIntent = new Intent(ConstantApi.service_action);
        mIntent.putExtra("event", status);
        sendBroadcast(mIntent);
    }

    private void showToast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    private void requestDate() {
        Logger.i("通讯录服务 requestDate");
        if (!NetworkUtils.isConnected()) {
            Toast.makeText(this, "网络无连接", Toast.LENGTH_SHORT).show();
            Logger.i("通讯录数据下载失败，无网络连接");
            return;
        }

        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(this, "token = null ", Toast.LENGTH_SHORT).show();
            Logger.i("通讯录数据下载失败，无token");

            WorkUtils.getInstance().getOffWork_callback();
            return;
        }
        OkHttpUtils
                .get()
                .addHeader("track-token", token)
                .url(Api.appQueryByAddressBook)
                .build()
                .execute(new GenericsCallback<ContactWorkServiceBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        WorkUtils.getInstance().getOffWork_callback();
                        showToast("通讯录数据下载失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(ContactWorkServiceBean response, int id) {
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.getData().getList().size() > 0) {
                            Logger.i("联系人开始写入......");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //设置其耗时操作
                                    try {
                                        Thread.sleep(1000);
                                        for (int i = 0; i < response.getData().getList().size(); i++) {
                                            String phone = response.getData().getList().get(i).getPhone();
                                            String name = response.getData().getList().get(i).getName();
                                            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(name)) {
                                                ContactUtils.savePhone(name, phone);
                                            }
                                        }
                                    } catch (InterruptedException e) {
                                        Log.i("耗时操作", e.getMessage());
                                    }
                                }
                            }).start();
                            showToast("通讯录同步成功");
                        } else {
                            showToast("通讯录同步失败");
                            WorkUtils.getInstance().getOffWork_callback();
                            Logger.i("通讯录数据下载失败 date =null ");
                        }


                        Logger.i("请求结果：通讯录数据 size" + response.getData().getList().size());
                        Logger.i("联系人写入成功 : 共" + response.getData().getList().size());
                    }
                });
    }
}
