package com.yinfeng.yf_trajectory.moudle.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.app.AppManager;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.storge.LattePreference;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.LocationService;
import com.yinfeng.yf_trajectory.LocationStatusManager;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.alarm.AlarmUtil;
import com.yinfeng.yf_trajectory.alarm.ContactWorkService;
import com.yinfeng.yf_trajectory.alarm.IntentConst;
import com.yinfeng.yf_trajectory.alarm.Net;
import com.yinfeng.yf_trajectory.mdm.MDMUtils;
import com.yinfeng.yf_trajectory.moudle.bean.UserInfoBean;
import com.yinfeng.yf_trajectory.moudle.service.PlayerMusicService;
import com.yinfeng.yf_trajectory.moudle.utils.InstallAppUtils;
import com.yinfeng.yf_trajectory.moudle.utils.LocationErrUtils;
import com.yinfeng.yf_trajectory.moudle.utils.LocationManagerUtils;
import com.yinfeng.yf_trajectory.moudle.utils.NotificationManagerUtils;
import com.yinfeng.yf_trajectory.moudle.utils.WorkUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

import static com.yinfeng.yf_trajectory.moudle.utils.PermissionUtilsx.getSystemVersion;

//import com.yinfeng.yf_trajectory.moudle.login.LoginVerActivity;

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
    private ImageView mActivityMapWorkDown;
    private ImageView mActivityMapClose;
    private ImageView mActivityMapStatus;
    private ImageView mActivityMapNewStart;
    private ImageView mActivityMapLocation;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void initBefore() {
        super.initBefore();
        sInstance = this;
    }


    @Override
    protected void initView() {
        super.initView();
//        PlayVoice.playVoice(getApplicationContext(), R.raw.per_tts);
        initEventRecaver();
        createAlarm();
        mActivityMapHeadimg = (CircleImageView) findViewById(R.id.activity_map_headimg);
        mActivityMapHeadimg.setOnClickListener(this);
        mActivityMapName = (TextView) findViewById(R.id.activity_map_name);
        mActivityMapName.setOnClickListener(this);
        startPlayMusicService();
        startLocationService();
        mActivityMapWorkDown = (ImageView) findViewById(R.id.activity_map_work_down);
        mActivityMapWorkDown.setOnClickListener(this);
        mActivityMapClose = (ImageView) findViewById(R.id.activity_map_close);
        mActivityMapClose.setOnClickListener(this);
        mActivityMapStatus = (ImageView) findViewById(R.id.activity_map_status);
        mActivityMapStatus.setOnClickListener(this);
        mActivityMapNewStart = (ImageView) findViewById(R.id.activity_map_new_Start);
        mActivityMapNewStart.setOnClickListener(this);
        mActivityMapLocation = (ImageView) findViewById(R.id.activity_map_location);
        mActivityMapLocation.setOnClickListener(this);

        Logger.i("service事件广播注册...");

    }

    /**
     * 设置闹钟
     */
    private void createAlarm() {
        Intent intent = new Intent(IntentConst.Action.matchRemind);
        intent.putExtra(Net.Param.ID, 123);
        AlarmUtil.controlAlarm(MapActivity.this, 6, 13, 123, intent);


        Intent intentActon = new Intent(this, ContactWorkService.class);
        intentActon.setAction(IntentConst.Action.downloadcontact);
        startService(intentActon);

    }


    private void startPlayMusicService() {
        Intent intent = new Intent(MapActivity.this, PlayerMusicService.class);
        startService(intent);
    }

    private void stopPlayMusicService() {
        Intent intent = new Intent(MapActivity.this, PlayerMusicService.class);
        stopService(intent);
    }

    public static Activity sInstance = null;

    /**
     * 开始定位服务
     */
    private void startLocationService() {
        getApplicationContext().startService(new Intent(this, LocationService.class));
        LocationStatusManager.getInstance().resetToInit(getApplicationContext());
    }

    /**
     * 关闭服务
     * 先关闭守护进程，再关闭定位服务
     */
//    private void stopLocationService() {
//        sendBroadcast(Utils.getCloseBrodecastIntent());
//        LocationStatusManager.getInstance().resetToInit(getApplicationContext());
//    }

    private MDMUtils mdmUtils = null;

    /**
     * 初始化相关组件
     */
    private void initHuaWeiHDM() {
        mdmUtils = new MDMUtils();

        try {


//        if (!mdmUtils.isNotificationDisabled()) {
            mdmUtils.setNotificationDisabled(false);
            mdmUtils.setRestoreFactoryDisabled(false);


            mdmUtils.setSystemUpdateDisabled(false);

            if (getSystemVersion() > 1000) {
                mdmUtils.setPowerSaveModeDisabled(true);
            }
                mdmUtils.setDefaultLauncher();
//            Logger.i("通知禁用未关闭");
//        } else {
//            Logger.i("通知禁用已关闭");
//        }
            //禁用隐私空间
            if (!mdmUtils.isAddUserDisabled()) {
                mdmUtils.setAddUserDisabled(true);
                Logger.i("通知添加多用户未开启");
            } else {
                Logger.i("通知添加多用户已开启");
            }
            //禁止反激活
            mdmUtils.addDisabledDeactivateMdmPackages();
            //禁止时间设置
            if (!mdmUtils.isTimeAndDateSetDisabled()) {
                Logger.i("时间设置已未禁用");
                mdmUtils.setTimeAndDateSetDisabled(true);
            } else {
                Logger.i("时间设置已禁用");
            }

        } catch (Exception e) {
            Logger.i("mapActivity  权限设置失败" + e.getMessage());
        }
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
//        initLocation();
    }


    private void showLocation() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(60000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.ic_map_deauful_icon)));
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 自定义精度范围的圆形边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//圆圈的颜色,设为透明的时候就可以去掉园区区域了
        //控制是否显示定位蓝点
        myLocationStyle.showMyLocation(true);
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
    }

    AMap.OnMyLocationChangeListener onMyLocationChangeListener = new AMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if (location != null) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            } else {
                Logger.i("地图定位刷新 错误");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        sInstance = null;
        try {
            unregisterReceiver(mEventReceiver);
            if (ChangeEventReceiver != null)
                unregisterReceiver(ChangeEventReceiver);
        } catch (Exception e) {

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDate(0, "");
        resu();
        mMapView.onResume();
    }


    private void resu() {
        Intent mIntent = new Intent(ConstantApi.RECEIVER_ACTION_DOWNLOAD_APK);
        mIntent.putExtra("result", "downlaod");
        sendBroadcast(mIntent);

        WorkUtils.getInstance().getJudgeLeave();
        WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
            @Override
            public void successful(int status, WorkUtils.IBundleBean bundleBean) {
                if (bundleBean.getIsLeaveing() == 1) {
                    //请假中...
                    sendServiceMsg("stop");
                    mActivityMapStatus.setImageResource(R.mipmap.ic_stop);

                } else {

                    WorkUtils.getInstance().getWorkTimeStatus();
                    WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
                        @Override
                        public void successful(int status, WorkUtils.IBundleBean bundleBean) {

                        }

                        @Override
                        public void failure(String msg) {
                            showToastC(msg);
                            WaitDialog.dismiss();
                        }
                    });


                    String work_time_status = LattePreference.getValue(ConstantApi.work_time_status);
                    if (!TextUtils.isEmpty(work_time_status) && work_time_status.equals("1")) {
                        mActivityMapStatus.setImageResource(R.mipmap.ic_start);
                    } else {
                        mActivityMapStatus.setImageResource(R.mipmap.ic_stop);

                        Log.i("TESTREZ", "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ" + work_time_status);
                    }

                }

            }

            @Override
            public void failure(String msg) {
                showToastC(msg);
                WaitDialog.dismiss();
            }
        });


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.activity_map_headimg:
                ActivityUtils.startActivity(MeInfoActivity.class);
                break;

            case R.id.activity_map_name:
                break;
            case R.id.activity_map_location:
                WaitDialog.show(MapActivity.this, "正在更新位置信息请稍后...");
                LocationManagerUtils.getInstance().init();
                LocationManagerUtils.getInstance().setLocationListenerm(new LocationManagerUtils.OnLocationListenerm() {
                    @Override
                    public void OnLocationListenerm(int status, AMapLocation aMapLocation, String lat, String lng, String addr) {
                        WaitDialog.dismiss();
                        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
                        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 16, 30, 0));
                        aMap.moveCamera(mCameraUpdate);

                    }
                });

                break;
            case R.id.activity_map_work_down:
                WorkUtils.getInstance().getJudgeLeave();
                WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
                    @Override
                    public void successful(int status, WorkUtils.IBundleBean bundleBean) {

                        if (bundleBean.getIsLeaveing() == 1) {
                            //请假中...
                            showToastC("请假中...");
                        } else {
                            voidStopWorkDay();
                        }
                    }

                    @Override
                    public void failure(String msg) {
                        showToastC(msg);
                    }
                });

                break;
            case R.id.activity_map_close:
                ActivityUtils.startActivity(RequestActivity.class);
                break;
            case R.id.activity_map_status:
                break;
            case R.id.activity_map_new_Start:

                WorkUtils.getInstance().getJudgeLeave();
                WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
                    @Override
                    public void successful(int status, WorkUtils.IBundleBean bundleBean) {
                        if (bundleBean.getIsLeaveing() == 1) {
                            //请假中...
                            MessageDialog.show(MapActivity.this, "提示", "您当前处于请假中，将取消您的请假，是否继续？", "是", "否", "")
                                    .setButtonOrientation(LinearLayout.VERTICAL).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {

                                    WorkUtils.getInstance().getCancelLeave();
                                    WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
                                        @Override
                                        public void successful(int status, WorkUtils.IBundleBean bundleBean) {
                                            voidStartWorkDay();
                                        }


                                        @Override
                                        public void failure(String msg) {
                                            showToastC(msg);
                                            WaitDialog.dismiss();
                                        }
                                    });
                                    return false;
                                }
                            });

                        } else {
                            //未请假
                            voidStartWorkDay();
                        }
                    }

                    @Override
                    public void failure(String msg) {
                        showToastC(msg);
                        WaitDialog.dismiss();
                    }
                });
                break;
        }
    }

    private void voidStopWorkDay() {
        WorkUtils.getInstance().getIsWorkDay();
        WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
            @Override
            public void successful(int status, WorkUtils.IBundleBean bundleBean) {
                if (bundleBean.getIsWorkDay() == 2) {
                    //非工作日  不用做是否上班中判断
                    stopLoc(2);
                } else {

                    //工作日    上班中不允许下班
                    WorkUtils.getInstance().getWorkTimeStatus();
                    WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
                        @Override
                        public void successful(int status, WorkUtils.IBundleBean bundleBean) {
                            if (bundleBean.getIsWorking() == 1) {
                                //工作中
                                showToastC("工作中...");
                            } else {
                                stopLoc(1);
                            }
                        }

                        @Override
                        public void failure(String msg) {
                            showToastC(msg);
                        }
                    });


                }
            }

            @Override
            public void failure(String msg) {
                showToastC(msg);
                WaitDialog.dismiss();
            }
        });

    }

    private void voidStartWorkDay() {

        WorkUtils.getInstance().getIsWorkDay();
        WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
            @Override
            public void successful(int status, WorkUtils.IBundleBean bundleBean) {
                if (bundleBean.getIsWorkDay() == 2) {
                    //非工作日
                    startLoc(2);
                } else {
                    //工作日

                    WorkUtils.getInstance().getWorkTimeStatus();
                    WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
                        @Override
                        public void successful(int status, WorkUtils.IBundleBean bundleBean) {
                            if (bundleBean.getIsWorking() == 1) {
                                //工作中
                                showToastC("定位服务已开启~");
                            } else {
                                startLoc(1);
                            }
                        }

                        @Override
                        public void failure(String msg) {
                            showToastC(msg);
                            WaitDialog.dismiss();
                        }
                    });


                }
            }

            @Override
            public void failure(String msg) {
                showToastC(msg);
                WaitDialog.dismiss();
            }
        });

    }


    /**
     * type ==1 工作日 开启   type==2 非工作日 开启
     */
    private void startLoc(int type) {
        MessageDialog.show(MapActivity.this, "提示", "启动定位服务，将开始记录您的位置，请确认？", "是", "否", "")
                .setButtonOrientation(LinearLayout.VERTICAL).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                sendServiceMsg("start");
                mActivityMapStatus.setImageResource(R.mipmap.ic_start);
                if (type == 1) {
                    LattePreference.saveKey(ConstantApi.work_time_status, "1");
//                    String work_time_status = LattePreference.getValue(ConstantApi.work_time_status);
//
//                    Log.i("TESTREZ","XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"+work_time_status);


                } else {
                    LattePreference.saveKey(ConstantApi.work_time_status, "1");

                    LattePreference.saveKey(ConstantApi.work_day_isLocation_status, "1");
                }
                return false;
            }
        });
    }

    /**
     * type ==1 工作日 关闭   type==2 非工作日 关闭
     */
    private void stopLoc(int type) {
        //可以下班 获取最新位置
        WaitDialog.show(MapActivity.this, "正在更新位置信息请稍后...");


        LocationManagerUtils.getInstance().init();
        LocationManagerUtils.getInstance().setLocationListenerm(new LocationManagerUtils.OnLocationListenerm() {
            @Override
            public void OnLocationListenerm(int status, AMapLocation aMapLocation, String lat, String lng, String addr) {
                WaitDialog.dismiss();
                if (status == 1) {
                    MessageDialog.show(MapActivity.this, "当前位置", addr, "确定", "取消", "")
                            .setButtonOrientation(LinearLayout.VERTICAL)
                            .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    sendServiceMsg("stop");
                                    mActivityMapStatus.setImageResource(R.mipmap.ic_stop);
                                    if (type == 1) {
                                        LattePreference.saveKey(ConstantApi.work_time_status, "0");
                                    } else {

                                        LattePreference.saveKey(ConstantApi.work_time_status, "0");

                                        LattePreference.saveKey(ConstantApi.work_day_isLocation_status, "2");
                                    }
                                    //更新一下数据库数据
                                    sendServiceMsg("upload");
                                    //调用接口说明记录一下
                                    WorkUtils.getInstance().getOffWork();
                                    WorkUtils.getInstance().setOnWorkListener(new WorkUtils.OnWorkListenerI() {
                                        @Override
                                        public void successful(int status, WorkUtils.IBundleBean bundleBean) {

                                        }

                                        @Override
                                        public void failure(String msg) {

                                        }
                                    });
                                    return false;
                                }


                            });
                } else {
                    WaitDialog.dismiss();
                    String errInfo;
                    if (aMapLocation == null) {
                        errInfo = "位置偏僻,逆地理编码获取失败，请重试";
                    } else {
                        errInfo = LocationErrUtils.getInstance().showErr(aMapLocation.getErrorCode());
                    }
                    MessageDialog.show(MapActivity.this, "提示", errInfo, "重试", "取消", "")
                            .setButtonOrientation(LinearLayout.VERTICAL).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            stopLoc(type);
                            return false;
                        }
                    });
                }
            }
        });

    }


    /**
     * 获取一次定位信息
     */
//    private void getOneAddress() {
//        WaitDialog.show(MapActivity.this, "正在获取您的最新位置请稍后...");
//        LocationManagerUtils.getInstance().init();
//        LocationManagerUtils.getInstance().setLocationListenerm(new LocationManagerUtils.OnLocationListenerm() {
//            @Override
//            public void OnLocationListenerm(int status, AMapLocation aMapLocation, String lat, String lng, String addr) {
//                if (status == 1) {
//                    WaitDialog.dismiss();
//                    MessageDialog.show(MapActivity.this, "当前位置", addr, "确定", "取消", "")
//                            .setButtonOrientation(LinearLayout.VERTICAL)
//                            .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
//                                @Override
//                                public boolean onClick(BaseDialog baseDialog, View v) {
//
//                                    return false;
//                                }
//                            });
//                } else {
//                    MessageDialog.show(MapActivity.this, "提示", aMapLocation.getErrorInfo(), "重试", "取消", "")
//                            .setButtonOrientation(LinearLayout.VERTICAL).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
//                        @Override
//                        public boolean onClick(BaseDialog baseDialog, View v) {
//                            getOneAddress();
//                            return false;
//                        }
//                    });
//                }
//
//            }
//        });
//    }
    private void sendServiceMsg(String status) {
        Intent mIntent = new Intent(ConstantApi.service_action);
        mIntent.putExtra("event", status);
        sendBroadcast(mIntent);
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
                        Logger.i("请求结果：查询个人信息" + GsonUtils.getInstance().toJson(response));
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
                mActivityMapHeadimg.setImageResource(R.mipmap.ic_home_man);
            } else if (ConmonUtils.isSex(bean.getIdCard()) == 2) {
                mActivityMapHeadimg.setImageResource(R.mipmap.ic_home_gire);
            }

        }
        mActivityMapName.setText(bean.getName() == "" ? "无数据" : "" + bean.getName());

    }

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
                        NotificationManagerUtils.startNotificationManager("银丰轨迹下载成功", R.mipmap.ic_app_start_icon);
                        mdmUtils.installApk(true, "track");

                    } else if (locationResult.equals(ConstantApi.RECEVIER_DOWNLOAD_HELP_APK)) { //下载轨迹apk
                        NotificationManagerUtils.startNotificationManager("银丰轨迹助手下载成功", R.mipmap.ic_app_start_icon);
                        mdmUtils.installApk(true, "help");

                    } else if (locationResult.equals(ConstantApi.RECEVIER_901)) { //异常登录
                        LattePreference.clear();
                        sendServiceMsg("stop");
                        stopPlayMusicService();
                        showTwo("您的账号在其他地方登陆！");
                    } else if (locationResult.equals(ConstantApi.RECEIVER_ACTION_DOWNLOAD_ALIVE_HELP_APK)) {//拉活辅助手
                        InstallAppUtils.openPackage(MapActivity.this, "com.yinfeng.yf_trajectory_help");
                    } else if (locationResult.equals(ConstantApi.RECEVIER_NO_SIM_READY)) {//WUKA
                        LattePreference.clear();
                        AppManager.getInstance().finishAllActivity();
                        sendServiceMsg("stop");
                        stopPlayMusicService();
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
        getApplication().registerReceiver(ChangeEventReceiver, intentFilter);


        IntentFilter eventFilter = new IntentFilter();
        eventFilter.addAction(ConstantApi.activity_action);
        registerReceiver(mEventReceiver, eventFilter);

    }


    private void loginErr() {
        Toast.makeText(MapActivity.this, "您的账号在其他地方登陆！", Toast.LENGTH_SHORT).show();
        Logger.i("账号在其他地方登陆");
        LattePreference.clear();
        AppManager.getInstance().finishAllActivity();
        finish();
    }


    private BroadcastReceiver mEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String mType = intent.getStringExtra("event");
                Message message = Message.obtain();
                message.what = 202;
                Bundle bundle = new Bundle();
                bundle.putString("event", mType);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }
    };

    private Handler mHandler = new LoopHandler();


    private class LoopHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 202) {
                Bundle bundle = msg.getData();
                String event = bundle.getString("event");
                Logger.i("Activity event: " + event);
                if (!TextUtils.isEmpty(event)) {
                    if (event.equals("stop")) {
                        mActivityMapStatus.setImageResource(R.mipmap.ic_stop);
                    } else if (event.equals("start")) {
                        mActivityMapStatus.setImageResource(R.mipmap.ic_start);
                    }
                }
            }
        }
    }
}
