package com.yinfeng.yf_trajectory.moudle.activity;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.app.Latte;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.LocationService;
import com.yinfeng.yf_trajectory.LocationStatusManager;
import com.yinfeng.yf_trajectory.PowerManagerUtil;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.Utils;
import com.yinfeng.yf_trajectory.mdm.MDMUtils;
import com.yinfeng.yf_trajectory.mdm.SampleDeviceReceiver;
import com.yinfeng.yf_trajectory.mdm.SampleEula;
import com.yinfeng.yf_trajectory.moudle.bean.UploadLocationInfoBean;
import com.yinfeng.yf_trajectory.moudle.bean.UserInfoBean;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusBean;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusUtils;
//import com.yinfeng.yf_trajectory.moudle.login.LoginVerActivity;
import com.yinfeng.yf_trajectory.moudle.login.SMSActivity;
import com.yinfeng.yf_trajectory.moudle.service.PlayerMusicService;
import com.yinfeng.yf_trajectory.moudle.utils.InstallAppUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.Subscribe;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * ============================================
 * 描  述：两次定位 地图sdk一次  5.0api一次 分别处理定位上报 与地图舒心  锁屏将定位数据插greendao数据库
 * 包  名：com.yinfeng.yf_trajectory
 * 类  名：MapActivity
 * 创建人：liuguodong
 * 创建时间：2019/8/6 14:47
 * ============================================
 **/
public class MapActivity extends BaseActivity implements View.OnClickListener {

    private CircleImageView mActivityMapHeadimg;
    private TextView mActivityMapName;
    private ImageView mActivityMapMatterApplication;
    private ImageView mActivityMapSearch;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_map;
    }

    private PowerManager powerManager;


    /**
     * 事件广播
     */
    private BroadcastReceiver ChangeEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConstantApi.RECEIVER_ACTION)) {
                String locationResult = intent.getStringExtra("result");
                if (null != locationResult && !locationResult.trim().equals("")) {
                    if (locationResult.equals(ConstantApi.RECEVIER_DOWNLOAD_APK)) {//下载轨迹apk
                        mdmUtils.installApk(true, "track");
                    } else if (locationResult.equals(ConstantApi.RECEVIER_DOWNLOAD_HELP_APK)) {//下载轨迹助手
                        mdmUtils.installApk(true, "help");

                    } else if (locationResult.equals(ConstantApi.RECEVIER_901)) {//异常登录

                        LattePreference.clear();
                        showTwo("您的账号在其他地方登陆！");

                    } else if (locationResult.equals(ConstantApi.RECEIVER_ACTION_DOWNLOAD_ALIVE_HELP_APK)) {//拉活辅助手
//
                        InstallAppUtils.openPackage(MapActivity.this, "com.yinfeng.yf_trajectory_help");

                    } else if (locationResult.equals(ConstantApi.RECEVIER_NO_SIM_READY)) {//WUKA
                        LattePreference.clear();
                        AppManager.getInstance().finishAllActivity();
                        ActivityUtils.startActivity(SMSActivity.class);

                    }
                }
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * 注册事件广播
     */
    private void initEventRecaver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantApi.RECEIVER_ACTION);
        registerReceiver(ChangeEventReceiver, intentFilter);
    }

    private void loginErr() {
        Toast.makeText(MapActivity.this, "您的账号在其他地方登陆！", Toast.LENGTH_SHORT).show();
        Logger.v("账号在其他地方登陆");
        LattePreference.clear();
        AppManager.getInstance().finishAllActivity();
        finish();


//        Intent dialogIntent = new Intent(getBaseContext(), LoginVerActivity.class);
//        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplication().startActivity(dialogIntent);
    }

    @Override
    protected void initView() {
        super.initView();
        initEventRecaver();
        mActivityMapHeadimg = (CircleImageView) findViewById(R.id.activity_map_headimg);
        mActivityMapHeadimg.setOnClickListener(this);
        mActivityMapName = (TextView) findViewById(R.id.activity_map_name);

        mActivityMapMatterApplication = (ImageView) findViewById(R.id.activity_map_matter_application);
        mActivityMapMatterApplication.setOnClickListener(this);
        mActivityMapSearch = (ImageView) findViewById(R.id.activity_map_search_icon);
        mActivityMapSearch.setOnClickListener(this);
        mActivityMapName.setOnClickListener(this);

        startPlayMusicService();
        startLocationService();


    }


    private void startPlayMusicService() {
        Intent intent = new Intent(MapActivity.this, PlayerMusicService.class);
        startService(intent);
    }


    private void startLocationService() {
        getApplicationContext().startService(new Intent(this, LocationService.class));
        LocationStatusManager.getInstance().resetToInit(getApplicationContext());

    }

    private MDMUtils mdmUtils = null;

    /**
     * 初始化相关组件
     */
    private void initHuaWeiHDM() {
        mdmUtils = new MDMUtils();

        mdmUtils.setNotificationDisabled(false);
        //禁用隐私空间
        mdmUtils.setAddUserDisabled(true);
        //禁止反激活
        mdmUtils.addDisabledDeactivateMdmPackages();

//        mdmUtils.setPackageManager();
    }


    @Override
    protected void initData() {

        UserInfoBean.DataBean bean = Hawk.get(ConstantApi.HK_USER_BEAN);
        if (bean != null) {
            setUserDate(bean);
        }
        initHuaWeiHDM();

    }


    @Override
    protected void initBefore(Bundle savedInstanceState) {
        super.initBefore(savedInstanceState);
        initMap(savedInstanceState);

    }


    //地图=======================================================================
    private MapView mMapView = null;
    private AMap aMap;

    private void initMap(Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        showLocation();

    }


    private void showLocation() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。

        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.ic_map_deauful_icon)));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 自定义精度范围的圆形边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//圆圈的颜色,设为透明的时候就可以去掉园区区域了

        //控制是否显示定位蓝点
        myLocationStyle.showMyLocation(true);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(onMyLocationChangeListener);


    }

    AMap.OnMyLocationChangeListener onMyLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (location != null) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
//                Logger.v( "地图定位刷新：");
            } else {
                Logger.v("地图定位刷新 错误");
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (ChangeEventReceiver != null)
            unregisterReceiver(ChangeEventReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDate(0, "");
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    //=======================================================================
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_map_headimg:
                ActivityUtils.startActivity(MeInfoActivity.class);
                break;
            case R.id.activity_map_matter_application:
                ActivityUtils.startActivity(MatterApplicationActivity.class);
                break;
            case R.id.activity_map_search_icon:
                Intent intent = new Intent(MapActivity.this, ViewTrackMapActivity.class);
                intent.putExtra(ConstantApi.INTENT_FLAG, ConstantApi.query_search);
                startActivity(intent);
                break;
            case R.id.activity_map_name:

                break;
        }
    }


    /**
     * 获取个人信息 track-token
     */

    private void requestDate(final int type, String msg) {
        if (!NetworkUtils.isConnected()) {
            showToastC("网络无链接,请稍后在试");
            return;
        }
        if (type == 1) {
            showProgress(msg);
        }
        String token = Hawk.get(ConstantApi.HK_TOKEN);
        if (TextUtils.isEmpty(token)) {
            showToastC("个人信息查询失败");
            return;
        }
        OkHttpUtils
                .post()
                .addHeader("track-token", token)
                .url(Api.API_user_info)
                .build()
                .execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        showToastC("网络异常，请稍后重试" + e.getMessage());
                        if (type == 1) {
                            dismisProgress();
                        }
                    }

                    @Override
                    public void onResponse(UserInfoBean response, int id) {
                        if (type == 1) {
                            dismisProgress();
                        }
                        if (response != null && response.getCode() == ConstantApi.API_REQUEST_SUCCESS && response.isSuccess()) {
                            UserInfoBean.DataBean bean = response.getData();
                            Hawk.put(ConstantApi.HK_USER_BEAN, response.getData());
                            setUserDate(bean);
                        } else if (response.getCode() == ConstantApi.API_REQUEST_ERR_901) {
                            LattePreference.clear();
                            showTwo(response.getMessage());

                        } else {
                            showToastC(response.getMessage());
                        }
                        Logger.v("请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }

    /**
     * 提交手机号数据
     */
    private void showTwo(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_app_start_icon).setTitle("提示")
                .setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginErr();
//                        ActivityUtils.startActivity(LoginVerActivity.class);
                        //ToDo: 你想做的事情
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        loginErr();
                    }
                });
        builder.create().show();
    }

    private void setUserDate(UserInfoBean.DataBean bean) {
        if (!TextUtils.isEmpty(bean.getIdCard())) {
            if (ConmonUtils.isSex(bean.getIdCard()) == 1) {
                mActivityMapHeadimg.setImageResource(R.mipmap.ic_home_gire);
            } else if (ConmonUtils.isSex(bean.getIdCard()) == 2) {
                mActivityMapHeadimg.setImageResource(R.mipmap.ic_home_man);
            }
        }
        mActivityMapName.setText(bean.getName() == "" ? "无数据" : "您好，" + bean.getName());

    }
}
