package com.yinfeng.yf_trajectory.moudle.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.caitiaobang.core.app.app.BaseActivity;
import com.caitiaobang.core.app.net.GenericsCallback;
import com.caitiaobang.core.app.net.JsonGenericsSerializator;
import com.caitiaobang.core.app.utils.ConmonUtils;
import com.orhanobut.hawk.Hawk;
import com.yinfeng.yf_trajectory.Api;
import com.yinfeng.yf_trajectory.ConstantApi;
import com.yinfeng.yf_trajectory.GsonUtils;
import com.yinfeng.yf_trajectory.R;
import com.yinfeng.yf_trajectory.mdm.MDMUtils;
import com.yinfeng.yf_trajectory.mdm.SampleDeviceReceiver;
import com.yinfeng.yf_trajectory.mdm.SampleEula;
import com.yinfeng.yf_trajectory.moudle.service.LocationService;
import com.yinfeng.yf_trajectory.moudle.service.PlayerMusicService;
import com.yinfeng.yf_trajectory.moudle.bean.UserInfoBean;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusBean;
import com.yinfeng.yf_trajectory.moudle.eventbus.EventBusUtils;
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

    @Override
    protected void initView() {
        super.initView();
        new EventBusUtils().register(this);

        mActivityMapHeadimg = (CircleImageView) findViewById(R.id.activity_map_headimg);
        mActivityMapHeadimg.setOnClickListener(this);
        mActivityMapName = (TextView) findViewById(R.id.activity_map_name);

        mActivityMapMatterApplication = (ImageView) findViewById(R.id.activity_map_matter_application);
        mActivityMapMatterApplication.setOnClickListener(this);
        mActivityMapSearch = (ImageView) findViewById(R.id.activity_map_search_icon);
        mActivityMapSearch.setOnClickListener(this);
    }

    private MDMUtils mdmUtils = null;
    private DevicePolicyManager mDevicePolicyManager = null;
    private ComponentName mAdminName = null;
    private SampleEula sampleEula = null;

    /**
     * 初始化相关组件
     */
    private void initHuaWeiHDM() {
        mdmUtils = new MDMUtils();
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminName = new ComponentName(this, SampleDeviceReceiver.class);
        sampleEula = new SampleEula(this, mDevicePolicyManager, mAdminName);
        sampleEula.activeProcessApp();
    }

    /**
     * 订阅接受者
     */
    @Subscribe
    public void onEventMainThread(EventBusBean event) {
        Toast.makeText(this, "" + event.getType(), Toast.LENGTH_SHORT).show();
        if (event.getType() == 1) {//激活
            //强制开启数据服务
            mdmUtils.forceMobiledataOn();
            //保持某应用始终运行
            mdmUtils.addPersistentApp();
            //设置应用为信任应用
            //setSuperWhiteListForHwSystemManger();
            //开启禁止卸载
            mdmUtils.setDisallowedUninstallPackages(true);

        } else if (event.getType() == 2) { //取消
            if (mAdminName != null && mAdminName != null) {
                sampleEula.activeProcessApp();
            } else {
                Toast.makeText(this, "mAdminName= null", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void initData() {
        requestDate(0, "");
        initHuaWeiHDM();
    }

    @Override
    protected void initBefore(Bundle savedInstanceState) {
        super.initBefore(savedInstanceState);
        initMap(savedInstanceState);
        startPlayMusicService();
        startLocationService();

    }


    private void stopPlayMusicService() {
        Intent intent = new Intent(MapActivity.this, PlayerMusicService.class);
        stopService(intent);
    }

    private void startPlayMusicService() {
        Intent intent = new Intent(MapActivity.this, PlayerMusicService.class);
        startService(intent);
    }

    private void startLocationService() {
        Intent intent = new Intent(MapActivity.this, LocationService.class);
        startService(intent);
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
                Log.i(ConstantApi.LOG_I, "地图定位刷新：");
            } else {
                Log.i(ConstantApi.LOG_I, "地图定位刷新 错误");
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        new EventBusUtils().unregister(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
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


                            if (!TextUtils.isEmpty(bean.getIdCard())) {
                                if (ConmonUtils.isSex(bean.getIdCard()) == 1) {
                                    mActivityMapHeadimg.setImageResource(R.mipmap.ic_home_gire);
                                } else if (ConmonUtils.isSex(bean.getIdCard()) == 2) {
                                    mActivityMapHeadimg.setImageResource(R.mipmap.ic_home_man);
                                }
                            }
                            mActivityMapName.setText(bean.getName() == "" ? "无数据" : "您好，" + bean.getName());
                        } else {
                            showToastC(response.getMessage());
                        }
                        Log.i(ConstantApi.LOG_I_NET, "请求结果：" + GsonUtils.getInstance().toJson(response));
                        dismisProgress();
                    }
                });
    }


}
